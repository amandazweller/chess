package service;

import dataaccess.*;

import java.util.HashMap;

public class ClearService {
    private final GameDAO memoryGameDAO;
    private final AuthDAO memoryAuthDAO;
    private final UserDAO memoryUserDAO;

    public ClearService(AuthDAO memoryAuthDAO, GameDAO memoryGameDAO, UserDAO memoryUserDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
        this.memoryUserDAO = memoryUserDAO;
    }

    public Object clear() throws DataAccessException {
        memoryGameDAO.clearGames();
        memoryAuthDAO.clearAuth();
        memoryUserDAO.clearUsers();
        return new HashMap<>();
    }
}
