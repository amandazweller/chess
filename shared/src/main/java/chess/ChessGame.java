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
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        initalizeBoard();
        setTeamTurn(TeamColor.WHITE);
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

    public void initalizeBoard(){
        board.grid = new ChessPiece[8][8];

        board.grid[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board.grid[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board.grid[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board.grid[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board.grid[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board.grid[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board.grid[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board.grid[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        for (int i = 0; i < 8; i++) {
            board.grid[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        board.grid[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board.grid[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board.grid[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board.grid[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board.grid[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board.grid[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board.grid[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board.grid[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        for (int i = 0; i < 8; i++) {
            board.grid[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
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
        Collection<ChessMove> allMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        if (board.getPiece(startPosition) == null) {
            return null;
        }
        for (ChessMove move : allMoves){
                if (!willCheck(move)){
                    valid.add(move);
                    System.out.println(move.getEndPosition().getRow() + " "
                            + move.getEndPosition().getColumn());
                }
        }
        return valid;
    }

    public Boolean willCheck(ChessMove move){
        ChessBoard newBoard = new ChessBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition pos = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null) {
                    newBoard.addPiece(pos, new ChessPiece(piece.getTeamColor(),
                            piece.getPieceType()));
                }
            }
        }
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition newPosition = new ChessPosition(move.getEndPosition().getRow(),
                move.getEndPosition().getColumn());
        ChessPiece piece = new ChessPiece(newBoard.getPiece(startPosition).getTeamColor(),
                newBoard.getPiece(startPosition).getPieceType());
        newBoard.addPiece(move.getEndPosition(), piece);
        newBoard.grid[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = null;
        boolean willCheck = false;
        ChessPosition kingPosition = findKingPosition(newBoard, newBoard.getPiece(newPosition).getTeamColor());

        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (newBoard.getPiece(position) != null &&
                        newBoard.getPiece(position).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
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
        if (board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException("No piece at start");
        }
        TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
        if (color != teamTurn){
            throw new InvalidMoveException("Not right team");
        }
        Collection<ChessMove> allMoves = validMoves(move.getStartPosition());
        if (!allMoves.isEmpty()){
            for (ChessMove m : allMoves){
                if (m.equals(move)){
                    invalidMove = false;
                    break;
                }
            }
        }
        if (invalidMove){
            throw new InvalidMoveException("Not valid move");
        }
        else {
            ChessPiece piece = board.getPiece(move.getStartPosition());
            if (move.getPromotionPiece() == null){
                board.addPiece(move.getEndPosition(), piece);
            }
            else {
                ChessPiece promotion = new ChessPiece( piece.getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), promotion);
            }

            board.grid[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = null;
            if (color.equals(TeamColor.WHITE)){
                setTeamTurn(TeamColor.BLACK);
            }
            else {
                setTeamTurn(TeamColor.WHITE);
            }
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
        ChessPosition kingPosition = findKingPosition(board, teamColor);

        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position)!= null && board.getPiece(position).getTeamColor() != teamColor){
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
        boolean isInCheckmate = true;
        ChessPosition kingPosition = findKingPosition(board, teamColor);

        if (isInCheck(teamColor)){
            for (int i = 1; i < 9; i++){
                for (int j = 1; j < 9; j++){
                    ChessPosition newPosition = new ChessPosition(i, j);
                    if (board.getPiece(newPosition)!= null && board.getPiece(newPosition).getTeamColor().equals(teamColor)){
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
        boolean allNull = true;
        if (isInCheck(teamColor)) {
            return false;
        }
        boolean isInStalemate = true;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition newPosition = new ChessPosition(i, j);
                if (board.getPiece(newPosition)!= null && board.getPiece(newPosition).getTeamColor().equals(teamColor)){
                    allNull = false;
                    if (!validMoves(newPosition).isEmpty()){
                        isInStalemate = false;
                        //doesn't work bc still need to see if another piece can protect the king not just if they still have moves
                        //actually might work if valid moves checks if in check;
                    }
                }
            }
        }
        if (allNull){
            isInStalemate = false;
        }
        return isInStalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
