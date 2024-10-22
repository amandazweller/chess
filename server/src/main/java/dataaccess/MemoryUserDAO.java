package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    final private Map<String, UserData> userDataMap;

    public MemoryUserDAO(){
        this.userDataMap = new HashMap<>();
    }

    public void createUser(UserData userData)  {
        userDataMap.put(userData.username(), userData);
    }

    public UserData getUser(String username){
        return userDataMap.get(username);
    }

    public void clearUsers() {
        userDataMap.clear();
    }
}
