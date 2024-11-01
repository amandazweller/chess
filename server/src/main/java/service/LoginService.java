package service;

import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class LoginService {
    private final UserDAO memoryUserDAO;
    private final AuthDAO memoryAuthDAO;

    public LoginService(UserDAO memoryUserDAO, AuthDAO memoryAuthDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryUserDAO = memoryUserDAO;
    }

    public AuthData getUser(UserData userData) throws ResponseException, DataAccessException {
        if (memoryUserDAO.getUser(userData.username()) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (!verifyUserPassword(userData.username(), userData.password())){
            throw new ResponseException(401, "Error: unauthorized");
        }
        else {
            return memoryAuthDAO.addAuth(userData.username());
        }
    }

    public boolean verifyUserPassword(String username, String password) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT password FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String storedHashedPassword = rs.getString("password");
                        return BCrypt.checkpw(password, storedHashedPassword);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to verify user password: %s", e.getMessage()));
        }
        return false;
    }

}
