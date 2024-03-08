package serviceTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AdminService;

import java.sql.SQLException;

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

}
