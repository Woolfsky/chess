package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, String game) {
    public String getWhiteUsername() {
        return whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
    public int getGameID() { return gameID; }
    public String getGameName() {
        return gameName;
    }
    public String getGame() { return game;}

    public GameData addPlayerToRecord(String playerColor, String playerUsername) {
        if (playerColor == null) {
            return this;
        } else if (playerColor.equals("WHITE")) {
            return new GameData(this.getGameID(), playerUsername, this.getBlackUsername(), this.getGameName(), this.getGame());
        } else if (playerColor.equals("BLACK")) {
            return new GameData(this.getGameID(), this.getWhiteUsername(), playerUsername, this.getGameName(), this.getGame());
        }
        return null;
    }
}
