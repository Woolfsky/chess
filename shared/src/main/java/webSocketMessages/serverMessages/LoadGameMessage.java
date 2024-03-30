package webSocketMessages.serverMessages;

public class LoadGameMessage extends ServerMessage {
    int gameID;
    public LoadGameMessage(ServerMessageType type, int gameID) {
        super(type);
        this.gameID = gameID;
    }
    public int getGameID() {
        return this.gameID;
    }

}
