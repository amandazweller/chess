package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;

public class LoginService {
    private final MemoryUserDAO memoryUserDAO;
    private final MemoryAuthDAO memoryAuthDAO;

    public LoginService(MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryUserDAO = memoryUserDAO;
    }

    public AuthData getUser(UserData userData) throws ResponseException{
        if (memoryUserDAO.getUser(userData.username()) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (!userData.password().equals(memoryUserDAO.getUser(userData.username()).password())){
            throw new ResponseException(401, "Error: unauthorized");
        }
        else {
            return memoryAuthDAO.addAuth(userData.username());
        }
    }

}
