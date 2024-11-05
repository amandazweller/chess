package ui;

import java.util.Arrays;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import exception.ResponseException;
import server.ServerFacade;


public class PreLoginClient {
    private String username = null;
    private String password = null;
    private String email = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;



    public PreLoginClient(String serverUrl, ReplPreLogin replPreLogin) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> loginUser(params);
                case "register" -> registerUser(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String loginUser(String... params) throws ResponseException {
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

    public String registerUser(String... params) throws ResponseException {
        if (params.length >= 3) {
            state = State.LOGGEDIN;
            username = String.join("-", params);
            password = String.join("-", params);
            email = String.join("-", params);
            UserData userData = new UserData(username, password, email);
            server.registerUser(userData);
            return String.format("You registered as %s.", username);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String help() {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - login <USERNAME> <PASSWORD>
                    - quit
                    - help
                    """;
    }
}
