package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import model.UserData;

import java.util.HashMap;

public class LogoutService {
    private MemoryUserDAO memoryUserDAO;
    private MemoryAuthDAO memoryAuthDAO;

    public LogoutService(MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryUserDAO = memoryUserDAO;
    }

    public Object logoutUser(String authToken) throws ResponseException, DataAccessException{
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        memoryAuthDAO.deleteAuth(authToken);
        return new HashMap<>();
    }
}
