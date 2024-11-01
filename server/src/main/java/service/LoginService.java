package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;

public class LoginService {
    private final UserDAO memoryUserDAO;
    private final AuthDAO memoryAuthDAO;

    public LoginService(UserDAO memoryUserDAO, AuthDAO memoryAuthDAO){
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
