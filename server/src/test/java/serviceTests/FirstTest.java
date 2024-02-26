package serviceTests;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.AuthData;
import org.junit.jupiter.api.*;
import service.AdminService;
import service.ClientService;

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

}
