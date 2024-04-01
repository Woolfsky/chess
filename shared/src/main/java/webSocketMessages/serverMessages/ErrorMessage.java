package webSocketMessages.serverMessages;

import chess.ChessGame;

public class ErrorMessage extends ServerMessage {
    String errorMessage;
    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {
        return this.errorMessage;
    }

}