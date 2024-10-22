package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;

import java.util.HashMap;
import java.util.Objects;

public class JoinGameService {
    private MemoryGameDAO memoryGameDAO;
    private MemoryAuthDAO memoryAuthDAO;
    private MemoryUserDAO memoryUserDAO;

    public JoinGameService(MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO, MemoryUserDAO memoryUserDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
        this.memoryUserDAO = memoryUserDAO;
    }

    public Object joinGame(Integer gameID, String authToken, String playerColor) throws ResponseException, DataAccessException {
        if (memoryGameDAO.getGame(gameID) == null){
            throw new ResponseException(400, "Error: bad request");
        }
        if (Objects.equals(playerColor, "WHITE") && memoryGameDAO.getGame(gameID).whiteUsername() != null){
            throw new ResponseException(403, "Error: already taken");
        }
        if (Objects.equals(playerColor, "BLACK") && memoryGameDAO.getGame(gameID).blackUsername() != null){
            throw new ResponseException(403, "Error: already taken");
        }
        if (memoryAuthDAO.getAuth(authToken) == null){
            throw new ResponseException(401, "Error: unauthorized");
        }

//        if (Objects.equals(playerColor, "WHITE"){
//            memoryGameDAO.getGame(gameID).whiteUsername() = memoryAuthDAO.getAuth(authToken).username();
//        }
        return new HashMap<>();
    }
}
