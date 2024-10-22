package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.ResponseException;
import model.ListGameResponse;


public class ListGamesService {
    private final MemoryGameDAO memoryGameDAO;
    private final MemoryAuthDAO memoryAuthDAO;

    public ListGamesService(MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
    }

    public ListGameResponse listAllGames(String authToken) throws ResponseException {
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        return new ListGameResponse(memoryGameDAO.listAllGames());
    }


}
