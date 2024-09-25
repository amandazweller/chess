package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard Board = new ChessBoard();

    public ChessGame() {
        this.teamTurn = teamTurn;
        this.Board = Board;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> allMoves = ChessPiece.pieceMoves(getBoard(), startPosition);
        for (ChessMove move : allMoves){
            if (!isInCheckmate(ChessBoard.getPiece(startPosition).getTeamColor) && !isInStalemate(ChessBoard.getPiece(startPosition).getTeamColor)){
                if (!willCheck(move)){
                    validMoves.add(move);
                }
            }
        }
        return allMoves;
    }

    public Boolean willCheck(ChessMove move){
        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard board = getBoard();
        board[move.getEndPosition().getRow()][move.getEndPosition().getColumn()] = ChessPiece.getPieceType(move.getStartPosition());
        board[move.getStartPosition().getRow()][move.getStartPosition().getColumn()] = null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = new ChessPosition(-1,-1);
        Boolean isInCheck = false;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                ChessPosition newPosition = new ChessPosition(i, j);
                if (ChessBoard.getPiece(newPosition).getTeamColor() == teamColor && ChessBoard.getPiece(newPosition).getPieceType() == ChessPiece.PieceType.KING){
                    kingPosition = newPosition;
                }
            }
        }

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (ChessBoard.getPiece(position).getTeamColor() != teamColor){
                    Collection<ChessMove> moves = validMoves(position);
                    for (ChessMove move : moves){
                        if (move.getEndPosition() == kingPosition){
                            isInCheck = true;
                        }
                    }
                }

            }
        }
        return isInCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = new ChessPosition(-1,-1);
        Boolean isInCheckmate = true;

        if (isInCheck(teamColor)){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    ChessPosition newPosition = new ChessPosition(i, j);
                    if (ChessBoard.getPiece(newPosition).getTeamColor() == teamColor){
                        if (!validMoves(newPosition).isEmpty()){
                            isInCheckmate = false;
                            //doesn't work bc still need to see if another piece can protect the king not just if they still have moves
                            //actually might work if valid moves checks if in check;
                        }
                    }
                }
            }
        }
        return isInCheckmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean isInStalemate = true;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                ChessPosition newPosition = new ChessPosition(i, j);
                if (ChessBoard.getPiece(newPosition).getTeamColor() == teamColor){
                    if (!validMoves(newPosition).isEmpty()){
                        isInStalemate = false;
                        //doesn't work bc still need to see if another piece can protect the king not just if they still have moves
                        //actually might work if valid moves checks if in check;
                    }
                }
            }
        }
        return isInStalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        Board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return Board;
    }
}
