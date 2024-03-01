package serviceTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import org.junit.jupiter.api.Test;
import service.AdminService;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUnitTests {
    @Test
    public void deleteEmptyData() throws DataAccessException {
        DataAccess sAccess = new SQLDataAccess();
        AdminService a = new AdminService(sAccess);
        assertTrue(a.delete());
    }
}
