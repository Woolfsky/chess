package server;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.ArrayList;
import java.util.List;

@WebSocket
public class WSServer {
    List<Session> sessionList = new ArrayList<>();

    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        // convert the message into a JoinPlayer object (or other commands) and then use logic
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        UserGameCommand.CommandType commandType = command.getCommandType();

        // command type logic...



        session.getRemote().sendString("WSServer response: " + message);
    }

    public void loadGame(ChessGame game) {}

    public void error(String errorMessage) {}

    public void notification(String message) {

    }
}