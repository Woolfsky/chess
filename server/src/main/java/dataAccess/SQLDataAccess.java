package dataAccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT username, password, email FROM gameUser WHERE username = '" + username + "';";
        return executeUserQuery(statement);
    };

    public void createUser(String username, String password, String email) throws DataAccessException {
        String statement = "INSERT INTO gameUser (username, password, email) VALUES ('" + username + "', '" + password + "', '" + email + "');";
        executeUpdate(statement);
    };

    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        String statement = "INSERT INTO auth (authToken, username) VALUES ('" + authToken + "', '" + username + "');";
        executeUpdate(statement);
        return new AuthData(authToken, username);
    };

    public AuthData getAuth(String username) throws DataAccessException, SQLException {
        String statement = "SELECT authToken, username FROM auth WHERE username = '" + username + "';";
        return executeAuthQuery(statement);
    };

    public boolean deleteAuth(String username) throws DataAccessException {
        String s = "DELETE FROM auth WHERE username = '" + username + "';";
        executeUpdate(s);
        return true;
    };

    public List<GameData> getGames(String username) throws DataAccessException {
        String s = "SELECT * FROM game;";
        return executeListGamesQuery(s);
    };

    public Integer newGame(String username, String gameName) throws DataAccessException {
        String s = "INSERT INTO game (gameName) VALUES ('" + gameName + "');";
        executeUpdate(s);
        String s2 = "SELECT gameID FROM game WHERE gameName = '" + gameName + "';";
        GameData g = executeGameQuery(s2);
        return g.getGameID();
    };

    public GameData getGame(Integer gameID) throws DataAccessException {
        String s = "SELECT gameID, gameName, whiteUsername, blackUsername, chessGame FROM game WHERE gameID = " + gameID + ";";
        return executeGameQuery(s);
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

    public AuthData executeAuthQuery(String statement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    return readAuthData(rs);
                }
            }
        } catch (DataAccessException e) {
            System.out.printf("Error in trying to delete SQL data: " + e.getMessage());
            throw new DataAccessException("Unable to delete data");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserData executeUserQuery(String statement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    return readUserData(rs);
                }
            }
        } catch (DataAccessException e) {
            System.out.printf("Error in trying to delete SQL data: " + e.getMessage());
            throw new DataAccessException("Unable to delete data");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData executeGameQuery(String statement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    return readGameData(rs);
                }
            }
        } catch (DataAccessException e) {
            System.out.printf("Error in trying to delete SQL data: " + e.getMessage());
            throw new DataAccessException("Unable to delete data");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GameData> executeListGamesQuery(String statement) throws DataAccessException {
        var resultList = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        resultList.add(readGameDataNoNext(rs));
                    }
                }
            }
        } catch (DataAccessException e) {
            System.out.printf("Error in trying to delete SQL data: " + e.getMessage());
            throw new DataAccessException("Unable to delete data");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    public AuthData readAuthData(ResultSet rs) throws SQLException {
        if (rs.next()) {
            String authToken = rs.getString("authToken");
            String username = rs.getString("username");
            return new AuthData(authToken, username);
        } else {
            return null;
        }
    }

    public UserData readUserData(ResultSet rs) throws SQLException {
        if (rs.next()) {
            String username = rs.getString("username");
            String hashedPassword = rs.getString("password");
            String email = rs.getString("email");
            return new UserData(username, hashedPassword, email);
        } else {
            return null;
        }
    }

    public GameData readGameData(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Integer gameID = rs.getInt("gameID");
            String gameName = null;
            String whiteUsername = null;
            String blackUsername = null;
            String chessGame = null;
            try { gameName = rs.getString("gameName"); } catch (SQLException s) {}
            try { whiteUsername = rs.getString("whiteUsername"); } catch (SQLException s) {}
            try { blackUsername = rs.getString("blackUsername"); } catch (SQLException s) {}
            try { chessGame = rs.getString("chessGame"); } catch (SQLException s) {}
            return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
        } else {
            return null;
        }
    }

    public GameData readGameDataNoNext(ResultSet rs) throws SQLException {
        Integer gameID = rs.getInt("gameID");
        String gameName = null;
        String whiteUsername = null;
        String blackUsername = null;
        String chessGame = null;
        try { gameName = rs.getString("gameName"); } catch (SQLException s) {}
        try { whiteUsername = rs.getString("whiteUsername"); } catch (SQLException s) {}
        try { blackUsername = rs.getString("blackUsername"); } catch (SQLException s) {}
        try { chessGame = rs.getString("chessGame"); } catch (SQLException s) {}
        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
    }


}
