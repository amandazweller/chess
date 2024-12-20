package service;

import dataaccess.*;
import exceptions.ResponseException;

import java.util.HashMap;
import java.util.Objects;

public class JoinGameService {
    private final GameDAO memoryGameDAO;
    private final AuthDAO memoryAuthDAO;

    public JoinGameService(AuthDAO memoryAuthDAO, GameDAO memoryGameDAO){
        this.memoryAuthDAO = memoryAuthDAO;
        this.memoryGameDAO = memoryGameDAO;
    }

    public Object joinGame(Integer gameID, String authToken, String playerColor) throws ResponseException, DataAccessException {
        if (gameID == null || authToken == null || playerColor == null){
            throw new ResponseException(400, "Error: bad request");
        }
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

        if (Objects.equals(playerColor, "WHITE")){
            memoryGameDAO.setWhiteUsername(gameID, memoryAuthDAO.getAuth(authToken).username());
        }
        if (Objects.equals(playerColor, "BLACK")){
            memoryGameDAO.setBlackUsername(gameID, memoryAuthDAO.getAuth(authToken).username());
        }
        return new HashMap<>();
    }
}
