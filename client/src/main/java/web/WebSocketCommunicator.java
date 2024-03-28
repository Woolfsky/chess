package web;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

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
                System.out.println("received a response from the server: " + message);
                UserGameCommand gameCommand = new Gson().fromJson(message, UserGameCommand.class);
                listener.notify(gameCommand);
            }
        });
    }

    public void joinPlayer(Integer gameID, ChessGame.TeamColor playerColor) throws IOException {
        System.out.print("JOINED!!!! from WebSocketCommunicator\n");
        this.session.getBasicRemote().sendText("JOINED!!!! from WebSocketCommunicator");
    }

    public void joinObserver(Integer gameID) {}

    public void makeMove(Integer gameID, ChessMove move) {}

    public void leave(Integer gameID) {}

    public void resign(Integer gameID) {}



    public interface SocketListener {
        void updateGame(ChessGame game);
        void notify(UserGameCommand gameCommand);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
