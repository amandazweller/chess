package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private final int gameID;
    private final ChessMove move;

    public MakeMove(String authToken, int gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
