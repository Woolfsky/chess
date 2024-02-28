package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import model.UserData;
import model.AuthData;

public class ClientService {
    /*
    ClientService handles methods relating to the individual client: registration, login, and logout
     */
    DataAccess dAccess;

    public ClientService(DataAccess dAccess) {
        this.dAccess = dAccess;
    }

    public AuthData register(String username, String password, String email) throws DataAccessException {
        if (dAccess.getUser(username) == null) {
            dAccess.createUser(username, password, email);
            AuthData authData = dAccess.createAuth(username);
            return authData;
        } else {
            throw new DataAccessException("Error: already taken");
        }
    }

    public AuthData login(String username, String password) throws DataAccessException {
        if (dAccess.getUser(username) != null) {
            UserData userData = dAccess.getUser(username);
            if (userData.getUsername().equals(username) && userData.getPassword().equals(password)) {
                AuthData authData = dAccess.createAuth(username);
                return authData;
            } else {
                // password wasn't correct
                throw new DataAccessException("Error: unauthorized (incorrect password)");
            }
        } else {
            // user wasn't in the system
            throw new DataAccessException("Error: unauthorized (user not in the system)");
        }
    }

    public boolean logout(String authToken) throws DataAccessException {
        if (dAccess.getAuth(authToken) == null) {
            throw new DataAccessException("Tried to retrieve an authData object for a username not in the system.");
        } else {
            return dAccess.deleteAuth(authToken);
        }
    }
}