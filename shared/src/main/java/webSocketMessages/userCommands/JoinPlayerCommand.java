package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    int gameID;
    ChessGame.TeamColor color;
    String username;


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
}
