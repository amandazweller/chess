package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    final private Map<Integer, GameData> gameDataMap;

    public MemoryGameDAO(){
        this.gameDataMap = new HashMap<>();
    }
    public GameData getGame(int gameID)  {
        return gameDataMap.get(gameID);
    }

    public ArrayList<GameData> listGames() {
        ArrayList<GameData> allGames = new ArrayList<>();
        for (Integer key : gameDataMap.keySet()) {
            GameData gameData = gameDataMap.get(key);
            allGames.add(gameData);
        }
        return allGames;
    }

    public GameData addGame(GameData gameData) {
        return gameDataMap.put(gameData.gameID(), gameData);
    }

    public void clearGames() {
        gameDataMap.clear();
    }

    public void setWhiteUsername(Integer gameID, String whiteUsername)  {
        GameData gameData = getGame(gameID);
        GameData updatedData = new GameData(gameID, whiteUsername, gameData.blackUsername(), gameData.gameName(), gameData.game());
        gameDataMap.replace(gameData.gameID(), updatedData);
    }
    public void setBlackUsername(Integer gameID, String blackUsername)  {
        GameData gameData = getGame(gameID);
        GameData updatedData = new GameData(gameID, gameData.whiteUsername(), blackUsername, gameData.gameName(), gameData.game());
        gameDataMap.replace(gameData.gameID(), updatedData);
    }

}
