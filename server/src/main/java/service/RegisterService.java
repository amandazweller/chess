package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;

public class RegisterService {
    private MemoryUserDAO memoryUserDAO;
    private MemoryAuthDAO memoryAuthDAO;

    public RegisterService(MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryUserDAO = memoryUserDAO;
    }

    public AuthData addUser(UserData userData) throws ResponseException, DataAccessException {
        if (userData.username() == null || userData.email() == null || userData.password() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        if (memoryUserDAO.getUser(userData.username()) == null){
            memoryUserDAO.createUser(userData);
            return memoryAuthDAO.addAuth(userData.username());
        }
        else {
            throw new ResponseException(403, "Error: already taken");
        }
    }
}
