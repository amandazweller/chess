package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import exceptions.ResponseException;

import java.util.HashMap;

public class LogoutService {
    private final MemoryAuthDAO memoryAuthDAO;

    public LogoutService (MemoryAuthDAO memoryAuthDAO){
        this.memoryAuthDAO = memoryAuthDAO;
    }

    public Object logoutUser(String authToken) throws ResponseException, DataAccessException{
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        memoryAuthDAO.deleteAuth(authToken);
        return new HashMap<>();
    }
}
