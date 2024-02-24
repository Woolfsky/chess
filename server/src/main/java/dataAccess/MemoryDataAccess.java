package dataAccess;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {

    private HashMap<String, String> AuthDataMap = new HashMap<>();

    @Override
    public boolean deleteData() throws DataAccessException {
        System.out.printf("Within MemoryDataAccess deleteData method...");
        AuthDataMap.clear();
        if (AuthDataMap.isEmpty()) {
            System.out.printf("returning TRUE from within MemoryDataAccess deleteData method...");
            return true;
        } else {
            throw new DataAccessException("Tried to delete all data but failed");
        }
    }
}
