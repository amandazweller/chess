package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData addAuth(String authToken) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken);
    public void clearAuth() throws DataAccessException;
}
