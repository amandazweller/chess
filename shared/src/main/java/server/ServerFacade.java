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
        return null;
    }

    private UserData loginUser(UserData userdata) throws ResponseException{
        return null;
    }

    private void logoutUser(UserData userData) throws ResponseException {

    }

    private ArrayList<GameData> listGames() throws ResponseException {
        return null;
    }

    private GameData createGame(GameData gameData) throws ResponseException  {
        return null;
    }

    private GameData joinGame(int gameID) throws ResponseException {
        return null;
    }

    private void clearAll() throws ResponseException {

    }
}
