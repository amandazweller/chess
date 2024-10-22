package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

import java.util.HashMap;

public class ClearService {
    private MemoryGameDAO memoryGameDAO;
    private MemoryAuthDAO memoryAuthDAO;
    private MemoryUserDAO memoryUserDAO;

    public ClearService(MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO, MemoryUserDAO memoryUserDAO){
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
