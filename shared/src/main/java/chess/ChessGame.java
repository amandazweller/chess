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
    private ChessPosition findKingPosition(ChessBoard board, TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor().equals(teamColor) && piece.getPieceType().equals(ChessPiece.PieceType.KING)) {
                    return pos;
                }
            }
        }
        return null;
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> valid = new ArrayList<>();
        Collection<ChessMove> allMoves = Board.getPiece(startPosition).pieceMoves(Board, startPosition);
        if (Board.getPiece(startPosition) == null) {
            return null;
        }
        for (ChessMove move : allMoves){
            //if (!isInCheckmate(Board.getPiece(startPosition).getTeamColor()) && !isInStalemate(Board.getPiece(startPosition).getTeamColor())){
                if (!willCheck(move)){
                    valid.add(move);
                    System.out.println(move.getEndPosition().getRow() + " " + move.getEndPosition().getColumn());
                }
            //}
        }
        return valid;
    }

//    public boolean canCaptureKing(ChessPosition kingPosition, ChessPiece.TeamColor enemyColor) {
//        for (int i = 0; i < )
//    }

    public Boolean willCheck(ChessMove move){
        ChessBoard newBoard = new ChessBoard();
        newBoard = Board;
        ChessPosition newPosition = new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn());
        if (newBoard.getPiece(newPosition) == null) {
            return false;
        }
        boolean willCheck = false;
        ChessPiece piece = new ChessPiece(newBoard.getPiece(newPosition).getTeamColor(), newBoard.getPiece(newPosition).getPieceType());
        newBoard.addPiece(move.getEndPosition(), piece);
        newBoard.grid[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = null;
        ChessPosition kingPosition = findKingPosition(newBoard, newBoard.getPiece(newPosition).getTeamColor());

        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (newBoard.getPiece(position) != null && newBoard.getPiece(position).getTeamColor() != newBoard.getPiece(newPosition).getTeamColor()){
                    Collection<ChessMove> moves = newBoard.getPiece(position).pieceMoves(newBoard, position);
                    for (ChessMove m : moves){
                        if (m.getEndPosition().equals(kingPosition)){
                            willCheck = true;
                            break;
                        }
                    }
                }
            }
        }
        return willCheck;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean invalidMove = true;
        Collection<ChessMove> allMoves = Board.getPiece(move.getStartPosition()).pieceMoves(Board, move.getStartPosition());
        if (!allMoves.isEmpty()){
            for (ChessMove m : allMoves){
                if (m.equals(move)){
                    invalidMove = false;
                    break;
                }
            }
        }

        if (invalidMove){
            throw new InvalidMoveException();
        }
        else {
            ChessPiece piece = Board.getPiece(move.getStartPosition());
            Board.addPiece(move.getEndPosition(), piece);
            Board.grid[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = null;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean isInCheck = false;
        ChessPosition kingPosition = findKingPosition(Board, teamColor);

        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (Board.getPiece(position)!= null && Board.getPiece(position).getTeamColor() != teamColor){
                    Collection<ChessMove> moves = validMoves(position);
                    for (ChessMove move : moves){
                        if (move.getEndPosition().equals(kingPosition)){
                            isInCheck = true;
                            break;
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
        if (!isInCheck(teamColor)) {
            return false;
        }
        Boolean isInCheckmate = true;
        ChessPosition kingPosition = findKingPosition(Board, teamColor);

        if (isInCheck(teamColor)){
            for (int i = 1; i < 9; i++){
                for (int j = 1; j < 9; j++){
                    ChessPosition newPosition = new ChessPosition(i, j);
                    if (Board.getPiece(newPosition)!= null && Board.getPiece(newPosition).getTeamColor().equals(teamColor)){
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
        if (isInCheck(teamColor)) {
            return false;
        }
        boolean isInStalemate = true;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition newPosition = new ChessPosition(i, j);
                if (Board.getPiece(newPosition)!= null && Board.getPiece(newPosition).getTeamColor().equals(teamColor)){
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
