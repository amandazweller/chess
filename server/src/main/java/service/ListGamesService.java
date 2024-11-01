package service;

import dataaccess.*;
import exceptions.ResponseException;
import model.ListGameResponse;


public class ListGamesService {
    private final GameDAO memoryGameDAO;
    private final AuthDAO memoryAuthDAO;

    public ListGamesService(AuthDAO memoryAuthDAO, GameDAO memoryGameDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
    }

    public ListGameResponse listAllGames(String authToken) throws ResponseException, DataAccessException {
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        return new ListGameResponse(memoryGameDAO.listGames());
    }


}
