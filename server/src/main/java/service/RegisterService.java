package service;

import dataaccess.*;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO memoryUserDAO, AuthDAO memoryAuthDAO){
        this.authDAO = memoryAuthDAO;
        this.userDAO = memoryUserDAO;
    }

    public AuthData addUser(UserData userData) throws ResponseException, DataAccessException {
        if (userData.username() == null || userData.email() == null || userData.password() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        if (MySqlUserDAO.getUser(userData.username()) == null){
            userDAO.createUser(userData);
            return authDAO.addAuth(userData.username());
        }
        else {
            throw new ResponseException(403, "Error: already taken");
        }
    }
}
