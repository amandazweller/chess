package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.ResponseException;
import model.GameData;

public class CreateGameService {
    private final MemoryGameDAO memoryGameDAO;
    private final MemoryAuthDAO memoryAuthDAO;

    public CreateGameService(MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
    }

    public GameData createGame(String gameName, String authToken) throws ResponseException {
        if (gameName == null){
            throw new ResponseException(400, "Error: bad request");
        }
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        int randomInteger = (int)(Math.random() * 1000);
        GameData gameData = new GameData(randomInteger, null, null, gameName, new ChessGame());
        memoryGameDAO.addGame(gameData);
        return gameData;
        //fix this so it returns "gameID": 1234
    }
}
