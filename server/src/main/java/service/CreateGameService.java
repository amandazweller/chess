package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.ResponseException;
import model.GameData;

import java.util.Vector;

public class CreateGameService {
    private MemoryGameDAO memoryGameDAO;
    private MemoryAuthDAO memoryAuthDAO;

    public CreateGameService(MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
    }

    public GameData createGame(GameData gameData, String authToken) throws ResponseException, DataAccessException{
        if (gameData.gameID() == null || gameData.whiteUsername() == null || gameData.blackUsername() == null || gameData.gameName() == null || gameData.game() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        return memoryGameDAO.addGame(gameData);
    }
}
