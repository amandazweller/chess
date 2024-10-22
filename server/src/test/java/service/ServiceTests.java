package service;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.ListGameResponse;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ServiceTests {
    private MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    private MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    private RegisterService registerService = new RegisterService(memoryUserDAO, memoryAuthDAO);
    private LoginService loginService = new LoginService(memoryUserDAO, memoryAuthDAO);
    private LogoutService logoutService = new LogoutService(memoryAuthDAO);
    private ListGamesService listGamesService = new ListGamesService(memoryAuthDAO, memoryGameDAO);
    private CreateGameService createGameService = new CreateGameService(memoryAuthDAO, memoryGameDAO);
    private JoinGameService joinGameService = new JoinGameService(memoryAuthDAO, memoryGameDAO);
    private ClearService clearService = new ClearService(memoryAuthDAO, memoryGameDAO, memoryUserDAO);
    String username = "username";
    String password = "password";
    String email = "email";
    String gameName = "gameName";
    UserData userData = new UserData(username, password, email);
    int gameID = 0;


    @Test
    @DisplayName("Register Success")
    public void Register (){
        registerService.addUser(userData);
        Assertions.assertEquals(memoryUserDAO.getUser(username).username(), username);
        Assertions.assertEquals(memoryUserDAO.getUser(username).password(), password);
        Assertions.assertEquals(memoryUserDAO.getUser(username).email(), email);
    }

    @Test
    @DisplayName("Register Fail")
    public void RegisterNoPassword (){
        String username = "username";
        String password = null;
        String email = "email";
        UserData userData = new UserData(username, password, email);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            registerService.addUser(userData);
        });
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @DisplayName("Login Success")
    public void Login () {
        Register();
        loginService.getUser(userData);
        Assertions.assertEquals(memoryUserDAO.getUser(username).username(), username);
        Assertions.assertEquals(memoryUserDAO.getUser(username).password(), password);
        Assertions.assertEquals(memoryUserDAO.getUser(username).email(), email);
    }

    @Test
    @DisplayName("Login Fail")
    public void LoginFailWrongPassword () {
        Register();
        UserData failData = new UserData(username, "incorrectPassword", email);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            loginService.getUser(failData);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());

    }

    @Test
    @DisplayName("Logout Success")
    public void Logout() {
        Register();
        AuthData authData = loginService.getUser(userData);
        logoutService.logoutUser(authData.authToken());
        Assertions.assertEquals(memoryAuthDAO.getAuth(authData.authToken()), null);
    }

    @Test
    @DisplayName("Logout Fail")
    public void LogoutWrongAuth() {
        String authToken = "hello";
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            logoutService.logoutUser(authToken);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @DisplayName("Create Game Success")
    public void CreateGame() {
        Login();
        AuthData authData = loginService.getUser(userData);
        GameData currGameData = createGameService.createGame(gameName, authData.authToken());
        Assertions.assertEquals(memoryGameDAO.getGame(currGameData.gameID()), currGameData);
        gameID = currGameData.gameID();
    }

    @Test
    @DisplayName("Create Game Fail")
    public void CreateGameNoGameName() {
        Login();
        AuthData authData = loginService.getUser(userData);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            GameData currGameData = createGameService.createGame(null, authData.authToken());
        });
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @DisplayName("Join Game Success")
    public void JoinGame() {
        Login();
        String gameName2 = "gameName2";
        String playerColor = "WHITE";
        AuthData authData = loginService.getUser(userData);
        GameData game = createGameService.createGame(gameName2, authData.authToken());
        joinGameService.joinGame(game.gameID(), authData.authToken(), playerColor);
        Assertions.assertEquals(memoryGameDAO.getGame(game.gameID()).whiteUsername(), username);
    }

    @Test
    @DisplayName("Join Game Fail")
    public void JoinGameNoPlayerColor() {
        Login();
        String gameName3 = "gameName3";
        String playerColor = null;
        AuthData authData = loginService.getUser(userData);
        GameData game = createGameService.createGame(gameName3, authData.authToken());
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            joinGameService.joinGame(game.gameID(), authData.authToken(), playerColor);
        });
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @DisplayName("List Games Success")
    public void ListGames() {
        Login();
        AuthData authData = loginService.getUser(userData);
        ListGameResponse listGameResponse = listGamesService.listAllGames(authData.authToken());
        Assertions.assertEquals(listGameResponse.games(), memoryGameDAO.listAllGames());
    }

    @Test
    @DisplayName("List Games Fail")
    public void ListAllGamesWrongAuth() {
        Login();
        String authToken = "nothing";
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            ListGameResponse listGameResponse = listGamesService.listAllGames(authToken);        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @DisplayName("Clear Success")
    public void Clear() {
        clearService.clear();
        Assertions.assertEquals(memoryGameDAO.listAllGames().isEmpty(), true);
    }
}
