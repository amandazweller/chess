package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData addAuth(String username) throws DataAccessException;
    AuthData getAuth(String username) throws DataAccessException;
}
