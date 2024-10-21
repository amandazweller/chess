package dataaccess;

import model.AuthData;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    final private Map<String, AuthData> authDataMap;

    public MemoryAuthDAO(){
        this.authDataMap = new HashMap<>();
    }

    public AuthData addAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData (username, authToken);
        authDataMap.put(authToken, authData);
        return authData;
    }

    public AuthData getAuth(String username) throws DataAccessException {
        return null;
    }
}
