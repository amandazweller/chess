package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.ResponseException;
import model.GameData;
import model.ListGameResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class ListGamesService {
    private final MemoryGameDAO memoryGameDAO;
    private final MemoryAuthDAO memoryAuthDAO;

    public ListGamesService(MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
    }

    public ListGameResponse listAllGames(String authToken) throws ResponseException, DataAccessException {
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        ListGameResponse listGameResponse = new ListGameResponse(memoryGameDAO.listAllGames());
        return listGameResponse;
    }


}
