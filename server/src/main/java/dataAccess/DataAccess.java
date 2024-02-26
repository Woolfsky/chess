package dataAccess;

import model.AuthData;
import model.UserData;

public interface DataAccess {
    boolean deleteData() throws DataAccessException; // fix this, this shouldn't throw anything, the service endpoint should

    UserData getUser(String username);

    void createUser(String username, String password, String email);

    AuthData createAuth(String username);

    AuthData getAuth(String username);

    boolean deleteAuth(String username);
}
