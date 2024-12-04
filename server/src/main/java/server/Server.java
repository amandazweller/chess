package server;

import com.google.gson.Gson;
import dataaccess.*;
import exceptions.ResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;


import model.GameData;
import model.ListGameResponse;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import service.*;
import spark.*;

public class Server {

    private final RegisterService registerService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final  ListGamesService listGamesService;
    private final CreateGameService createGameService;
    private final JoinGameService joinGameService;
    private final ClearService clearService;

    static ConcurrentHashMap<Session, Integer> allSessions = new ConcurrentHashMap<>();


    public Server(){
        try {
            AuthDAO authDAO = new MySqlAuthDAO();
            UserDAO userDAO = new MySqlUserDAO();
            GameDAO gameDAO = new MySqlGameDAO();

            this.registerService = new RegisterService(userDAO, authDAO);
            this.loginService = new LoginService(userDAO, authDAO);
            this.logoutService = new LogoutService(authDAO);
            this.listGamesService = new ListGamesService(authDAO, gameDAO);
            this.createGameService = new CreateGameService(authDAO, gameDAO);
            this.joinGameService = new JoinGameService(authDAO, gameDAO);
            this.clearService = new ClearService(authDAO, gameDAO, userDAO);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }



    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", WebsocketHandler.class);

        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearAll);
          Spark.exception(ResponseException.class, this::exceptionResponse);
          Spark.exception(DataAccessException.class, this::exceptionDataAccess);


        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void exceptionResponse(ResponseException except, Request request, Response response){
        response.status(except.getStatusCode());
        response.body(new Gson().toJson(Map.of("message", except.getMessage())));
        response.type("appilcation/json");
    }
    public void exceptionDataAccess(DataAccessException ex, Request req, Response res) {
        res.status(500);
        res.body(new Gson().toJson(Map.of("message", ex.getMessage())));
        res.type("application/json");
    }

    public Object registerUser(Request request, Response response) throws ResponseException, DataAccessException{
        var user = new Gson().fromJson(request.body(), UserData.class);
        Object registerResponse = registerService.addUser(user);
        return new Gson().toJson(registerResponse);
    }

    public Object loginUser(Request request, Response response) throws ResponseException, DataAccessException{
        var user = new Gson().fromJson(request.body(), UserData.class);
        Object loginResponse = loginService.getUser(user);
        return new Gson().toJson(loginResponse);
    }

    public Object logoutUser(Request request, Response response) throws ResponseException, DataAccessException{
        String authToken = new Gson().fromJson(request.headers("Authorization"), String.class);
        Object logoutResponse = logoutService.logoutUser(authToken);
        return new Gson().toJson(logoutResponse);
    }

    public Object listGames(Request request, Response response) throws ResponseException, DataAccessException{
        String authToken = new Gson().fromJson(request.headers("Authorization"), String.class);
        ListGameResponse listGameResponse = listGamesService.listAllGames(authToken);
        return new Gson().toJson(listGameResponse);
    }

    public Object createGame(Request request, Response response) throws ResponseException, DataAccessException {
        var game = new Gson().fromJson(request.body(), GameData.class);
        String authToken = new Gson().fromJson(request.headers("Authorization"), String.class);
        GameData createGameResponse = createGameService.createGame(game.gameName(), authToken);
        return new Gson().toJson(createGameResponse);
    }

    public Object joinGame(Request request, Response response) throws ResponseException, DataAccessException {
        var game = new Gson().fromJson(request.body(), GameData.class);
        JsonObject body = new Gson().fromJson(request.body(), JsonObject.class);
        if (!body.has("playerColor") || body.get("playerColor").isJsonNull()) {
            throw new ResponseException(400, "Error: bad request");
        }
        String playerColor = body.get("playerColor").getAsString();
        String authToken = request.headers("Authorization");
        Object joinGameResponse = joinGameService.joinGame(game.gameID(), authToken, playerColor);
        return new Gson().toJson(joinGameResponse);
    }

    public void clear() throws DataAccessException {
        clearService.clear();
    }

    public Object clearAll(Request request, Response response) throws ResponseException, DataAccessException {
        clear();
        response.status(200);
        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
