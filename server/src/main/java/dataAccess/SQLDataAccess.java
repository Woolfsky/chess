package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLDataAccess implements DataAccess {

    public SQLDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
        addTables();
    }

    public boolean deleteData() throws DataAccessException {
        List<String> statements = new ArrayList<>();
        statements.add("DELETE FROM auth;");
        statements.add("DELETE FROM gameUser;");
        statements.add("DELETE FROM game;");
        for (String s : statements) {
            executeUpdate(s);
        }
        return true;
    };

    public UserData getUser(String username) {
        return null;
    };

    public void createUser(String username, String password, String email) {};

    public AuthData createAuth(String username) {
        return null;
    };

    public AuthData getAuth(String username) {
        return null;
    };

    public boolean deleteAuth(String username) {
        return false;
    };

    public List<GameData> getGames(String username) {
        return null;
    };

    public Integer newGame(String username, String gameName) {
        return null;
    };

    public GameData getGame(Integer gameID) {
        return null;
    };

    public boolean addPlayer(Integer gameID, String username, String playerColor) {
        return false;
    };

    private final String[] dbCreationStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `authToken` TEXT NOT NULL,
              `username` TEXT NOT NULL,
              PRIMARY KEY (`authToken`(255))
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS gameUser (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` TEXT NOT NULL,
              `password` TEXT NOT NULL,
              `email` TEXT NOT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` TEXT NOT NULL,
              `whiteUsername` TEXT,
              `blackUsername` TEXT,
              `chessGame` TEXT,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public void addTables() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : dbCreationStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    public boolean executeUpdate(String statement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (DataAccessException e) {
            System.out.printf("Error in trying to delete SQL data: " + e.getMessage());
            throw new DataAccessException("Unable to delete data");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
