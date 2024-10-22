package dataaccess;

import model.GameData;

import java.util.Vector;

public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;
    Vector<GameData> listAllGames() throws DataAccessException;
    GameData addGame(GameData gameData) throws DataAccessException;
}
