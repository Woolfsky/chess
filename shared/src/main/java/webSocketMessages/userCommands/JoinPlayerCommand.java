package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    private int gameID;
    private ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(String authToken, CommandType type, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken, type);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return this.playerColor;
    }


    @Override
    public String toString() {
        return "JoinPlayerCommand{" +
                "gameID=" + gameID +
                ", playerColor=" + playerColor +
                '}';
    }
}
