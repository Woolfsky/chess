package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.List;
import java.util.Map;

public interface DataAccess {
    boolean deleteData() throws DataAccessException;

    UserData getUser(String username);

    void createUser(String username, String password, String email);

    AuthData createAuth(String username);

    AuthData getAuth(String username);

    boolean deleteAuth(String username);

    List<GameData> getGames(String username);

    Integer newGame(String username, String gameName);

    GameData getGame(Integer gameID);

    boolean addPlayer(Integer gameID, String username, String playerColor);
}
