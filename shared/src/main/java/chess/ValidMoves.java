package chess;
import java.util.ArrayList;
import java.util.Collection;

public class ValidMoves {

    public ValidMoves() {}
    public static Collection<ChessPosition> KingMoves(ChessPosition position){
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        if (row >= 2){
            ChessPosition newPosition = new ChessPosition(row - 1, column);
            valid.add(newPosition);
            ChessPosition newPosition3 = new ChessPosition(row - 1, column + 1);
            valid.add(newPosition3);
            if (column >= 2){
                ChessPosition newPosition2 = new ChessPosition(row - 1, column - 1);
                valid.add(newPosition2);
            }
        }
        if (column >= 2){
            ChessPosition newPosition = new ChessPosition(row, column - 1);
            valid.add(newPosition);
        }
        if (row < 8){
            ChessPosition newPosition = new ChessPosition(row + 1, column);
            valid.add(newPosition);
            ChessPosition newPosition3 = new ChessPosition(row + 1, column - 1);
            valid.add(newPosition3);
            if (column < 8){
                ChessPosition newPosition2 = new ChessPosition(row + 1, column + 1);
                valid.add(newPosition2);
            }
        }
        if (column < 8){
            ChessPosition newPosition = new ChessPosition(row, column + 1);
            valid.add(newPosition);
        }
        return valid;
    }

    public static Collection<ChessPosition> QueenMoves(ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        Collection<ChessPosition> queenMoves = new ArrayList<>();
        valid = BishopMoves(position);
        queenMoves = RookMoves(position);
        valid.addAll(queenMoves);
        return valid;
    }
    public static Collection<ChessPosition> BishopMoves(ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        while (row < 8 && column < 8){
            ChessPosition newPosition = new ChessPosition(row + 1, column + 1);
            valid.add(newPosition);
            row += 1;
            column += 1;
        }
        row = position.getRow();
        column = position.getColumn();
        while (row >= 2 && column < 8){
            ChessPosition newPosition = new ChessPosition(row - 1, column + 1);
            valid.add(newPosition);
            row -= 1;
            column += 1;
        }
        row = position.getRow();
        column = position.getColumn();
        while (row >= 2 && column >= 2){
            ChessPosition newPosition = new ChessPosition(row - 1, column - 1);
            valid.add(newPosition);
            row -= 1;
            column -= 1;
        }
        row = position.getRow();
        column = position.getColumn();
        while (row < 8 && column >= 2){
            ChessPosition newPosition = new ChessPosition(row + 1, column - 1);
            valid.add(newPosition);
            row += 1;
            column -= 1;
        }

        return valid;
    }
    public static Collection<ChessPosition> KnightMoves(ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();

        if (row >= 2){
            if(column >= 3){
                ChessPosition newPosition = new ChessPosition(row - 2, column - 1);
                valid.add(newPosition);
            }
            if (column < 8) {
                ChessPosition newPosition = new ChessPosition(row - 2, column + 1);
                valid.add(newPosition);
            }
        }
        if (row >= 2){
            if(column >= 3){
                ChessPosition newPosition = new ChessPosition(row - 1, column - 2);
                valid.add(newPosition);
            }
            if (column < 8) {
                ChessPosition newPosition = new ChessPosition(row - 1, column + 2);
                valid.add(newPosition);
            }
        }
        if (row < 8){
            if (column >= 3){
                ChessPosition newPosition = new ChessPosition(row + 1, column - 2);
                valid.add(newPosition);
            }
            if (column < 7) {
                ChessPosition newPosition = new ChessPosition(row + 1, column + 2);
                valid.add(newPosition);
            }
        }
        if (row < 7){
            if(column >= 2){
                ChessPosition newPosition = new ChessPosition(row + 2, column - 1);
                valid.add(newPosition);
            }
            if (column < 8) {
                ChessPosition newPosition = new ChessPosition(row + 2, column + 1);
                valid.add(newPosition);
            }
        }
        return valid;
    }
    public static Collection<ChessPosition> PawnMoves(ChessPosition position, boolean isWhite) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        if (isWhite){
            if (row < 8) {
                valid.add(new ChessPosition(row + 1, column));
                if (row == 2) {
                    valid.add(new ChessPosition(row + 2, column));
                }
            }
        }
        else {
            if (row > 1) {
                valid.add(new ChessPosition(row - 1, column));
                if (row == 7) {
                    valid.add(new ChessPosition(row - 2, column));
                }
            }
        }
        return valid;
    }
    public static Collection<ChessPosition> RookMoves(ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        for (int i = row + 1; i < 9; i++){
            ChessPosition newPosition = new ChessPosition(i, column);
            valid.add(newPosition);
        }
        for (int i = row - 1; i > 0; i--){
            ChessPosition newPosition = new ChessPosition(i, column);
            valid.add(newPosition);
        }
        for (int i = column - 1; i > 0; i--){
            ChessPosition newPosition = new ChessPosition(row, i);
            valid.add(newPosition);
        }
        for (int i = column + 1; i < 9; i++){
            ChessPosition newPosition = new ChessPosition(row, i);
            valid.add(newPosition);
        }
        return valid;
    }

    public static Collection<ChessPosition> returnValid(ChessPiece.PieceType pieceType, ChessPosition position, boolean isWhite) {
        Collection<ChessPosition> positions = new ArrayList<>();
        if (pieceType == ChessPiece.PieceType.KING) {
            positions = KingMoves(position);
        }
        else if (pieceType == ChessPiece.PieceType.QUEEN) {
            positions = QueenMoves(position);
        }
        else if (pieceType == ChessPiece.PieceType.BISHOP) {
            positions = BishopMoves(position);
        }
        else if (pieceType == ChessPiece.PieceType.KNIGHT) {
            positions = KnightMoves(position);
        }
        else if (pieceType == ChessPiece.PieceType.PAWN) {
            positions = PawnMoves(position, isWhite);
        }
        else if (pieceType == ChessPiece.PieceType.ROOK) {
            positions = RookMoves(position);
        }
        return positions;
    }


}
