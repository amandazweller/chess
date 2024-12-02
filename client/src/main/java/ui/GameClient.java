package ui;

import java.util.ArrayList;
import java.util.Arrays;

import model.GameData;
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
                case "highlightMoves" -> highlightMoves();
                case "resign" -> resign();

                case "leave" -> "leave";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String highlightMoves() {
        return null;
    }

    private String makeMove(String[] params) {
        return null;
    }

    private String redrawBoard() {
        return null;
    }

    private String resign() {
        return null;
    }

    public String help() {
        return """
                    move <FROM> <TO> <PROMOTION-PIECE> - make a move (promotion piece only used when the move will result in promotion of pawn)
                    highlightMoves <position> - highlight all legal moves for this piece
                    redraw - redraw board
                    leave - leave game
                    resign - forfeit game
                    quit - playing chess
                    help - with possible commands
                    """;
    }
    }