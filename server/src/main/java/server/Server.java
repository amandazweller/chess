package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import java.util.Map;
import model.UserData;
import service.RegisterService;
import spark.*;

public class Server {
    private MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    private MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    private RegisterService registerService;
    public Server(){
        this.registerService = new RegisterService(memoryUserDAO, memoryAuthDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
//        Spark.post("/session", userHandler.loginUser);
//        Spark.delete("/session", userHandler.logoutUser);
//        Spark.post("/game", userHandler.createGame);
//        Spark.get("/game", userHandler.listGames);
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

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}