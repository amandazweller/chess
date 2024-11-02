package dataaccess;

import model.AuthData;
import exceptions.ResponseException;

import java.sql.*;
import java.util.UUID;


public class MySqlAuthDAO implements AuthDAO{
    MySqlDAO mySqlDAO = new MySqlDAO();
    public MySqlAuthDAO() throws ResponseException, DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  authData (
              `username` VARCHAR(255) NOT NULL,
              `authToken` VARCHAR(255) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        mySqlDAO.configureDatabase(createStatements);
    }

    public AuthData addAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData (username, authToken);
        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        mySqlDAO.executeUpdate(statement, authData.authToken(), authData.username());
        return authData;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM authData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String username = rs.getString("username");
                        return new AuthData(username, authToken);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to get auth data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authData WHERE authToken = ?";
        mySqlDAO.executeUpdate(statement, authToken);
    }

    public void clearAuth() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM authData";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to clear auth data: %s", e.getMessage()));
        }
    }


}