package dataAccess;

import model.AuthData;
import model.UserData;

public interface DataAccess {
    boolean deleteData() throws DataAccessException;

    UserData getUser(String username);

    void createUser(String username, String password, String email);

    AuthData createAuth(String username);
}
