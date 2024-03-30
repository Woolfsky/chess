package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private int gameID;
    private String username;

    public JoinObserverCommand(String authToken, UserGameCommand.CommandType type, String username, int gameID) {
        super(authToken, type);
        this.gameID = gameID;
        this.username = username;
    }

    @Override
    public int getGameID() {
        return gameID;
    }

    @Override
    public String getUsername() { return this.username; }
}
