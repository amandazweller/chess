package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import model.GameData;

import java.util.Vector;

public class ListGamesService {
    private MemoryGameDAO memoryGameDAO;
    private MemoryAuthDAO memoryAuthDAO;

    public ListGamesService(MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
    }

    public Vector<GameData> listAllGames(String authToken) throws ResponseException, DataAccessException {
        return null;
    }


}
