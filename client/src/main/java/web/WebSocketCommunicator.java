package web;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.rmi.dgc.Lease;

public class WebSocketCommunicator extends Endpoint {
    private SocketListener listener;
    public Session session;
    private Gson gson = new Gson();;

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
                    listener.updateRenderGame(loadGameMessage.getGame());
                }
                if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
                    NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                    System.out.println(notificationMessage.getMessage());
                }
                if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
                    ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                    System.out.println(errorMessage.getErrorMessage());
                }
            }
        });
    }

    public void joinPlayer(Integer gameID, ChessGame.TeamColor playerColor, String authToken) throws IOException {
        JoinPlayerCommand joinCommand = new JoinPlayerCommand(authToken, UserGameCommand.CommandType.JOIN_PLAYER, gameID, playerColor);
        String jsonCommand = gson.toJson(joinCommand);
        this.session.getBasicRemote().sendText(jsonCommand);
    }

    public void joinObserver(Integer gameID, String authToken) throws IOException {
        JoinObserverCommand joinObserverCommand = new JoinObserverCommand(authToken, UserGameCommand.CommandType.JOIN_OBSERVER, gameID);
        String jsonCommand = gson.toJson(joinObserverCommand);
        this.session.getBasicRemote().sendText(jsonCommand);
    }

    public void makeMove(Integer gameID, ChessMove move, String authToken) throws IOException {
        MakeMoveCommand makeMoveCommand = new MakeMoveCommand(authToken, UserGameCommand.CommandType.MAKE_MOVE, gameID, move);
        String jsonCommand = gson.toJson(makeMoveCommand);
        this.session.getBasicRemote().sendText(jsonCommand);
    }

    public void leave(Integer gameID, String authToken) throws IOException {
        LeaveCommand leaveCommand = new LeaveCommand(authToken, UserGameCommand.CommandType.LEAVE, gameID);
        String jsonCommand = gson.toJson(leaveCommand);
        this.session.getBasicRemote().sendText(jsonCommand);
    }

    public void resign(Integer gameID, String authToken) throws IOException {
        ResignCommand resignCommand = new ResignCommand(authToken, UserGameCommand.CommandType.RESIGN, gameID);
        String jsonCommand = gson.toJson(resignCommand);
        this.session.getBasicRemote().sendText(jsonCommand);
    }

    public interface SocketListener {
        void updateRenderGame(ChessGame game);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
