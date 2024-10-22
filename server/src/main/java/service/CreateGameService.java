package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.ResponseException;
import model.GameData;
import java.util.Random;

import java.util.Vector;

public class CreateGameService {
    private MemoryGameDAO memoryGameDAO;
    private MemoryAuthDAO memoryAuthDAO;

    public CreateGameService(MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
    }

    public Object createGame(String gameName, String authToken) throws ResponseException, DataAccessException{
        if (gameName == null){
            throw new ResponseException(400, "Error: bad request");
        }
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        int randomInteger = (int)(Math.random() * 1000);
        GameData gameData = new GameData(randomInteger, "", "", gameName, new ChessGame());
        memoryGameDAO.addGame(gameData);
        return gameData.gameID();
        //fix this so it returns "gameID": 1234
    }
}
