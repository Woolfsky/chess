package server;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WSServer {
    Map<String, Session> sessionList = new HashMap<>();

    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        JoinPlayerCommand joinPlayerCommand;
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        UserGameCommand.CommandType commandType = command.getCommandType();

        if (commandType.equals(UserGameCommand.CommandType.JOIN_PLAYER)) {
            joinPlayerCommand = gson.fromJson(message, JoinPlayerCommand.class);
            joinCommand(joinPlayerCommand, session);
        }
    }

    public void joinCommand(JoinPlayerCommand command, Session session) throws IOException {

        // FIRST, send a load game!!!!
        loadGame("Load the game:)", session);

        String authToken = command.getAuthString();
        sessionList.put(authToken, session);
        String m = command.getUsername() + " joined game " + command.getGameID() + " as " + command.getTeamColor();

        for (String i : sessionList.keySet()) {
                if (i != authToken) {
                    notification(sessionList.get(i), m);
                }
        }
    }



    public void loadGame(String game, Session session) throws IOException {
        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(loadGameMessage);
        session.getRemote().sendString(jsonMessage);
    }

    public void error(String errorMessage) {}

    public void notification(Session session, String message) throws IOException {
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(notificationMessage);
        session.getRemote().sendString(jsonMessage);
    }
}