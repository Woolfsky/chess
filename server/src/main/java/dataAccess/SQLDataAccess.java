package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.List;

public class SQLDataAccess implements DataAccess {
    public boolean deleteData() throws DataAccessException {
        return false;
    };

    public UserData getUser(String username) {
        return null;
    };

    public void createUser(String username, String password, String email) {};

    public AuthData createAuth(String username) {
        return null;
    };

    public AuthData getAuth(String username) {
        return null;
    };

    public boolean deleteAuth(String username) {
        return false;
    };

    public List<GameData> getGames(String username) {
        return null;
    };

    public Integer newGame(String username, String gameName) {
        return null;
    };

    public GameData getGame(Integer gameID) {
        return null;
    };

    public boolean addPlayer(Integer gameID, String username, String playerColor) {
        return false;
    };
}
