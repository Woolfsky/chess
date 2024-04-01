package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private int gameID;

    public JoinObserverCommand(String authToken, UserGameCommand.CommandType type, int gameID) {
        super(authToken, type);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

}
