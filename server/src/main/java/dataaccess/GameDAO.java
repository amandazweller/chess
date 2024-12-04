package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    GameData addGame(GameData gameData) throws DataAccessException;
    public void clearGames() throws DataAccessException;
    public void setWhiteUsername(Integer gameID, String whiteUsername) throws DataAccessException;
    public void setBlackUsername(Integer gameID, String blackUsername) throws DataAccessException;

}
