package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import java.util.Map;

import model.GameData;
import model.UserData;
import service.*;
import spark.*;

public class Server {
    private MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    private MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    private RegisterService registerService;
    private LoginService loginService;
    private LogoutService logoutService;
    private ListGamesService listGamesService;
    private CreateGameService createGameService;

    public Server(){
        this.registerService = new RegisterService(memoryUserDAO, memoryAuthDAO);
        this.loginService = new LoginService(memoryUserDAO, memoryAuthDAO);
        this.logoutService = new LogoutService(memoryUserDAO, memoryAuthDAO);
        this.listGamesService = new ListGamesService(memoryAuthDAO, memoryGameDAO);
        this.createGameService = new CreateGameService(memoryAuthDAO, memoryGameDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        //Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
//        Spark.put("/game", userHandler.joinGame);
//        Spark.delete("/db", userHandler.clearAll);
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
        Object listGameResponse = listGamesService.listAllGames(authToken);
        return new Gson().toJson(listGameResponse);
    }

    private Object createGame(Request request, Response response) throws ResponseException, DataAccessException{
        var game = new Gson().fromJson(request.body(), GameData.class);
        String authToken = new Gson().fromJson(request.headers("Authorization"), String.class);
        Object createGameResponse = createGameService.createGame(game, authToken);
        return new Gson().toJson(createGameResponse);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
