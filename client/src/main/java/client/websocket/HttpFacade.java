package client.websocket;

import chess.ChessGame;
import chess.ChessPosition;
import client.ServerFacade;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.ListGameResponse;
import ui.PrintBoard;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;

public class HttpFacade {
    String serverURL;
    ServerFacade serverFacade;

    public HttpFacade(ServerFacade server, String url){
       serverFacade = server;
        serverURL = url;
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
        serverFacade.authToken = (String) responseMap.get("authToken");
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
        serverFacade.authToken = (String) responseMap.get("authToken");
        serverFacade.currentUsername = username;
        return !response.contains("Error");
    }

    public boolean logoutUser() throws ResponseException {
        var path = "/session";
        var response = this.makeRequest("DELETE", path, null);
        if (response.contains("Error")){
            return false;
        }
        serverFacade.authToken = null;
        serverFacade.currentUsername = null;
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
        serverFacade.teamColor = ChessGame.TeamColor.valueOf(playerColor);
        serverFacade.currentGameID = gameData.gameID();
        serverFacade.game = gameData.game();
        serverFacade.playing = true;
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
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (serverFacade.authToken != null) {
                http.addRequestProperty("authorization", serverFacade.authToken);
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

    public boolean observeGame(int gameID) {
        serverFacade.currentGameID = gameID;
        serverFacade.teamColor = null;
        return true;
    }

    public void printBoard(ChessPosition highlighted) throws ResponseException {
        new PrintBoard(serverFacade.game).printBoard(serverFacade.teamColor, highlighted);
    }

}

