package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private final ChessMove move;

    public MakeMove(String authToken, int gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
    }

    public Integer getGameID() {
        return super.getGameID();
    }

    public ChessMove getMove() {
//        System.out.println(move.getStartPosition().getRow() + move.getStartPosition().getColumn());
//
//        System.out.println(move.getEndPosition().getRow() + move.getEndPosition().getColumn());

        return move;
    }
}
