package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Request;
import exception.ResponseException;
import model.GameData;
import model.AuthData;
import model.ListGameResponse;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public UserData registerUser(UserData userData) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, userData, UserData.class);
    }

    public UserData loginUser(UserData userData) throws ResponseException{
        var path = "/session";
        return this.makeRequest("POST", path, userData, UserData.class);
    }

    public void logoutUser() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public ArrayList<GameData> listGames() throws ResponseException {
        var path = "/game";
        record listPetResponse(ArrayList<GameData> games) {
        }
        var response = this.makeRequest("GET", path, null, listPetResponse.class);
        return response.games();
    }

    public GameData createGame(String gameName) throws ResponseException  {
        var path = "/game";
        return this.makeRequest("POST", path, gameName, GameData.class);
    }

    public GameData joinGame(int gameID) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, gameID, GameData.class);
    }

    public void clearAll() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
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

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
