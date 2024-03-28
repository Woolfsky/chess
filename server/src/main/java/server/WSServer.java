package server;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

@WebSocket
public class WSServer {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.print("Message received from the client!\n");
        session.getRemote().sendString("WSServer response: " + message);
    }

    @OnWebSocketError
    public void onWebSocketError(Session session, Throwable throwable) {
        System.out.print("websocket error: ");
    }

    public void loadGame(ChessGame game) {}

    public void error(String errorMessage) {}

    public void notification(String message) {}
}