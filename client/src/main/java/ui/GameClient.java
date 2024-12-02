package ui;

import java.util.Arrays;
import java.util.Map;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.InvalidMoveException;
import exception.ResponseException;
import client.ServerFacade;


public class GameClient {
    ServerFacade serverFacade;

    public GameClient(ServerFacade server, ReplGame replGame) {
        serverFacade = server;

    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redrawBoard();
                case "move" -> makeMove(params);
                case "highlight" -> highlightMoves(params);
                case "resign" -> resign();
                case "leave" -> "leave";
                default -> help();
            };
        } catch (ResponseException | InvalidMoveException ex) {
            return ex.getMessage();
        }
    }

    private String highlightMoves(String... params) throws ResponseException {
        if (params.length >= 1){
            ChessPosition start = new ChessPosition(Character.getNumericValue(params[0].charAt(1)), toColumn.get(params[0].charAt(0)));
            if (serverFacade.game.getBoard().getPiece(start) == null){
                return "No piece found at selected position. Try another position.";
            }
            serverFacade.printBoard(start);
            return "Valid moves highlighted in green.";
        }
        throw new ResponseException(400, "Expected: <POSITION>");
    }

    private static final Map<Character, Integer> toColumn = Map.of(
            'a', 1, 'b', 2, 'c', 3, 'd', 4, 'e', 5, 'f', 6, 'g', 7, 'h', 8
    );

    private String makeMove(String... params) throws InvalidMoveException, ResponseException {
        if (params.length >= 2) {
            if (!serverFacade.playing){
                return "You are not currently playing. Unable to make move.";
            }
            if (serverFacade.game.getTeamTurn() != serverFacade.teamColor){
                return "Other team's turn. Please wait to make move.";
            }

            int startCol = toColumn.get(params[0].charAt(1));
            int startRow = Character.getNumericValue(params[0].charAt(1));
            ChessPosition start = new ChessPosition(startRow, startCol);
            int endCol = toColumn.get(params[1].charAt(1));
            int endRow = Character.getNumericValue(params[1].charAt(1));
            ChessPosition end = new ChessPosition(endRow, endCol);

            ChessPiece.PieceType promotionPiece = null;
            if (params.length >= 3) {
                promotionPiece = getPieceType(params[2]);
            }
            ChessMove move = new ChessMove(start, end, promotionPiece);
            serverFacade.game.makeMove(move);
            redrawBoard();
            return "Move made successfully";
        }
            throw new ResponseException(400, "Expected: <FROM> <TO> <PROMOTION-PIECE> (promotion piece only applicable with promotion of pawn)");
    }

    public ChessPiece.PieceType getPieceType(String name) {
        return switch (name.toUpperCase()) {
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "PAWN" -> ChessPiece.PieceType.PAWN;
            default -> null;
        };
    }

    private String redrawBoard() throws ResponseException {
        serverFacade.printBoard(null);
        return " ";
    }

    private String resign() {
        return String.format("%s resigned successfully. Game over.", serverFacade.currentUsername);
    }

    public String help() {
        return """
                    move <FROM> <TO> <PROMOTION-PIECE> - make a move (promotion piece only used when the move will result in promotion of pawn)
                    highlight <POSITION> - highlight all legal moves for this piece
                    redraw - redraw board
                    leave - leave game
                    resign - forfeit game
                    help - with possible commands
                    """;
    }
    }