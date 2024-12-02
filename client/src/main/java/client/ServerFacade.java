package client;

import chess.ChessGame;
import chess.ChessPosition;
import client.websocket.HttpFacade;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.ListGameResponse;
import ui.PrintBoard;

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
    HttpFacade httpFacade;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public ServerFacade(){
        this("http://localhost:8181");
        httpFacade = new HttpFacade(this, serverUrl);
    }



    public boolean registerUser(String username, String password, String email) throws ResponseException {
        return httpFacade.registerUser(username, password, email);
    }

    public boolean loginUser(String username, String password) throws ResponseException{
        return httpFacade.loginUser(username, password);
    }

    public boolean logoutUser() throws ResponseException {
        return httpFacade.logoutUser();
    }

    public ArrayList<GameData> listGames() throws ResponseException {
        return httpFacade.listGames();
    }

    public boolean createGame(String gameName) throws ResponseException  {
        return httpFacade.createGame(gameName);
    }

    public boolean joinGame(GameData gameData, String playerColor) throws ResponseException {
        return httpFacade.joinGame(gameData, playerColor);
    }

    public void clearAll() throws ResponseException {
        httpFacade.clearAll();
    }

    public boolean observeGame(int gameID) {
        return httpFacade.observeGame(gameID);
    }

    public void printBoard(ChessPosition highlighted) throws ResponseException {
        new PrintBoard(game).printBoard(teamColor, highlighted);
    }

}
