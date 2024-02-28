package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.Random;

import java.util.*;

public class MemoryDataAccess implements DataAccess {

    private HashMap<String, AuthData> authDataMap = new HashMap<>(); // maps an AuthToken to an AuthData object
    private HashMap<String, UserData> userDataMap = new HashMap<>(); // maps a username to a UserData object
    private HashMap<Integer, GameData> gameDataMap = new HashMap<>(); // maps a gameID to a GameData object

    public boolean deleteData() throws DataAccessException {
        authDataMap.clear();
        userDataMap.clear();
        gameDataMap.clear();
        if (authDataMap.isEmpty() && userDataMap.isEmpty() && gameDataMap.isEmpty()) {
            return true;
        } else {
            throw new DataAccessException("Tried to delete all data but failed");
        }
    }

    public UserData getUser(String username) {
        return userDataMap.get(username);
    }

    public void createUser(String username, String password, String email) {
        UserData userData = new UserData(username, password, email);
        userDataMap.put(username, userData);
    }

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDataMap.put(authToken, authData);
        return authData;
    }

    public AuthData getAuth(String authToken) {
        return authDataMap.get(authToken);
    }

    public boolean deleteAuth(String username) {
        authDataMap.remove(username);
        return authDataMap.get(username) == null;
    }

    public List<GameData> getGames(String username) {
        return new ArrayList<>(gameDataMap.values());
    }

    public Integer newGame(String username, String gameName) {
        int gameID = new Random().nextInt(10000);
        while (gameDataMap.containsKey(gameID)) {
            gameID = new Random().nextInt(10000);
        }
        GameData game = new GameData(gameID, null, null, gameName, null);
        gameDataMap.put(gameID, game);
        return gameID;
    }

    public GameData getGame(Integer gameID) {
        return gameDataMap.get(gameID);
    }

    public boolean addPlayer(Integer gameID, String username, String playerColor) {
        GameData gameData = gameDataMap.get(gameID);
        GameData updatedGameData = gameData.addPlayerToRecord(playerColor, username);
        gameDataMap.put(gameID, updatedGameData);
        return true;
    };
}
