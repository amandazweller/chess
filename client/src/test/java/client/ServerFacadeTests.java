package client;

import dataaccess.DataAccessException;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    private static Server server;

    private ServerFacade facade;
    static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setup() throws DataAccessException {
        server.clear();
        facade = new ServerFacade("http://localhost:" + port);

    }

    @AfterEach
    void cleanup() throws DataAccessException {
        server.clear();
    }

    @Test
    public void registerPositive() throws ResponseException {
        assertTrue(facade.registerUser("username", "password", "email"));
    }

    @Test
    public void registerNegative() throws ResponseException {
        facade.registerUser("username", "password", "email");
        assertFalse(facade.registerUser("username", "password", "email"));
    }

    @Test
    public void loginPositive() throws ResponseException {
        facade.registerUser("username", "password", "email");
        assertTrue(facade.loginUser("username", "password"));
    }

    @Test
    public void loginNegative() throws ResponseException {
        facade.registerUser("username", "password", "email");
        assertFalse(facade.loginUser("username", "wrong"));
    }

    @Test
    public void logoutPositive() throws ResponseException {
        facade.registerUser("username", "password", "email");
        assertTrue(facade.logoutUser());
    }

    @Test
    public void logoutNegative() throws ResponseException {
        assertFalse(facade.logoutUser());
    }

    @Test
    public void createGamePositive() throws ResponseException {
        facade.registerUser("username", "password", "email");
        assertTrue(facade.createGame("gameName"));
    }

    @Test
    public void createGameNegative() throws ResponseException {
        assertFalse(facade.createGame("gameName"));
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        facade.registerUser("username", "password", "email");
        facade.createGame("gameName");
        assertEquals(1, facade.listGames().size());
    }

    @Test
    public void listGamesNegative() throws ResponseException {
        assertTrue(facade.listGames().isEmpty());
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        facade.registerUser("username", "password", "email");
        facade.createGame("gameName");
        ArrayList<GameData> games = facade.listGames();
        int gameID = games.getFirst().gameID();
        boolean result = facade.joinGame(gameID, "WHITE");
        assertTrue(result);
    }

    @Test
    public void joinGameNegative() throws ResponseException {
        facade.registerUser("username", "password", "email");
        facade.createGame("gameName");
        boolean result = facade.joinGame(0, "WHITE");
        assertFalse(result);
    }
}
