package chess;
import java.util.ArrayList;
import java.util.Collection;

public class ValidMoves {

    public Collection<ChessPosition> KingMoves(ChessPosition position){
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        if (row >= 1){
            ChessPosition newPosition = new ChessPosition(row - 1, column);
            valid.add(newPosition);
        }
        if (column >= 1){
            ChessPosition newPosition = new ChessPosition(row, column - 1);
            valid.add(newPosition);
        }
        if (row < 8){
            ChessPosition newPosition = new ChessPosition(row + 1, column);
            valid.add(newPosition);
        }
        if (column < 8){
            ChessPosition newPosition = new ChessPosition(row, column + 1);
            valid.add(newPosition);
        }
        return valid;
    }

    public Collection<ChessPosition> QueenMoves(ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
    }
    public Collection<ChessPosition> BishopMoves(ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
    }
    public Collection<ChessPosition> KnightMoves(ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
    }
    public Collection<ChessPosition> PawnMoves(ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
    }
    public Collection<ChessPosition> RookMoves(ChessPosition position) {
        Collection<ChessPosition> valid = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
    }

    public Collection<ChessPosition> ValidMoves(ChessPiece.PieceType pieceType, ChessPosition position) {
        if (pieceType == ChessPiece.PieceType.KING) {
            Collection<ChessPosition> positions = KingMoves(position);
        }
        else if (pieceType == ChessPiece.PieceType.QUEEN) {

        }
        else if (pieceType == ChessPiece.PieceType.BISHOP) {

        }
        else if (pieceType == ChessPiece.PieceType.KNIGHT) {

        }
        else if (pieceType == ChessPiece.PieceType.PAWN) {

        }
        else if (pieceType == ChessPiece.PieceType.ROOK) {

        }
    }


}
