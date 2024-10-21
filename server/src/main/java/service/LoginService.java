package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class LoginService {
    private MemoryUserDAO memoryUserDAO;
    private MemoryAuthDAO memoryAuthDAO;

    public LoginService(MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryUserDAO = memoryUserDAO;
    }

    public AuthData getUser(UserData userData) throws ResponseException, DataAccessException{
        if (memoryUserDAO.getUser(userData.username()) == null){
            throw new ResponseException(400, "Error: unauthorized");
        }
        if (!userData.password().equals(memoryUserDAO.getUser(userData.username()).password())){
            throw new ResponseException(400, "Error: unauthorized");
        }
        else {
            return memoryAuthDAO.addAuth(userData.username());
        }
    }

}
