package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {

    String message;

    public NotificationMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
