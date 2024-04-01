package service;

import chess.ChessGame;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import model.GameData;
import model.AuthData;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameService {
    /*
    GameService handles methods relating to games: list games, create game, join game
     */
    DataAccess dAccess;

    public GameService(DataAccess dAccess) {
        this.dAccess = dAccess;
    }

    public List<GameData> listGames(String authToken) throws DataAccessException, SQLException {
        if (dAccess.getAuth(authToken) == null) {
            throw new DataAccessException("Tried to retrieve an authData object for a username not in the system.");
        } else {
            String username = dAccess.getAuth(authToken).getUsername();
            return dAccess.getGames(username);
        }
    }

    public Integer createGame(String authToken, String gameName) throws DataAccessException, SQLException {
        if (dAccess.getAuth(authToken) == null) {
            throw new DataAccessException("Tried to retrieve an authData object for a username not in the system.");
        } else {
            String username = dAccess.getAuth(authToken).getUsername();
            return dAccess.newGame(username, gameName);
        }
    }

    public boolean joinGame(String authToken, String playerColor, int gameID) throws DataAccessException, SQLException {
        if (dAccess.getAuth(authToken) == null) {
            throw new DataAccessException("Tried to retrieve an authData object for a username not in the system.");
        } else if (dAccess.getGame(gameID) == null) {
            throw new DataAccessException("No game exists");
        } else {
            GameData game = dAccess.getGame(gameID);
            String username = dAccess.getAuth(authToken).getUsername();
            if (Objects.equals(playerColor, "WHITE")) {
                if (!Objects.equals(game.getWhiteUsername(), null) && !game.getWhiteUsername().equals(username)) {
                    throw new DataAccessException("Player position already taken");
                }
            }
            if (Objects.equals(playerColor, "BLACK")) {
                if (!Objects.equals(game.getBlackUsername(), null) && !game.getBlackUsername().equals(username)) {
                    throw new DataAccessException("Player position already taken");
                }
            }
            return dAccess.addPlayer(gameID, username, playerColor);
        }
    }

    public GameData getGame(String authToken, int gameID) throws DataAccessException, SQLException {
        if (dAccess.getAuth(authToken) == null) {
            throw new DataAccessException("Tried to retrieve an authData object for a username not in the system.");
        } else {
            return dAccess.getGame(gameID);
        }
    }

    public void setGame(int gameID, ChessGame g) throws DataAccessException {
        dAccess.setGame(gameID, g);
    }

}
