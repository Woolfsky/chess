package dataAccessTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AdminService;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUnitTests {

    @BeforeEach
    void clear() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        s.deleteData();
    }

    @Test
    public void deleteData() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        assertTrue(s.deleteData());
    }

    @Test
    public void createAuth() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        assertDoesNotThrow(() -> {
            s.createAuth("awesomeCoder123");
        });
    }

    @Test
    public void createBadAuth() {
        DataAccess s = new SQLDataAccess();
        assertThrows(RuntimeException.class, () -> {
            s.createAuth("\"");
        });
    }

    @Test
    public void getAuth() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        s.createAuth("awesomeCoder123");
        assertDoesNotThrow(() -> {
            s.getAuth("awesomeCoder123");
        });
    }

    @Test
    public void getBadAuth() throws DataAccessException, SQLException {
        DataAccess s = new SQLDataAccess();
        assertNull(s.getAuth("awesomeCoder123"));
    }

    @Test
    public void createUser() throws DataAccessException, SQLException {
        DataAccess s = new SQLDataAccess();
        assertDoesNotThrow(() -> {
            s.createUser("coolguy", "password", "myemail@coders.com");
        });
    }

    @Test
    public void createBadUser() {
        DataAccess s = new SQLDataAccess();
        assertThrows(RuntimeException.class, () -> {
            s.createUser("cool\"guy", "password", "myemail@coders.com");
        });
    }

    @Test
    public void getUser() throws DataAccessException, SQLException {
        DataAccess s = new SQLDataAccess();
        s.createUser("coolguy", "password", "myemail@coders.com");
        assertNotNull(s.getUser("coolguy"));
    }

    @Test
    public void getBadUser() throws DataAccessException, SQLException {
        DataAccess s = new SQLDataAccess();
        assertNull(s.getUser("coolgirl"));
    }

    @Test
    public void deleteAuth() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        s.createAuth("coolguy");
        assertTrue(s.deleteAuth("coolguy"));
    }

    @Test
    public void deleteBadAuth() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        assertDoesNotThrow(() -> {
            s.deleteAuth("coolguy");
        });
    }

    @Test
    public void newGame() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        assertInstanceOf(Integer.class, s.newGame("coolguy", "mycoolgame"));
    }

    @Test
    public void getGame() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        Integer i = s.newGame("coolguy", "mycoolgame");
        assertDoesNotThrow(() -> {
            s.getGame(i);
        });
    }

    @Test
    public void getBadGame() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        assertNull(s.getGame(3));
    }

    @Test
    public void listGames() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        s.newGame("coolguy", "mycoolgame");
        s.newGame("coolguy", "myOTHERcoolgame");
        s.newGame("coolgal", "mycoolgame2");
        s.newGame("coolbaby", "mycoolgame3");
        List<GameData> l = s.getGames("coolguy");
        assertNotNull(l);
    }

    @Test
    public void listNoGames() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        List<GameData> l = s.getGames("coolguy");
        assertEquals(l.size(), 0);
    }

    @Test
    public void addPlayer() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        Integer i = s.newGame("coolguy", "mycoolgame");
        assertTrue(s.addPlayer(i, "coolguy", "BLACK"));
    }

    @Test
    public void addBadPlayerColor() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        Integer i = s.newGame("coolguy", "mycoolgame");
        assertFalse(s.addPlayer(i, "coolguy", "BLUE"));
    }

    @Test
    public void addPlayerToBadGame() throws DataAccessException {
        DataAccess s = new SQLDataAccess();
        assertThrows(DataAccessException.class, () -> {
            s.addPlayer(2, "coolguy", "WHITE");
        });
    }

}
