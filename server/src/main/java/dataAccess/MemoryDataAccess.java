package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.Random;

import javax.xml.crypto.Data;
import java.util.*;

public class MemoryDataAccess implements DataAccess {

    private HashMap<String, AuthData> AuthDataMap = new HashMap<>(); // maps an AuthToken to an AuthData object
    private HashMap<String, UserData> UserDataMap = new HashMap<>(); // maps a username to a UserData object
    private HashMap<Integer, GameData> GameDataMap = new HashMap<>(); // maps a gameID to a GameData object

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
        AuthDataMap.put(authToken, authData);
        return authData;
    }

    public AuthData getAuth(String authToken) {
        return AuthDataMap.get(authToken);
    }

    public boolean deleteAuth(String username) {
        AuthDataMap.remove(username);
        return AuthDataMap.get(username) == null;
    }

    public List<GameData> getGames(String username) {
        return new ArrayList<>(GameDataMap.values());
    }

    public Integer newGame(String username, String gameName) {
        int gameID = new Random().nextInt(10000);
        while (GameDataMap.containsKey(gameID)) {
            gameID = new Random().nextInt(10000);
        }
        GameData game = new GameData(gameID, "", "", gameName, "");
        GameDataMap.put(gameID, game);
        return gameID;
    }
}
