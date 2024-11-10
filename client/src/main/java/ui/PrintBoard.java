package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class PrintBoard {
    ChessBoard board;

    PrintBoard(ChessBoard board) {
        this.board = board;
    }


    private static final int BOARD_SIZE = 8;
    private static final int SQAURE_SIZE = 8;
    private static final int LINE_WIDTH = 1;

    public void printBoard() {
        StringBuilder stringBuilder = new StringBuilder();
    }

    private String printRow(int row, boolean reversed){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_BG_COLOR_BLACK).append(SET_BG_COLOR_BLUE);
        stringBuilder.append(" ").append(row)
    }


    private String printSquareColor(int row, int col){
        boolean isEvenRow = (row % 2 == 0);
        boolean isEvenCol = (col % 2 == 0);

        if ((isEvenRow && isEvenCol) || (!isEvenRow && !isEvenCol)) {
            return SET_BG_COLOR_WHITE;
        } else {
            return SET_BG_COLOR_BLACK;
        }
    }

    private String printPiece(int row, int col){
        StringBuilder stringBuilder = new StringBuilder();
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);

        if (piece != null){
            switch(piece.getTeamColor()){
                case WHITE -> {
                    switch (piece.getPieceType()) {
                        case KING -> stringBuilder.append(WHITE_KING);
                        case ROOK -> stringBuilder.append(WHITE_ROOK);
                        case BISHOP -> stringBuilder.append(WHITE_BISHOP);
                        case QUEEN -> stringBuilder.append(WHITE_QUEEN);
                        case KNIGHT -> stringBuilder.append(WHITE_KNIGHT);
                        case PAWN -> stringBuilder.append(WHITE_PAWN);
                    }
                }
                case BLACK -> {
                    switch (piece.getPieceType()) {
                        case QUEEN -> stringBuilder.append(BLACK_QUEEN);
                        case KING -> stringBuilder.append(BLACK_KING);
                        case BISHOP -> stringBuilder.append(BLACK_BISHOP);
                        case ROOK -> stringBuilder.append(BLACK_ROOK);
                        case KNIGHT -> stringBuilder.append(BLACK_KNIGHT);
                        case PAWN -> stringBuilder.append(BLACK_PAWN);
                    }
                }
            }
        } else {
            stringBuilder.append(EMPTY);
        }
        return stringBuilder.toString();
    }

}
