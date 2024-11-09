package ui;

import java.util.Arrays;

import model.GameData;
import exception.ResponseException;
import client.ServerFacade;


public class PostLoginClient {
    private String username = null;
    private String password = null;
    private String email = null;
    private String gameName = null;
    private final ServerFacade server;
    private State state = State.LOGGEDOUT;


    public PostLoginClient(String serverUrl, ReplPostLogin replPostLogin) {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logoutUser();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            state = State.LOGGEDIN;
            gameName = params[0];
            boolean result = server.createGame(gameName);
            if (result){
                return String.format("Game %s created successfully.", gameName);
            }
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.LOGGEDIN;
            int id = Integer.parseInt(params[0]);
            String playerColor = params[1];
            boolean result = server.joinGame(id, playerColor);
            if (result){
                return String.format("Game %s joined successfully.", id);
            }
        }
        throw new ResponseException(400, "Expected: <ID> <WHITE|BLACK>");
    }

    private GameData getGame(int id) throws ResponseException {
        for (var game : server.listGames()) {
            if (game.gameID() == id) {
                return game;
            }
        }
        return null;
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            state = State.LOGGEDIN;
            int id = Integer.parseInt(params[0]);
            GameData gameData = getGame(id);
            server.observeGame(gameData);
            return String.format("Successfully observing game with id: %s.", id);
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    public String logoutUser() throws ResponseException {
        assertSignedIn();
        server.logoutUser();
        state = State.LOGGEDOUT;
        return String.format("%s has logged out", username);
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames();
        for (int i = 0; i < games.size(); i++) {
            GameData game = games.get(i);
            String whiteUser = game.whiteUsername() != null ? game.whiteUsername() : "no player found";
            String blackUser = game.blackUsername() != null ? game.blackUsername() : "no player found";
            return String.format("%d -- Game Name: %s  |  White User: %s  |  Black User: %s %n", i, game.gameName(), whiteUser, blackUser);
        }
        return null;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
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
