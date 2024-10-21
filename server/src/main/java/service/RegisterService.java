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

    }

    public AuthData addUser(UserData userData) throws ResponseException, DataAccessException {
        if (userData.username() == null || userData.email() == null || userData.password() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        UserData oldData = memoryUserDAO.getUser(userData.username());
        if (oldData != null){
            throw new ResponseException(403, "Error: already taken");
        }
        return memoryAuthDAO.addAuth(userData.username());
    }
}
