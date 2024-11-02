package dataaccess;

import chess.ChessGame;
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
    public void addAuth() throws DataAccessException {
        AuthData authData = authDAO.addAuth(username);
        Assertions.assertNotNull(authData);
        Assertions.assertEquals(authDAO.getAuth(authData.authToken()).authToken(), authData.authToken());
    }

    @Test
    @DisplayName("AddAuth Failure")
    public void addAuthNullUsername() {
        ResponseException exception = Assertions.assertThrows(
                ResponseException.class,
                () -> authDAO.addAuth(null)
        );
        Assertions.assertTrue(
                exception.getMessage().contains("unable to update database")
        );
        Assertions.assertTrue(
                exception.getMessage().contains("Column 'username' cannot be null")
        );
    }

    @Test
    @DisplayName("GetAuth Success")
    public void getAuth() throws DataAccessException {
        AuthData newAuthData = authDAO.addAuth(username);
        AuthData authData = authDAO.getAuth(newAuthData.authToken());
        Assertions.assertNotNull(authData);
        Assertions.assertEquals(authDAO.getAuth(authData.authToken()).authToken(), authData.authToken());
    }

    @Test
    @DisplayName("GetAuth Failure")
    public void getAuthWrongToken() throws DataAccessException {
        AuthData authData = authDAO.getAuth("nonExistentToken");
        Assertions.assertNull(authData);
    }

    @Test
    @DisplayName("DeleteAuth Success")
    public void deleteAuth() throws DataAccessException {
        AuthData authData = authDAO.addAuth(username);
        authDAO.deleteAuth(authData.authToken());
        Assertions.assertNull(authDAO.getAuth(authData.authToken()));

    }

    @Test
    @DisplayName("ClearAuth Success")
    public void clearAuth() throws DataAccessException {
        AuthData authData = authDAO.addAuth(username);
        authDAO.clearAuth();
        Assertions.assertNull(authDAO.getAuth(authData.authToken()));
    }

    @Test
    @DisplayName("AddGame Success")
    public void addGame() throws DataAccessException {
        gameDAO.clearGames();
        GameData gameData = new GameData(1, null, null, "gameName", new ChessGame());
        GameData createdGame = gameDAO.addGame(gameData);
        Assertions.assertNotNull(createdGame);
        Assertions.assertEquals(createdGame, gameData);
    }

    @Test
    @DisplayName("AddGame Failure")
    public void addGameNullGameName() {
        GameData gameData = new GameData(1, null, null, null, new ChessGame());
        ResponseException exception = Assertions.assertThrows(
                ResponseException.class,
                () -> gameDAO.addGame(gameData));
        Assertions.assertTrue(
                exception.getMessage().contains("unable to update database"));
    }

    @Test
    @DisplayName("GetGame Success")
    public void getGame() throws DataAccessException {
        gameDAO.clearGames();
        GameData gameData = new GameData(1, null, null, "gameName", new ChessGame());
        gameDAO.addGame(gameData);
        GameData retrievedGame = gameDAO.getGame(1);
        Assertions.assertNotNull(retrievedGame);
        Assertions.assertEquals(gameData.gameName(), retrievedGame.gameName());
        Assertions.assertEquals(gameData.gameID(), retrievedGame.gameID());
        Assertions.assertEquals(gameData.whiteUsername(), retrievedGame.whiteUsername());
        Assertions.assertEquals(gameData.blackUsername(), retrievedGame.blackUsername());
    }

    @Test
    @DisplayName("GetGame Failure")
    public void getGameWrongID() throws DataAccessException {
        GameData gameData = gameDAO.getGame(-1);  // Non-existent ID
        Assertions.assertNull(gameData);
    }

    @Test
    @DisplayName("ListGames Success")
    public void listGames() throws DataAccessException {
        ArrayList<GameData> games = gameDAO.listGames();
        Assertions.assertNotNull(games);
    }

    @Test
    @DisplayName("ClearGames Success")
    public void clearGames() throws DataAccessException {
        gameDAO.clearGames();
        ArrayList<GameData> games = gameDAO.listGames();
        Assertions.assertTrue(games.isEmpty());
    }

    @Test
    @DisplayName("CreateUser Success")
    public void createUser() throws DataAccessException {
        userDAO.clearUsers();
        UserData createdUser = userDAO.createUser(userData);
        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals(username, createdUser.username());
    }

    @Test
    @DisplayName("CreateUser Failure")
    public void createUserSameUsername() throws DataAccessException {
        userDAO.clearUsers();
        userDAO.createUser(userData);
        Assertions.assertThrows(ResponseException.class, () -> userDAO.createUser(userData));
    }

    @Test
    @DisplayName("GetUser Success")
    public void getUser() throws DataAccessException {
        userDAO.clearUsers();
        userDAO.createUser(userData);
        UserData sameUser = userDAO.getUser(username);
        Assertions.assertNotNull(sameUser);
        Assertions.assertEquals(username, sameUser.username());
    }

    @Test
    @DisplayName("GetUser Failure")
    public void getUserWrongUsername() throws DataAccessException {
        UserData retrievedUser = userDAO.getUser("nonExistentUsername");
        Assertions.assertNull(retrievedUser);
    }

    @Test
    @DisplayName("ClearUsers Success")
    public void clearUsers() throws DataAccessException {
        userDAO.clearUsers();
        UserData retrievedUser = userDAO.getUser(username);
        Assertions.assertNull(retrievedUser);
    }

    @Test
    @DisplayName("SetWhiteUsername Success")
    public void setWhiteUsername() throws DataAccessException {
        gameDAO.clearGames();
        GameData gameData = new GameData(1, null, null, "gameName", new ChessGame());
        gameDAO.addGame(gameData);

        String newWhiteUsername = "newWhitePlayer";
        gameDAO.setWhiteUsername(1, newWhiteUsername);

        GameData updatedGame = gameDAO.getGame(1);
        Assertions.assertNotNull(updatedGame);
        Assertions.assertEquals(newWhiteUsername, updatedGame.whiteUsername());
    }

    @Test
    @DisplayName("SetBlackUsername Success")
    public void setBlackUsername() throws DataAccessException {
        gameDAO.clearGames();
        GameData gameData = new GameData(1, null, null, "gameName", new ChessGame());
        gameDAO.addGame(gameData);

        String newBlackUsername = "newBlackPlayer";
        gameDAO.setBlackUsername(1, newBlackUsername);

        GameData updatedGame = gameDAO.getGame(1);
        Assertions.assertNotNull(updatedGame);
        Assertions.assertEquals(newBlackUsername, updatedGame.blackUsername());
    }

}
