package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.GameService;
import spark.Spark;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WSServer {
    private Map<String, Session> sessionList = new HashMap<>();
    private GameService gService;

    public WSServer(GameService gService) {
        this.gService = gService;
    }

    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        JoinPlayerCommand joinPlayerCommand;
        JoinObserverCommand joinObserverCommand;
        MakeMoveCommand makeMoveCommand;
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        UserGameCommand.CommandType commandType = command.getCommandType();

        if (commandType.equals(UserGameCommand.CommandType.JOIN_PLAYER)) {
            joinPlayerCommand = gson.fromJson(message, JoinPlayerCommand.class);
            joinCommand(joinPlayerCommand, session);
        }
        if (commandType.equals(UserGameCommand.CommandType.JOIN_OBSERVER)) {
            joinObserverCommand = gson.fromJson(message, JoinObserverCommand.class);
            joinObserverCommand(joinObserverCommand, session);
        }
        if (commandType.equals(UserGameCommand.CommandType.MAKE_MOVE)) {
            makeMoveCommand = gson.fromJson(message, MakeMoveCommand.class);
            makeMoveCommand(makeMoveCommand, session);
        }
    }

    public void joinCommand(JoinPlayerCommand command, Session session) throws IOException, SQLException, DataAccessException {
        loadGame(command.getAuthString(), command.getGameID(), session);

        String authToken = command.getAuthString();
        sessionList.put(authToken, session);
        String m = command.getUsername() + " joined game " + command.getGameID() + " as " + command.getTeamColor();

        for (String i : sessionList.keySet()) {
                if (!i.equals(authToken)) {
                    notification(sessionList.get(i), m);
                }
        }
    }

    public void joinObserverCommand(JoinObserverCommand command, Session session) throws IOException, SQLException, DataAccessException {
        loadGame(command.getAuthString(), command.getGameID(), session);

        String authToken = command.getAuthString();
        sessionList.put(authToken, session);
        String m = command.getUsername() + " joined game " + command.getGameID() + " as an observer";

        for (String i : sessionList.keySet()) {
            if (!i.equals(authToken)) {
                notification(sessionList.get(i), m);
            }
        }
    }

    public void makeMoveCommand(MakeMoveCommand command, Session session) throws IOException, SQLException, DataAccessException {
        loadGame(command.getAuthString(), command.getGameID(), session);

        Gson gson = new Gson();
        GameData gameData = gService.getGame(command.getAuthToken(), command.getGameID());
        ChessGame g = gson.fromJson(gameData.getGame(), ChessGame.class);
        try {
            g.makeMove(command.getMove());
            String authToken = command.getAuthString();
            sessionList.put(authToken, session);
            String m = command.getUsername() + " made a move from " + command.getMove().getStartPosition().toString() + " to " + command.getMove().getEndPosition().toString();

            // send out the LOAD_GAMEs to everyone here.....

            for (String i : sessionList.keySet()) {
                if (!i.equals(authToken)) {
                    notification(sessionList.get(i), m);
                }
            }
        } catch (InvalidMoveException e) {
            // create an error message and send that to the root client.......

        }
    }

    public void loadGame(String authToken, int gameID, Session session) throws IOException, SQLException, DataAccessException {
        Gson gson = new Gson();
        GameData gameData = gService.getGame(authToken, gameID);
        ChessGame g = gson.fromJson(gameData.getGame(), ChessGame.class);

        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, g);
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