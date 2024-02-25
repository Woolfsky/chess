package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess {

    private HashMap<String, AuthData> AuthDataMap = new HashMap<>();
    private HashMap<String, UserData> UserDataMap = new HashMap<>();
    private HashMap<String, GameData> GameDataMap = new HashMap<>();

    public boolean deleteData() throws DataAccessException {
        AuthDataMap.clear();
        UserDataMap.clear();
        GameDataMap.clear();
        if (AuthDataMap.isEmpty() && UserDataMap.isEmpty() && GameDataMap.isEmpty()) {
            return true;
        } else {
            throw new DataAccessException("Tried to delete all data but failed");
        }
    }

    public UserData getUser(String username) {
        return UserDataMap.get(username);
    }

    public void createUser(String username, String password, String email) {
        UserData userData = new UserData(username, password, email);
        UserDataMap.put(username, userData);
    }

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        AuthDataMap.put(username, authData);
        return authData;
    }
}
