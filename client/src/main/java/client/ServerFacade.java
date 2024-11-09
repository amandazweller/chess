package client;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {
    private String serverUrl = "http://localhost:8181";

    public ServerFacade(String url){
        serverUrl = url;
    }

    public boolean registerUser(UserData userData) throws ResponseException {
        var body = Map.of("username", userData.username(), "password", userData.password(), "email", userData.email());
        var jsonBody = new Gson().toJson(body);
        var path = "/user";
        var response = this.makeRequest("POST", path, jsonBody);
        if (response.contains("Error")){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean loginUser(UserData userData) throws ResponseException{
        var body = Map.of("username", userData.username(), "password", userData.password(), "email", userData.email());
        var jsonBody = new Gson().toJson(body);
        var path = "/session";
        var response = this.makeRequest("POST", path, jsonBody);
        if (response.contains("Error")){
            return false;
        }
        else {
            return true;
        }
    }

    public void logoutUser() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null);
    }

    public ArrayList<GameData> listGames() throws ResponseException {
        var path = "/game";
        var response = this.makeRequest("GET", path, null);
        GameData game = new Gson().fromJson(response, GameData.class);
        ArrayList<GameData> games = new ArrayList<>();
        games.add(game);
        return games;
    }

    public GameData createGame(String gameName) throws ResponseException  {
        var body = Map.of("gameName", gameName);
        var jsonBody = new Gson().toJson(body);
        var path = "/game";
        var response = this.makeRequest("POST", path, jsonBody);
        if (response.contains("Error"))
    }

    public GameData joinGame(GameData gameData) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, gameData);
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

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public void observeGame(GameData gameData) {

    }
}
