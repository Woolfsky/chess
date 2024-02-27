package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, String game) { // change game parameter's type of ChessGame in next phase
    public String getWhiteUsername() {
        return whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
}
