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
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE|BLACK] - a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands
                    """;
    }
    }