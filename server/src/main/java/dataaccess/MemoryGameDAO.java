package dataaccess;

import exceptions.ResponseException;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MemoryGameDAO implements GameDAO{
    final private Map<Integer, GameData> gameDataMap;

    public MemoryGameDAO(){
        this.gameDataMap = new HashMap<>();
    }
    public GameData getGame(int gameID) throws DataAccessException {
        return gameDataMap.get(gameID);
    }

    public Vector<GameData> listAllGames() throws DataAccessException {
        Vector<GameData> allGames = new Vector<>();
        for (Integer key : gameDataMap.keySet()) {
            GameData gameData = gameDataMap.get(key);
            allGames.add(gameData);
        }
        return allGames;
    }

    public GameData addGame(GameData gameData) throws DataAccessException{
        return gameDataMap.put(gameData.gameID(), gameData);
    }
}
