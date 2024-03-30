package web;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocketCommunicator extends Endpoint {
    SocketListener listener;
    public Session session;

    public WebSocketCommunicator(SocketListener socketListener) throws Exception {
        this.listener = socketListener;

        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
                    LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                    listener.updateRenderGame(loadGameMessage.getGameID());
                }
                if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
                    NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                    System.out.println(notificationMessage.getMessage());
                }
            }
        });
    }

    public void joinPlayer(Integer gameID, ChessGame.TeamColor playerColor, String username, String authToken) throws IOException, EncodeException {
        JoinPlayerCommand joinCommand = new JoinPlayerCommand(authToken, UserGameCommand.CommandType.JOIN_PLAYER, username, gameID, playerColor);
        Gson gson = new Gson();
        String jsonCommand = gson.toJson(joinCommand);
        this.session.getBasicRemote().sendText(jsonCommand);
    }

    public void joinObserver(Integer gameID) {}

    public void makeMove(Integer gameID, ChessMove move) {}

    public void leave(Integer gameID) {}

    public void resign(Integer gameID) {}



    public interface SocketListener {
        void updateRenderGame(int gameID);
        void notify(UserGameCommand gameCommand);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
