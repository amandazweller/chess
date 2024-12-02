package ui;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class PrintBoard {
    ChessGame game;

    PrintBoard(ChessGame game) {
        this.game = game;
    }

    public void printBoard(ChessGame.TeamColor teamColor, ChessPosition highlighted) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_TEXT_BOLD);

        Collection<ChessPosition> endPositions = new ArrayList<>();
        if (highlighted != null){
            Collection<ChessMove> validMoves = game.validMoves(highlighted);
            if (!validMoves.isEmpty()){
                for (ChessMove validMove : validMoves){
                    endPositions.add(validMove.getEndPosition());
                }
            }
        }
        boolean reversed = false;
        if (teamColor == ChessGame.TeamColor.BLACK){
            reversed = true;
        }
        for (int i = 0; i < 2; i++){

            stringBuilder.append(printLabels(reversed));

            for (int row = 8; row > 0; row--){
                int reversedRow = row;
                if (reversed){
                    reversedRow = 9 - row;
                }
                stringBuilder.append(printRow(reversedRow, reversed, highlighted, endPositions));
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
        stringBuilder.append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String printRow(int row, boolean reversed, ChessPosition start, Collection<ChessPosition> highlighted){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_TEXT_COLOR_BLACK).append(SET_BG_COLOR_MAGENTA);
        stringBuilder.append(" ").append(row).append(" ");

        for (int col = 1; col <= 8; col++){
            int reversedCol = col;
            if (reversed){
               reversedCol = 9 - col;
            }
            stringBuilder.append(printSquareColor(row, reversedCol, start, highlighted));
            stringBuilder.append(printPiece(row, reversedCol));
        }

        stringBuilder.append(SET_TEXT_COLOR_BLACK).append(SET_BG_COLOR_MAGENTA);
        stringBuilder.append(" ").append(row).append(" ");
        stringBuilder.append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);
        return stringBuilder.toString();
    }

    private String printSquareColor(int row, int col, ChessPosition start, Collection<ChessPosition> highlighted){
        ChessPosition position = new ChessPosition(row, col);
        if (start.equals(position)){
            return SET_BG_COLOR_MAGENTA;
        }
        boolean isEvenRow = (row % 2 == 0);
        boolean isEvenCol = (col % 2 == 0);

        if ((isEvenRow && isEvenCol) || (!isEvenRow && !isEvenCol)) {
            if (highlighted.contains(position)){
                return SET_BG_COLOR_DARK_GREEN;
            }
            return SET_BG_COLOR_BLACK;
        } else {
            if (highlighted.contains(position)){
                return SET_BG_COLOR_GREEN;
            }
            return SET_BG_COLOR_WHITE;
        }
    }

    private String printPiece(int row, int col){
        StringBuilder stringBuilder = new StringBuilder();
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = game.getBoard().getPiece(position);
        stringBuilder.append(SET_TEXT_COLOR_BLUE);

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
