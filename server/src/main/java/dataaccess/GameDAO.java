package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listAllGames() throws DataAccessException;
    GameData addGame(GameData gameData) throws DataAccessException;
    public void clearGames() throws DataAccessException;
    public void setWhiteUsername(Integer gameID, String whiteUsername);
    public void setBlackUsername(Integer gameID, String blackUsername);
}
