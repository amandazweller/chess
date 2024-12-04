package server;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import chess.ChessGame;
import model.GameData;
import websocket.messages.*;
import websocket.messages.Error;

import java.io.IOException;
import java.util.Collection;

@WebSocket
public class WebsocketHandler {

    AuthDAO authDAO = new MySqlAuthDAO();
    GameDAO gameDAO = new MySqlGameDAO();
    MySqlDAO mySqlDAO = new MySqlDAO();

    public WebsocketHandler() throws DataAccessException {
    }

    @OnWebSocketConnect
    public void handleConnect(Session session) {
        Server.allSessions.put(session, 0);
    }

     @OnWebSocketClose
    public void handleClose(Session session, int code, String message) {
        Server.allSessions.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException, InvalidMoveException {
        System.out.printf("Received: %s%n", message);

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, new Gson().fromJson(message, Connect.class));
            case MAKE_MOVE -> makeMove(session, new Gson().fromJson(message, MakeMove.class));
            case LEAVE -> leave(session, new Gson().fromJson(message, Leave.class));
            case RESIGN -> resign(session, new Gson().fromJson(message, Resign.class));
            default -> sendError(session, new Error("Error: Unrecognized command type"));
        }
    }

    private void sendError(Session session, Error error) throws IOException {
        System.out.printf("Error: %s%n", new Gson().toJson(error));
        session.getRemote().sendString(new Gson().toJson(error));
    }

    private void resign(Session session, Resign command) throws IOException {
        try {
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            GameData game = gameDAO.getGame(command.getGameID());
            ChessGame.TeamColor playerColor = getTeamColor(auth.username(), game);

            String opponentUsername;
            if (playerColor == ChessGame.TeamColor.WHITE) {
                opponentUsername = game.blackUsername();
            } else {
                opponentUsername = game.whiteUsername();
            }

            if (game.game().isGameOver()) {
                sendError(session, new Error("Error: You cannot resign, the game is over already"));
                return;
            }

            if (playerColor == null) {
                sendError(session, new Error("Error: You are observing this game"));
                return;
            }

            game.game().setGameOver(true);
            var statement = "UPDATE gameData SET game=? WHERE gameID=?";
            mySqlDAO.executeUpdate(statement, game.game(), game.gameID());
            Notification notification = new Notification("%s has forfeited, %s wins!".formatted(auth.username(), opponentUsername));
            notifyEveryone(session, notification, game.gameID(), true);
        } catch (DataAccessException e) {
            sendError(session, new Error("Error: Not authorized"));
        } catch (ResponseException e) {
            sendError(session, new Error("Error: invalid game"));
        }
    }

    private void leave(Session session, Leave command) throws IOException {
        try {
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            GameData game = gameDAO.getGame(command.getGameID());
            Notification notification = new Notification("%s has left the game".formatted(auth.username()));
            notifyAll(session, notification, game.gameID());

            if (game.whiteUsername().equals(auth.username())){
                var statement = "UPDATE gameData SET whiteUsername = ? WHERE gameID=?";
                mySqlDAO.executeUpdate(statement, null, game.gameID());
            }
            if (game.blackUsername().equals(auth.username())){
                var statement = "UPDATE gameData SET blackUsername = ? WHERE gameID=?";
                mySqlDAO.executeUpdate(statement, null, game.gameID());
            }
            session.close();
        } catch (DataAccessException e) {
            sendError(session, new Error("Error: Not authorized"));
        }
    }

    private void makeMove(Session session, MakeMove command) throws DataAccessException, IOException, InvalidMoveException {
        try {
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            if (auth == null){
                sendError(session, new Error("Invalid authToken."));
                return;
            }
            GameData game = gameDAO.getGame(command.getGameID());
            ChessGame.TeamColor playerColor = getTeamColor(auth.username(), game);
            if (game.game().getTeamTurn() != playerColor){
                sendError(session, new Error("Please wait until your turn"));
                return;
            }
            if (game.game().getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor() != playerColor){
                sendError(session, new Error("Can't move other team's pieces. Try again."));
                return;
            }
            if (playerColor == null) {
                sendError(session, new Error("Error: You are only observing this game"));
                return;
            }

            if (game.game().isGameOver()) {
                sendError(session, new Error("Error: the game is over, not able to make a move"));
                return;
            }

            boolean invalidMove = game.game().checkValidMove(command.getMove());
            if (invalidMove){
                sendError(session, new Error("Invalid Move."));
                return;
            }
            game.game().makeMove(command.getMove());
            broadcastGameUpdate(session, auth.username(), game, playerColor);
        } catch (ResponseException e) {
            sendError(session, new Error("Error: Game is over"));
        }
    }

    private void broadcastGameUpdate(Session session, String username, GameData game, ChessGame.TeamColor playerColor)
            throws DataAccessException, IOException {
        Notification notification;
        ChessGame.TeamColor opponentColor;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            opponentColor = ChessGame.TeamColor.BLACK;
        } else {
            opponentColor = ChessGame.TeamColor.WHITE;
        }
        if (game.game().isInCheckmate(opponentColor)) {
            System.out.println("in checkmate");
            notification = new Notification("Checkmate! %s wins!".formatted(username));
            game.game().setGameOver(true);
        } else if (game.game().isInStalemate(opponentColor)) {
            System.out.println("in stalemate");
            notification = new Notification("Stalemate caused by %s's move! It's a tie!".formatted(username));
            game.game().setGameOver(true);
        } else if (game.game().isInCheck(opponentColor)) {
            notification = new Notification("%s made a move. %s is now in check!".formatted(username, opponentColor));
        } else {
            notification = new Notification("%s made a move.".formatted(username));
        }
        notifyAll(session, notification, game.gameID());
        var statement = "UPDATE gameData SET game=? WHERE gameID=?";
        notifyEveryone(session, new LoadGame(game.game()), game.gameID(), true);
        mySqlDAO.executeUpdate(statement, game.game(), game.gameID());
    }

    private void connect(Session session, Connect command) throws IOException {
        try {
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            GameData game = gameDAO.getGame(command.getGameID());

            if (auth == null){
                sendError(session, new Error("Error: Not authorized"));
                return;
            }
            if (game == null){
                sendError(session, new Error("Error: Not a valid game"));
                return;
            }
            Server.allSessions.put(session, game.gameID());

            if (command.getColor() == null){
                Notification notification = new Notification("%s joined the game".formatted(auth.username()));
                notifyAll(session, notification, game.gameID());
                LoadGame load = new LoadGame(game.game());
                sendMessage(session, load);
                return;
            }

            ChessGame.TeamColor playerColor = command.getColor().toString().equalsIgnoreCase("white")
                    ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

            boolean isCorrectColor;
            if (playerColor == ChessGame.TeamColor.WHITE) {
                isCorrectColor = game.whiteUsername().equals(auth.username());
            } else {
                isCorrectColor = game.blackUsername().equals(auth.username());
            }

            if (!isCorrectColor) {
                Error error = new Error("Error: trying to join with wrong color");
                sendError(session, error);
                return;
            }
            Notification notification = new Notification("%s joined the game as %s player".formatted(auth.username(), command.getColor().toString()));
            notifyAll(session, notification, game.gameID());
            LoadGame load = new LoadGame(game.game());
            sendMessage(session, load);
        }catch (DataAccessException e) {
            sendError(session, new Error("Error: Not authorized"));
        } catch (ResponseException e) {
            sendError(session, new Error("Error: Not a valid game"));
        }
    }

    public void notifyAll(Session currentSession, ServerMessage message, int gameID) throws IOException {
        notifyEveryone(currentSession, message, gameID, false);
    }

    public void notifyEveryone(Session currentSession, ServerMessage message, int gameID, boolean toSelf) throws IOException {
        System.out.printf("Broadcasting (toSelf: %s): %s%n", toSelf, new Gson().toJson(message));
        for (Session session : Server.allSessions.keySet()) {
            boolean inGame = Server.allSessions.get(session) != null;
            boolean inSameGame = Server.allSessions.get(session).equals(gameID);
            boolean isSelf = session == currentSession;
            if ((toSelf || !isSelf) && inGame && inSameGame) {
                sendMessage(session, message);
            }
        }
    }

    private ChessGame.TeamColor getTeamColor(String username, GameData game) {
        if (username.equals(game.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        }
        else if (username.equals(game.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        else{
            return null;
        }
    }

    public void sendMessage(Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

}
