package service;
import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.ListGameResponse;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ServiceTests {

    UserDAO memoryUserDAO = new MySqlUserDAO();
    AuthDAO memoryAuthDAO = new MySqlAuthDAO();
    GameDAO memoryGameDAO = new MySqlGameDAO();
    RegisterService registerService = new RegisterService(memoryUserDAO, memoryAuthDAO);
    LoginService loginService = new LoginService(memoryUserDAO, memoryAuthDAO);
    LogoutService logoutService = new LogoutService(memoryAuthDAO);
    ListGamesService listGamesService = new ListGamesService(memoryAuthDAO, memoryGameDAO);
    CreateGameService createGameService = new CreateGameService(memoryAuthDAO, memoryGameDAO);
    JoinGameService joinGameService = new JoinGameService(memoryAuthDAO, memoryGameDAO);
    ClearService clearService = new ClearService(memoryAuthDAO, memoryGameDAO, memoryUserDAO);




    String username = "username";
    String password = "password";
    String email = "email";
    String gameName = "gameName";
    UserData userData = new UserData(username, password, email);
    int gameID = 0;

    public ServiceTests() throws DataAccessException {
    }


    @Test
    @DisplayName("Register Success")
    public void register () throws DataAccessException {
        clearService.clear();
        registerService.addUser(userData);
        Assertions.assertEquals(memoryUserDAO.getUser(username).username(), username);
        Assertions.assertEquals(memoryUserDAO.getUser(username).email(), email);
    }

    @Test
    @DisplayName("Register Fail")
    public void registerNoPassword () throws DataAccessException {
        clearService.clear();
        String username = "username";
        String password = null;
        String email = "email";
        UserData userData = new UserData(username, password, email);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> registerService.addUser(userData));
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @DisplayName("Login Success")
    public void login () throws DataAccessException {
        register();
        loginService.getUser(userData);
        Assertions.assertEquals(memoryUserDAO.getUser(username).username(), username);
        Assertions.assertEquals(memoryUserDAO.getUser(username).email(), email);
    }

    @Test
    @DisplayName("Login Fail")
    public void loginFailWrongPassword () throws DataAccessException {
        register();
        UserData failData = new UserData(username, "incorrectPassword", email);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> loginService.getUser(failData));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());

    }

    @Test
    @DisplayName("Logout Success")
    public void logout() throws DataAccessException {
        register();
        AuthData authData = loginService.getUser(userData);
        logoutService.logoutUser(authData.authToken());
        Assertions.assertNull(memoryAuthDAO.getAuth(authData.authToken()));
    }

    @Test
    @DisplayName("Logout Fail")
    public void logoutWrongAuth() {
        String authToken = "hello";
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> logoutService.logoutUser(authToken));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGame() throws DataAccessException {
        login();
        AuthData authData = loginService.getUser(userData);
        GameData currGameData = createGameService.createGame(gameName, authData.authToken());
        Assertions.assertEquals(memoryGameDAO.getGame(currGameData.gameID()).whiteUsername(), currGameData.whiteUsername());
        Assertions.assertEquals(memoryGameDAO.getGame(currGameData.gameID()).blackUsername(), currGameData.blackUsername());
        Assertions.assertEquals(memoryGameDAO.getGame(currGameData.gameID()).gameName(), currGameData.gameName());
        gameID = currGameData.gameID();
    }

    @Test
    @DisplayName("Create Game Fail")
    public void createGameNoGameName() throws DataAccessException {
        login();
        AuthData authData = loginService.getUser(userData);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () ->
                createGameService.createGame(null, authData.authToken()));
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @DisplayName("Join Game Success")
    public void joinGame() throws DataAccessException {
        String gameName2 = "gameName2";
        String playerColor = "WHITE";
        AuthData authData = loginService.getUser(userData);
        GameData game = createGameService.createGame(gameName2, authData.authToken());
        joinGameService.joinGame(game.gameID(), authData.authToken(), playerColor);
        Assertions.assertEquals(memoryGameDAO.getGame(game.gameID()).whiteUsername(), username);
    }

    @Test
    @DisplayName("Join Game Fail")
    public void joinGameNoPlayerColor() throws DataAccessException {
        login();
        String gameName3 = "gameName3";
        String playerColor = null;
        AuthData authData = loginService.getUser(userData);
        GameData game = createGameService.createGame(gameName3, authData.authToken());
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () ->
                joinGameService.joinGame(game.gameID(), authData.authToken(), playerColor));
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @DisplayName("List Games Success")
    public void listGames() throws DataAccessException {
        login();
        AuthData authData = loginService.getUser(userData);
        ListGameResponse listGameResponse = listGamesService.listAllGames(authData.authToken());
        Assertions.assertEquals(listGameResponse.games(), memoryGameDAO.listGames());
    }

    @Test
    @DisplayName("List Games Fail")
    public void listAllGamesWrongAuth() throws DataAccessException {
        login();
        String authToken = "nothing";
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> listGamesService.listAllGames(authToken));
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @DisplayName("Clear Success")
    public void clear() throws DataAccessException {
        clearService.clear();
        Assertions.assertTrue(memoryGameDAO.listGames().isEmpty());
    }
}
