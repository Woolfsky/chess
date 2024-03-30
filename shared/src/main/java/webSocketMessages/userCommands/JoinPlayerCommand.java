package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    private int gameID;
    private ChessGame.TeamColor color;
    private String username;

    public JoinPlayerCommand(String authToken, CommandType type, String username, int gameID, ChessGame.TeamColor color) {
        super(authToken, type);
        this.gameID = gameID;
        this.color = color;
        this.username = username;
    }

    @Override
    public int getGameID() {
        return gameID;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    @Override
    public String getUsername() { return this.username; }

    @Override
    public String toString() {
        return "JoinPlayerCommand{" +
                "gameID=" + gameID +
                ", color=" + color +
                ", username='" + username + '\'' +
                '}';
    }
}
