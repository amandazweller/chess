package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import exceptions.ResponseException;

import java.util.HashMap;

public class LogoutService {
    private final AuthDAO memoryAuthDAO;

    public LogoutService (AuthDAO memoryAuthDAO){
        this.memoryAuthDAO = memoryAuthDAO;
    }

    public Object logoutUser(String authToken) throws ResponseException{
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        memoryAuthDAO.deleteAuth(authToken);
        return new HashMap<>();
    }
}
