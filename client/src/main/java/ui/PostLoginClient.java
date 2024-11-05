package ui;

import java.util.Arrays;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import exception.ResponseException;
import server.ServerFacade;


public class PostLoginClient {
    private String username = null;
    private String password = null;
    private String email = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;


    public PostLoginClient(String serverUrl, ReplPreLogin replPreLogin) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
        if (params.length >= 2) {
            state = State.LOGGEDIN;
            username = String.join("-", params);
            password = String.join("-", params);
            UserData userData = new UserData(username, password, email);
            server.loginUser(userData);
            return String.format("You logged in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.LOGGEDIN;
            username = String.join("-", params);
            password = String.join("-", params);
            UserData userData = new UserData(username, password, email);
            server.loginUser(userData);
            return String.format("You logged in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.LOGGEDIN;
            username = String.join("-", params);
            password = String.join("-", params);
            UserData userData = new UserData(username, password, email);
            server.loginUser(userData);
            return String.format("You logged in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String logoutUser() throws ResponseException {
        assertSignedIn();
        server.logoutUser();
        state = State.LOGGEDOUT;
        return String.format("%s left the shop", username);
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    public String help() {
        return """
                    - create <NAME>
                    - list
                    - join <ID> [WHITE|BLACK]
                    - observe <ID>
                    - logout
                    - quit
                    - help
                    """;
    }


}
