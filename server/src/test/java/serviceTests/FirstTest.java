package serviceTests;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import service.AdminService;
import service.ClientService;
import service.GameService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FirstTest {
    @Test
    public void clearEmptyData() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        AdminService s = new AdminService(mAccess);
        assertNotNull(s.delete());
    }

    @Test
    public void registerUsers() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        ClientService s = new ClientService(mAccess);

        AuthData a = s.register("CoderGuy", "123", "asdfasdf");
        AuthData b = s.register("CoderGirl", "123", "asdfasdf");
        assertNotNull(a);
        assertNotNull(b);
        assertEquals("CoderGuy", a.getUsername());
        assertEquals("CoderGirl", b.getUsername());
    }

    @Test
    public void registerExistingUser() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        ClientService s = new ClientService(mAccess);
        s.register("CoderGuy", "123", "emails");
        assertThrows(DataAccessException.class, () -> {
            s.register("CoderGuy", "456", "gmails");
        });
    }

    @Test
    public void clearPopulatedData() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        ClientService s = new ClientService(mAccess);
        AdminService a = new AdminService(mAccess);
        s.register("CoderGuy", "123", "asdfasdf");
        assertNotNull(a.delete());
    }

    @Test
    public void loginExistingUser() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        ClientService s = new ClientService(mAccess);
        s.register("CoderGuy", "123", "emails");
        assertNotNull(s.login("CoderGuy", "123"));
    }

    @Test
    public void loginAttemptNonRegisteredUser() {
        DataAccess mAccess = new MemoryDataAccess();
        ClientService s = new ClientService(mAccess);
        assertThrows(DataAccessException.class, () -> {s.login("CoderGuy", "123");});
    }

    @Test
    public void incorrectPassword() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        ClientService s = new ClientService(mAccess);
        s.register("CoderGuy", "123", "emails");
        assertThrows(DataAccessException.class, () -> {s.login("CoderGuy", "123456");});
    }

    @Test
    public void requestGamesForNewUser() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        GameService g = new GameService(mAccess);
        ClientService s = new ClientService(mAccess);
        AuthData authData = s.register("CoderGuy", "123", "emails");
        List<GameData> games = g.listGames(authData.getAuthToken());
        assertEquals(games.size(), 0);
    }

    @Test
    public void createGame() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        GameService g = new GameService(mAccess);
        ClientService s = new ClientService(mAccess);
        AuthData authData = s.register("CoderGuy", "123", "emails");
        Integer gameID = g.createGame(authData.getAuthToken(), "myNewGame");
        assertNotEquals(0, gameID);
    }

    @Test
    public void createGameUnauthorized() {
        DataAccess mAccess = new MemoryDataAccess();
        GameService g = new GameService(mAccess);
        assertThrows(DataAccessException.class, () -> {
            g.createGame("fakeToken", "myNewGame");
        });
    }

    @Test
    public void requestGamesForExperiencedUser() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        GameService g = new GameService(mAccess);
        ClientService s = new ClientService(mAccess);
        AuthData authData = s.register("CoderGuy", "123", "emails");
        g.createGame(authData.getAuthToken(), "myNewGame");
        List<GameData> games = g.listGames(authData.getAuthToken());
        assertEquals(games.size(), 1);
    }

    @Test
    public void requestGamesForUnregisteredUser() throws DataAccessException {
        DataAccess mAccess = new MemoryDataAccess();
        GameService g = new GameService(mAccess);
        assertThrows(DataAccessException.class, () -> {
            g.listGames("fake_auth_token");
        });
    }

}
