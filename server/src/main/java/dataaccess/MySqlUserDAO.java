package dataaccess;
import org.mindrot.jbcrypt.BCrypt;

import model.UserData;
import exceptions.ResponseException;

import java.sql.*;


public class MySqlUserDAO implements UserDAO{
    MySqlDAO mySqlDAO = new MySqlDAO();
    public MySqlUserDAO() throws ResponseException, DataAccessException {
        String[] statements = {
                """
            CREATE TABLE IF NOT EXISTS  userData (
              `username` VARCHAR(255) NOT NULL,
              `password` VARCHAR(255) NOT NULL,
              `email`  VARCHAR(255) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        mySqlDAO.configureDatabase(statements);
    }

    public UserData createUser(UserData userData) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        mySqlDAO.executeUpdate(statement, userData.username(), hashedPassword, userData.email());
        return new UserData(userData.username(), hashedPassword, userData.email());
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        return new UserData(username, password, email);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to retrieve user: %s", e.getMessage()));
        }
        return null;
    }

    public void clearUsers() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM userData";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to clear users: %s", e.getMessage()));
        }
    }

}
