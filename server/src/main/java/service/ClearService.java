package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

import java.util.HashMap;

public class ClearService {
    private final MemoryGameDAO memoryGameDAO;
    private final MemoryAuthDAO memoryAuthDAO;
    private final MemoryUserDAO memoryUserDAO;

    public ClearService(MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO, MemoryUserDAO memoryUserDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
        this.memoryUserDAO = memoryUserDAO;
    }

    public Object clear() {
        memoryGameDAO.clearGames();
        memoryAuthDAO.clearAuth();
        memoryUserDAO.clearUsers();
        return new HashMap<>();
    }
}
