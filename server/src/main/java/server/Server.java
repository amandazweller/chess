package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;

import java.util.Map;

import com.google.gson.JsonObject;


import model.GameData;
import model.ListGameResponse;
import model.UserData;
import service.*;
import spark.*;

public class Server {
    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    private final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    private final RegisterService registerService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final  ListGamesService listGamesService;
    private final CreateGameService createGameService;
    private final JoinGameService joinGameService;
    private final ClearService clearService;

    public Server(){
        this.registerService = new RegisterService(memoryUserDAO, memoryAuthDAO);
        this.loginService = new LoginService(memoryUserDAO, memoryAuthDAO);
        this.logoutService = new LogoutService(memoryAuthDAO);
        this.listGamesService = new ListGamesService(memoryAuthDAO, memoryGameDAO);
        this.createGameService = new CreateGameService(memoryAuthDAO, memoryGameDAO);
        this.joinGameService = new JoinGameService(memoryAuthDAO, memoryGameDAO);
        this.clearService = new ClearService(memoryAuthDAO, memoryGameDAO, memoryUserDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
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

    private void exceptionResponse(ResponseException except, Request request, Response response){
        response.status(except.getStatusCode());
        response.body(new Gson().toJson(Map.of("message", except.getMessage())));
        response.type("appilcation/json");
    }
    private void exceptionDataAccess(DataAccessException ex, Request req, Response res) {
        res.status(500);
        res.body(new Gson().toJson(Map.of("message", ex.getMessage())));
        res.type("application/json");
    }

    private Object registerUser(Request request, Response response) throws ResponseException, DataAccessException{
        var user = new Gson().fromJson(request.body(), UserData.class);
        Object registerResponse = registerService.addUser(user);
        return new Gson().toJson(registerResponse);
    }

    private Object loginUser(Request request, Response response) throws ResponseException, DataAccessException{
        var user = new Gson().fromJson(request.body(), UserData.class);
        Object loginResponse = loginService.getUser(user);
        return new Gson().toJson(loginResponse);
    }

    private Object logoutUser(Request request, Response response) throws ResponseException, DataAccessException{
        String authToken = new Gson().fromJson(request.headers("Authorization"), String.class);
        Object logoutResponse = logoutService.logoutUser(authToken);
        return new Gson().toJson(logoutResponse);
    }

    private Object listGames(Request request, Response response) throws ResponseException, DataAccessException{
        String authToken = new Gson().fromJson(request.headers("Authorization"), String.class);
        ListGameResponse listGameResponse = listGamesService.listAllGames(authToken);
        return new Gson().toJson(listGameResponse);
    }

    private Object createGame(Request request, Response response) throws ResponseException{
        var game = new Gson().fromJson(request.body(), GameData.class);
        String authToken = new Gson().fromJson(request.headers("Authorization"), String.class);
        GameData createGameResponse = createGameService.createGame(game.gameName(), authToken);
        return new Gson().toJson(createGameResponse);
    }

    private Object joinGame(Request request, Response response) throws ResponseException{
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

    private Object clearAll(Request request, Response response) throws ResponseException{
        Object clearResponse = clearService.clear();
        return new Gson().toJson(clearResponse);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
