package ui;

import java.util.ArrayList;
import java.util.Arrays;

import chess.ChessGame;
import model.GameData;
import exception.ResponseException;
import client.ServerFacade;


public class PostLoginClient {
    ServerFacade serverFacade;



    public PostLoginClient(ServerFacade server, ReplPostLogin replPostLogin) {
        serverFacade = server;
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
            String gameName = params[0];
            boolean result = serverFacade.createGame(gameName);
            if (result){
                return String.format("Game %s created successfully.", gameName);
            }
            else {
                return "did not work. try again";
            }
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            int id;
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                throw new ResponseException(400, "Please enter valid ID");
            }
            ArrayList<GameData> games = serverFacade.listGames();
            if (id > games.size()){
                throw new ResponseException(400, "Please enter valid ID");
            }
            GameData gameData = games.get(id - 1);
            String playerColor = params[1].toUpperCase();
            boolean result = serverFacade.joinGame(gameData, playerColor);
            if (result){
                serverFacade.connect();
                serverFacade.connectWS(gameData.gameID(), ChessGame.TeamColor.valueOf(playerColor));
                return String.format("Game %s joined successfully. \n", id);
            }
            else {
                return "Game does not exist or color already taken. Please try again.";
            }
        }
        throw new ResponseException(400, "Expected: <ID> <WHITE|BLACK>");
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            int parsedInt;
            try {
                parsedInt = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                throw new ResponseException(400, "Please enter valid ID");
            }
            ArrayList<GameData> games = serverFacade.listGames();
            if (parsedInt > games.size()){
                throw new ResponseException(400, "Please enter valid ID");
            }
            GameData gameData = games.get(parsedInt - 1);
            boolean result = serverFacade.observeGame(gameData);
            if (result){
                serverFacade.connect();
                serverFacade.connectWS(gameData.gameID(), null);
                return String.format("Successfully joined game with id: %s. You are now observing. \n", parsedInt);
            }
            else {
                return "Game does not exist. Please try again.";
            }
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    public String logoutUser() throws ResponseException {
        serverFacade.logoutUser();
        return "You are now logged out";
    }

    public String listGames() throws ResponseException {
        ArrayList<GameData> games = serverFacade.listGames();
        StringBuilder result = new StringBuilder();
        int i = 1;
        for (GameData game : games) {
            String whiteUser = game.whiteUsername() != null ? game.whiteUsername() : "no player found";
            String blackUser = game.blackUsername() != null ? game.blackUsername() : "no player found";
            result.append(String.format("%d -- Game Name: %s  |  White User: %s  |  Black User: %s %n", i, game.gameName(), whiteUser, blackUser));
            i++;
        }
        if (games.isEmpty()){
            return "No games found";
        }
        return result.toString();
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
