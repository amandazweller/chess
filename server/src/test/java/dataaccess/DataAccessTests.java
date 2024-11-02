package dataaccess;

import dataaccess.*;
import dataaccess.DataAccessException;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
public class DataAccessTests {

    // Sample data for testing
    private final String username = "username";
    private final String password = "password";
    private final String email = "email";
    private final UserData userData = new UserData(username, password, email);

    // DAOs
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    {
        try {
            authDAO = new MySqlAuthDAO();
            userDAO = new MySqlUserDAO();
            gameDAO = new MySqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("AddAuth Success")
    public void addAuth_Success() throws DataAccessException {
        AuthData authData = authDAO.addAuth(username);
        Assertions.assertNotNull(authData);
        Assertions.assertEquals(authDAO.getAuth(authData.authToken()).authToken(), authData.authToken());
    }

    @Test
    @DisplayName("AddAuth Failure - Null Username")
    public void addAuth_Failure_NullUsername() {
        // Expecting an exception to be thrown when trying to add a null username
        ResponseException exception = Assertions.assertThrows(
                ResponseException.class,
                () -> authDAO.addAuth(null), // Providing a null username
                "Expected ResponseException for null username insertion, but it was not thrown."
        );

        // Check if the exception message matches the expected message
        Assertions.assertTrue(
                exception.getMessage().contains("unable to update database"),
                "The exception message should indicate a database update issue due to null username."
        );
        Assertions.assertTrue(
                exception.getMessage().contains("Column 'username' cannot be null"),
                "The exception message should indicate that the 'username' column cannot be null."
        );
    }

    @Test
    @DisplayName("GetAuth Success")
    public void getAuth_Success() throws DataAccessException {
        AuthData newAuthData = authDAO.addAuth(username);
        AuthData authData = authDAO.getAuth(newAuthData.authToken());
        Assertions.assertNotNull(authData);
        Assertions.assertEquals(authDAO.getAuth(authData.authToken()).authToken(), authData.authToken());
    }

    @Test
    @DisplayName("GetAuth Failure - Non-existent Token")
    public void getAuth_Failure_NonExistentToken() throws DataAccessException {
        AuthData authData = authDAO.getAuth("nonExistentToken");
        Assertions.assertNull(authData);  // Expecting null if token does not exist
    }

}
