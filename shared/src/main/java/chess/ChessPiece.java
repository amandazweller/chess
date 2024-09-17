package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece.PieceType pieceType = getPieceType();
        ChessPiece.PieceType promotionPiece = ChessMove.getPromotionPiece();

        boolean isWhite = ChessGame.TeamColor.WHITE.equals(pieceColor);

        Collection<ChessPosition> positions = ValidMoves.returnValid(board, pieceType, myPosition, isWhite);

        for (ChessPosition position : positions) {
            System.out.println(position.getRow() + " " + position.getColumn());
            ChessMove move = new ChessMove(myPosition, position, promotionPiece);
            moves.add(move);
            //ChessPiece targetPiece = board.getPiece(position);

//            if (targetPiece == null || targetPiece.getTeamColor() != pieceColor) {
//                ChessMove move = new ChessMove(myPosition, position, promotionPiece);
//                moves.add(move);
//            }

            //CODE CAPTURE OF OTHER PIECE
//            if (targetPiece != null) {
//                if (targetPiece.getTeamColor() != pieceColor) {
//                    ChessMove move = new ChessMove(myPosition, position, promotionPiece);
//                    moves.add(move);
//                }
//                break;
//            }

            if (pieceType == PieceType.PAWN && (position.getRow() == 1 || position.getRow() == 8)) {
                ChessMove promotionMove = new ChessMove(myPosition, position, ChessPiece.PieceType.QUEEN); // assuming queen promotion by default
                moves.add(promotionMove);
            }

        }
        return moves;
    }
}
