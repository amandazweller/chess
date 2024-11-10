package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class PrintBoard {
    ChessBoard board;

    PrintBoard(ChessBoard board) {
        this.board = board;
    }

    public void printBoard() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_TEXT_BOLD);

        for (int i = 0; i < 2; i++){
            boolean reversed = (i == 0);

            stringBuilder.append(printLabels(reversed));

            for (int row = 8; row > 0; row--){
                int reversedRow = row;
                if (reversed){
                    reversedRow = 9 - row;
                }
                stringBuilder.append(printRow(reversedRow, reversed));
                stringBuilder.append("\n");
            }

            stringBuilder.append(printLabels(reversed));

            if (i == 0 ) {
                stringBuilder.append("\n");
            }
        }

        stringBuilder.append(RESET_TEXT_BOLD_FAINT);
        System.out.println(stringBuilder);

    }

    private String printLabels(boolean reversed){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_BG_COLOR_MAGENTA).append(SET_TEXT_COLOR_BLACK);

        if (reversed){
            stringBuilder.append("    h  g  f  e  d  c  b  a    ");
        }
        else {
            stringBuilder.append("    a  b  c  d  e  f  g  h    ");
        }
        stringBuilder.append(RESET_BG_COLOR).append(RESET_TEXT_COLOR).append("\n");
        return stringBuilder.toString();
    }

    private String printRow(int row, boolean reversed){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_BG_COLOR_BLACK).append(SET_BG_COLOR_MAGENTA);
        stringBuilder.append(" ").append(row).append(" ");

        for (int col = 1; col <= 8; col++){
            int reversedCol = col;
            if (reversed){
               reversedCol = 9 - col;
            }
            stringBuilder.append(printSquareColor(row, reversedCol));
            stringBuilder.append(printPiece(row, reversedCol));
        }

        stringBuilder.append(SET_BG_COLOR_BLACK).append(SET_BG_COLOR_MAGENTA);
        stringBuilder.append(" ").append(row).append(" ");
        return stringBuilder.toString();
    }

    private String printSquareColor(int row, int col){
        boolean isEvenRow = (row % 2 == 0);
        boolean isEvenCol = (col % 2 == 0);

        if ((isEvenRow && isEvenCol) || (!isEvenRow && !isEvenCol)) {
            return SET_BG_COLOR_BLACK;
        } else {
            return SET_BG_COLOR_WHITE;
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