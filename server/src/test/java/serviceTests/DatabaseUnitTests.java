package serviceTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import org.junit.jupiter.api.Test;
import service.AdminService;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUnitTests {
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
}
