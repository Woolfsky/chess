package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DataAccess {
    boolean deleteData() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void createUser(String username, String password, String email) throws DataAccessException;

    AuthData createAuth(String username) throws DataAccessException;

    AuthData getAuth(String username) throws DataAccessException, SQLException;

    boolean deleteAuth(String username) throws DataAccessException;

    List<GameData> getGames(String username);

    Integer newGame(String username, String gameName);

    GameData getGame(Integer gameID);

    boolean addPlayer(Integer gameID, String username, String playerColor);
}
