package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private int gameID;

    public ResignCommand(String authToken, CommandType type, int gameID) {
        super(authToken, type);
        this.gameID = gameID;
    }

    public int getGameID() { return this.gameID; }

}
