package server;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.*;
import chess.ChessGame;
import model.GameData;
import websocket.messages.*;
import websocket.messages.Error;

import java.io.IOException;

public class WebsocketHandler {
    AuthDAO authDAO = new MySqlAuthDAO();
    UserDAO userDAO = new MySqlUserDAO();
    GameDAO gameDAO = new MySqlGameDAO();
    MySqlDAO mySqlDAO = new MySqlDAO();

    public WebsocketHandler() throws DataAccessException {
    }

    public void handleConnect(Session session) {
            Server.allSessions.put(session, 0);
        }

        public void handleClose(Session session, int code, String message) {
            Server.allSessions.remove(session);
        }

    public void handleMessage(Session session, String message) throws DataAccessException, IOException, InvalidMoveException {
        System.out.printf("Received: %s%n", message);

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, new Gson().fromJson(message, JoinPlayer.class));
            case JOIN_OBSERVER -> joinObserver(session, new Gson().fromJson(message, JoinObserver.class));
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
            mySqlDAO.executeUpdate(auth.authToken(), game);
            Notification notification = new Notification("%s has forfeited, %s wins!".formatted(auth.username(), opponentUsername));
            notifyEveryone(session, notification, true);
        } catch (DataAccessException e) {
            sendError(session, new Error("Error: Not authorized"));
        } catch (ResponseException e) {
            sendError(session, new Error("Error: invalid game"));
        }
    }

    private void leave(Session session, Leave command) throws IOException {
        try {
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            Notification notification = new Notification("%s has left the game".formatted(auth.username()));
            notifyAll(session, notification);

            session.close();
        } catch (DataAccessException e) {
            sendError(session, new Error("Error: Not authorized"));
        }
    }

    private void makeMove(Session session, MakeMove command) throws DataAccessException, IOException, InvalidMoveException {
        AuthData auth = authDAO.getAuth(command.getAuthToken());
        GameData game = gameDAO.getGame(command.getGameID());
        ChessGame.TeamColor playerColor = getTeamColor(auth.username(), game);
        if (playerColor == null) {
            sendError(session, new Error("Error: You are only observing this game"));
            return;
        }
        if (game.game().isGameOver()) {
            sendError(session, new Error("Error: the game is over, not able to make a move"));
            return;
        }
        if (!game.game().getTeamTurn().equals(playerColor)) {
            sendError(session, new Error("Error: It is not your turn"));
            return;
        }
        game.game().makeMove(command.getMove());
        ChessGame.TeamColor opponentColor = playerColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        broadcastGameUpdate(session, auth.username(), game, playerColor);
    }

    private void broadcastGameUpdate(Session session, String username, GameData game, ChessGame.TeamColor playerColor) throws DataAccessException, IOException {
        Notification notification;

        ChessGame.TeamColor opponentColor;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            opponentColor = ChessGame.TeamColor.BLACK;
        } else {
            opponentColor = ChessGame.TeamColor.WHITE;
        }

        if (game.game().isInCheckmate(opponentColor)) {
            notification = new Notification("Checkmate! %s wins!".formatted(username));
            game.game().setGameOver(true);
        } else if (game.game().isInStalemate(opponentColor)) {
            notification = new Notification("Stalemate caused by %s's move! It's a tie!".formatted(username));
            game.game().setGameOver(true);
        } else if (game.game().isInCheck(opponentColor)) {
            notification = new Notification("%s made a move. %s is now in check!".formatted(username, opponentColor));
        } else {
            notification = new Notification("%s made a move.".formatted(username));
        }

        notifyAll(session, notification);
        mySqlDAO.executeUpdate(username, game);
        notifyAll(session, new LoadGame(game.game()));
    }

    private void joinObserver(Session session, JoinObserver command) throws IOException {
        try {
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            GameData game = gameDAO.getGame(command.getGameID());

            Notification notification = new Notification("%s has joined the game as an observer".formatted(auth.username()));
            notifyAll(session, notification);

            LoadGame load = new LoadGame(game.game());
            sendMessage(session, load);
        }
        catch (DataAccessException e) {
            sendError(session, new Error("Error: Not authorized"));
        } catch (ResponseException e) {
            sendError(session, new Error("Error: Not a valid game"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void joinPlayer(Session session, JoinPlayer command) throws DataAccessException, IOException {
        try {
            AuthData auth = authDAO.getAuth(command.getAuthToken());

            GameData game = gameDAO.getGame(command.getGameID());

            ChessGame.TeamColor playerColor = command.getColor().toString().equalsIgnoreCase("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

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
            notifyAll(session, notification);

            LoadGame load = new LoadGame(game.game());
            sendMessage(session, load);
        }catch (DataAccessException e) {
            sendError(session, new Error("Error: Not authorized"));
        } catch (ResponseException e) {
            sendError(session, new Error("Error: Not a valid game"));
        }
    }

    public void notifyAll(Session currentSession, ServerMessage message) throws IOException {
        notifyEveryone(currentSession, message, false);
    }

    public void notifyEveryone(Session currentSession, ServerMessage message, boolean local) throws IOException {
        System.out.printf("Broadcasting (toSelf: %s): %s%n", local, new Gson().toJson(message));
        for (Session session : Server.allSessions.keySet()) {
            boolean inGame = Server.allSessions.get(session) != 0;
            boolean inSameGame = Server.allSessions.get(session).equals(Server.allSessions.get(currentSession));
            boolean isLocal = session == currentSession;
            if ((local || !isLocal) && inGame && inSameGame) {
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
        else return null;
    }

    public void sendMessage(Session session, ServerMessage message) throws IOException, IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private boolean checkColor(String username, GameData game, ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE && username.equals(game.whiteUsername())) ||
                (color == ChessGame.TeamColor.BLACK && username.equals(game.blackUsername()));
    }

}
