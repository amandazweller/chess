package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.WebSocketFacade;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.ListGameResponse;
import ui.PrintBoard;
import websocket.commands.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    public String authToken;
    public ChessGame.TeamColor teamColor = null;
    public int currentGameID;
    public ChessGame game;
    public String currentUsername;
    public boolean playing = false;

    WebSocketFacade webSocketFacade;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public boolean registerUser(String username, String password, String email) throws ResponseException {
        var body = Map.of("username", username, "password", password, "email", email);
        var jsonBody = new Gson().toJson(body);
        var path = "/user";
        var response = this.makeRequest("POST", path, jsonBody);
        if (response.contains("Error")){
            return false;
        }
        Map responseMap = new Gson().fromJson(response, Map.class);
        authToken = (String) responseMap.get("authToken");
        return !response.contains("Error");
    }

    public boolean loginUser(String username, String password) throws ResponseException{
        var body = Map.of("username", username, "password", password);
        var jsonBody = new Gson().toJson(body);
        var path = "/session";
        var response = this.makeRequest("POST", path, jsonBody);
        if (response.contains("Error")){
            return false;
        }
        Map responseMap = new Gson().fromJson(response, Map.class);
        authToken = (String) responseMap.get("authToken");
        currentUsername = username;
        return !response.contains("Error");
    }

    public boolean logoutUser() throws ResponseException {
        var path = "/session";
        var response = this.makeRequest("DELETE", path, null);
        if (response.contains("Error")){
            return false;
        }
        authToken = null;
        currentUsername = null;
        return !response.contains("Error");    }

    public ArrayList<GameData> listGames() throws ResponseException {
        var path = "/game";
        var response = this.makeRequest("GET", path, null);
        if (response.contains("Error")) {
            return new ArrayList<>();
        }
        ListGameResponse games = new Gson().fromJson(response, ListGameResponse.class);
        return games.games();
    }

    public boolean createGame(String gameName) throws ResponseException  {
        var body = Map.of("gameName", gameName);
        var jsonBody = new Gson().toJson(body);
        var path = "/game";
        var response = makeRequest("POST", path, jsonBody);
        return !response.contains("Error");
    }

    public boolean joinGame(GameData gameData, String playerColor) throws ResponseException {
        var body = Map.of("gameID", gameData.gameID(), "playerColor", playerColor);
        teamColor = ChessGame.TeamColor.valueOf(playerColor);
        currentGameID = gameData.gameID();
        game = gameData.game();
        playing = true;
        var jsonBody = new Gson().toJson(body);
        var path = "/game";
        var response = this.makeRequest("PUT", path, jsonBody);
        return !response.contains("Error");
    }

    public void clearAll() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null);
    }

    private String makeRequest(String method, String path, String request) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            if (request != null) {
                http.setDoOutput(true);
                http.setRequestProperty("Content-Type", "application/json");
                try (OutputStream os = http.getOutputStream()) {
                    os.write(request.getBytes());
                    os.flush();
                }
            }

            http.connect();
            int responseCode = http.getResponseCode();
            if (!isSuccessful(responseCode)) {
                return "Error: 401";
            }
            return readBody(http);

        } catch (IOException e) {
            throw new ResponseException(500, "Connection error: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String readBody(HttpURLConnection http) throws IOException {
        try (InputStream inputStream = http.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public boolean observeGame(GameData gameData) {
        currentGameID = gameData.gameID();
        game = gameData.game();
        teamColor = null;
        return true;
    }

    public void printBoard(ChessPosition highlighted) {
        new PrintBoard(game).printBoard(teamColor, highlighted);
    }

    public void connect() {
        try {
            webSocketFacade = new WebSocketFacade(serverUrl, teamColor);
        }
        catch (Exception e) {
            System.out.println("Failed to make connection with server");
        }
    }

    public void close() {
        try {
            webSocketFacade.session.close();
            webSocketFacade= null;
        }
        catch (IOException e) {
            System.out.println("Failed to close connection with server");
        }
    }

    public void sendCommand(UserGameCommand command) throws ResponseException {
        String message = new Gson().toJson(command);
        webSocketFacade.sendMessage(message);
    }

    public void connectWS(int gameID, ChessGame.TeamColor color) throws ResponseException {
        sendCommand(new Connect(authToken, gameID, color));
    }

    public void makeMove(int gameID, ChessMove move) throws ResponseException {
        sendCommand(new MakeMove(authToken, gameID, move));
    }

    public void leave(int gameID) throws ResponseException {
        sendCommand(new Leave(authToken, gameID));
    }

    public void resign(int gameID) throws ResponseException {
        sendCommand(new Resign(authToken, gameID));
    }
}
