package ui;

import java.util.Arrays;
import exception.ResponseException;
import client.ServerFacade;


public class PreLoginClient {
    private String username = null;
    private String password = null;
    ServerFacade serverFacade;


    public PreLoginClient(ServerFacade server, ReplPreLogin replPreLogin) {
        serverFacade = server;
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
            username = params[0];
            password = params[1];
            boolean result = serverFacade.loginUser(username, password);
            if (result){
                return String.format("You are now logged in as %s.", username);
            }
            else {
                return "Incorrect username or password. Please try again.";
            }
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String registerUser(String... params) throws ResponseException {
        if (params.length >= 3) {
            username = params[0];
            password = params[1];
            String email = params[2];
            boolean result = serverFacade.registerUser(username, password, email);
            if (result){
                return String.format("You registered as %s.", username);
            }
            else {
                return "Username already taken";
            }
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String help() {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
    }
}
