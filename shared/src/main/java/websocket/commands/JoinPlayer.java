package websocket.commands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    private final int gameID;
    private final ChessGame.TeamColor color;

    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor color) {
        super(CommandType.JOIN_PLAYER, authToken, gameID);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.color = color;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
