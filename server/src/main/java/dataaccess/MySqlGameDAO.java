package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import com.google.gson.Gson;
import exceptions.ResponseException;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlGameDAO implements GameDAO{
    public MySqlGameDAO() throws ResponseException, DataAccessException {
        configureDatabase();
    }
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername;
                        String blackUsername;
                        if (rs.getString("whiteUsername") == null){
                            whiteUsername = null;
                        }
                        else {
                            whiteUsername = rs.getString("whiteUsername;");
                        }
                        if (rs.getString("blackUsername") == null){
                            blackUsername = null;
                        }
                        else {
                            blackUsername = rs.getString("blackUsername;");
                        }
                        String gameName = rs.getString("gameName");
                        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to get game: %s", e.getMessage()));
        }
        return null;
    }


    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gameData";
            try (var ps = conn.prepareStatement(statement);
                 var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int gameId = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    GameData gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
                    games.add(gameData);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to list all games: %s", e.getMessage()));
        }
        return games;
    }


    public GameData addGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        return gameData;
    }

    public void clearGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to clear games: %s", e.getMessage()));
        }
    }

    public void setWhiteUsername(Integer gameID, String whiteUsername) {
        var statement = "UPDATE gameData SET whiteUsername = ? WHERE gameID = ?";
        executeUpdate(statement, whiteUsername, gameID);
    }

    public void setBlackUsername(Integer gameID, String blackUsername) {
        var statement = "UPDATE gameData SET blackUsername = ? WHERE gameID = ?";
        executeUpdate(statement, blackUsername, gameID);
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof UserData p) ps.setString(i + 1, p.toString());
                    else if (param instanceof ChessGame p) ps.setString(i + 1, new Gson().toJson(p));
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `gameID` int NOT NULL,
              `whiteUsername` VARCHAR(255),
              `blackUsername`  VARCHAR(255),
              `gameName` VARCHAR(255) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }



}
