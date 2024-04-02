package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.ClientService;
import service.GameService;
import spark.Spark;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WSServer {
    private Map<Integer, Map<String, Session>> allSessions = new HashMap<>();
    private final GameService gService;
    private final ClientService cService;
    private final Gson gson = new Gson();

    public WSServer(GameService gService, ClientService cService) {
        this.gService = gService;
        this.cService = cService;
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
        ResignCommand resignCommand;
        LeaveCommand leaveCommand;

        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        UserGameCommand.CommandType commandType = command.getCommandType();

        if (commandType.equals(UserGameCommand.CommandType.JOIN_PLAYER)) {
            joinPlayerCommand = gson.fromJson(message, JoinPlayerCommand.class);
            joinPlayerCommand(joinPlayerCommand, session);
        }
        if (commandType.equals(UserGameCommand.CommandType.JOIN_OBSERVER)) {
            joinObserverCommand = gson.fromJson(message, JoinObserverCommand.class);
            joinObserverCommand(joinObserverCommand, session);
        }
        if (commandType.equals(UserGameCommand.CommandType.MAKE_MOVE)) {
            makeMoveCommand = gson.fromJson(message, MakeMoveCommand.class);
            makeMoveCommand(makeMoveCommand, session);
        }
        if (commandType.equals(UserGameCommand.CommandType.RESIGN)) {
            resignCommand = gson.fromJson(message, ResignCommand.class);
            resignCommand(resignCommand, session);
        }
        if (commandType.equals(UserGameCommand.CommandType.LEAVE)) {
            leaveCommand = gson.fromJson(message, LeaveCommand.class);
            leaveCommand(leaveCommand, session);
        }
    }

    @OnWebSocketError public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

//    @OnWebSocketClose public void onClose(Session session, int statusCode, String reason) {
//        sessionList.values().removeIf(value -> value.equals(session));
//    }

    public void joinPlayerCommand(JoinPlayerCommand command, Session session) throws IOException, SQLException, DataAccessException, InvalidMoveException {
        try {
            verifyGameExists(command);
            verifyNotEmptyGame(command);
            verifyAuth(command);
            verifyJoinedGame(command);
            String username = cService.getUsername(command.getAuthString());
            GameData gameData = gService.getGame(command.getAuthString(), command.getGameID());
            verifyNotStealingSpot(session, command, gameData, username);

            loadGame(command.getAuthString(), command.getGameID(), session);

            String authToken = command.getAuthString();

            if (allSessions.containsKey(command.getGameID())) {
                Map<String, Session> innerMap = allSessions.get(command.getGameID());
                innerMap.put(authToken, session);
            } else {
                Map<String, Session> innerMap = new HashMap<>();
                innerMap.put(authToken, session);
                allSessions.put(command.getGameID(), innerMap);
            }

            String m = username + " joined game " + command.getGameID() + " as " + command.getPlayerColor();

            Map<String, Session> sessionList = allSessions.get(command.getGameID());
            for (String i : sessionList.keySet()) {
                if (!i.equals(authToken)) {
                    notification(sessionList.get(i), m);
                }
            }
        } catch (Exception e) {
            error(session, e.getMessage());
        }

    }

    public void joinObserverCommand(JoinObserverCommand command, Session session) throws IOException, SQLException, DataAccessException {
        try {
            verifyJoinObserver(command);
            String username = cService.getUsername(command.getAuthString());
            loadGame(command.getAuthString(), command.getGameID(), session);

            String authToken = command.getAuthString();
            if (allSessions.containsKey(command.getGameID())) {
                Map<String, Session> innerMap = allSessions.get(command.getGameID());
                innerMap.put(authToken, session);
            } else {
                Map<String, Session> innerMap = new HashMap<>();
                innerMap.put(authToken, session);
                allSessions.put(command.getGameID(), innerMap);
            }
            String m = username + " joined game " + command.getGameID() + " as an observer";

            Map<String, Session> sessionList = allSessions.get(command.getGameID());
            for (String i : sessionList.keySet()) {
                if (!i.equals(authToken)) {
                    notification(sessionList.get(i), m);
                }
            }
        } catch (Exception e) {
            error(session, e.getMessage());
        }

    }

    public void makeMoveCommand(MakeMoveCommand command, Session session) throws IOException, SQLException, DataAccessException {
        try {
            GameData gameData = gService.getGame(command.getAuthString(), command.getGameID());
            ChessGame g = gson.fromJson(gameData.getGame(), ChessGame.class);
            String username = cService.getUsername(command.getAuthString());
            verifyCorrectMover(gameData, g, username);
            verifyGameNotOver(g);
            g.makeMove(command.getMove());
            this.gService.setGame(gameData.getGameID(), g);

            String authToken = command.getAuthString();
            Map<String, Session> sessionList = allSessions.get(command.getGameID());
            sessionList.put(authToken, session);

            String m = username + " made a move from " + command.getMove().getStartPosition().toString() + " to " + command.getMove().getEndPosition().toString();

            for (String s : sessionList.keySet()) {
                loadGame(s, gameData.getGameID(), sessionList.get(s));
            }

            for (String i : sessionList.keySet()) {
                if (!i.equals(authToken)) {
                    notification(sessionList.get(i), m);
                }
            }

            endGameIfNeeded(g, gameData);
        } catch (Exception e) {
            error(session, e.getMessage());
        }
    }

    public void resignCommand(ResignCommand command, Session session) throws IOException {
        try {
            GameData gameData = gService.getGame(command.getAuthString(), command.getGameID());
            ChessGame g = gson.fromJson(gameData.getGame(), ChessGame.class);
            verifyGameNotOver(g);
            verifyNotObserver(command, gameData);
            g.setGameOverStatus(true);
            this.gService.setGame(gameData.getGameID(), g);

            String username = cService.getUsername(command.getAuthString());
            String m = username + " resigned from the game";

            Map<String, Session> sessionList = allSessions.get(command.getGameID());
            for (String i : sessionList.keySet()) {
                notification(sessionList.get(i), m);
            }

        } catch (Exception e) {
            error(session, e.getMessage());
        }
    }

    public void leaveCommand(LeaveCommand command, Session session) throws IOException {
        try {
            String username = cService.getUsername(command.getAuthString());
            gService.removePlayer(command.getGameID(), command.getAuthString(), username);

            String m = username + " has left the game";

            Map<String, Session> sessionList = allSessions.get(command.getGameID());
            sessionList.remove(command.getAuthString());

            for (String i : sessionList.keySet()) {
                if (!i.equals(command.getAuthString())) {
                    notification(sessionList.get(i), m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            error(session, e.getMessage());
        }
    }

    public void loadGame(String authToken, int gameID, Session session) throws IOException, SQLException, DataAccessException, InvalidMoveException {
        GameData gameData = gService.getGame(authToken, gameID);
        ChessGame g = gson.fromJson(gameData.getGame(), ChessGame.class);

        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, g);
        String jsonMessage = gson.toJson(loadGameMessage);
        session.getRemote().sendString(jsonMessage);
    }

    public void error(Session session, String errorMessage) throws IOException {
        ErrorMessage errorMessage1 = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
        String jsonMessage = gson.toJson(errorMessage1);
        session.getRemote().sendString(jsonMessage);
    }

    public void notification(Session session, String message) throws IOException {
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        String jsonMessage = gson.toJson(notificationMessage);
        session.getRemote().sendString(jsonMessage);
    }


    public void verifyCorrectMover(GameData gData, ChessGame game, String username) throws InvalidMoveException, SQLException, DataAccessException {
        if (game.getTeamTurn() == ChessGame.TeamColor.WHITE) {
            if (!username.equals(gData.getWhiteUsername())) {
                throw new InvalidMoveException("Invalid move, not your turn");
            }
        } else if (game.getTeamTurn() == ChessGame.TeamColor.BLACK) {
            if (!username.equals(gData.getBlackUsername())) {
                throw new InvalidMoveException("Invalid move, not your turn");
            }
        }
    }

    public void endGameIfNeeded(ChessGame g, GameData gameData) throws DataAccessException {
        if (g.isInCheckmate(ChessGame.TeamColor.WHITE) || g.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            g.setGameOverStatus(true);
            this.gService.setGame(gameData.getGameID(), g);
        }
    }

    public void verifyNotObserver(ResignCommand command, GameData gameData) throws SQLException, DataAccessException, InvalidMoveException {
        String commandingUsername = cService.getUsername(command.getAuthString());
        if (!commandingUsername.equals(gameData.getWhiteUsername()) && !commandingUsername.equals(gameData.getBlackUsername())) {
            throw new InvalidMoveException("Observer cannot resign");
        }
    }

    public void verifyNotStealingSpot(Session s, JoinPlayerCommand command, GameData gameData, String username) throws InvalidMoveException, IOException {
        if (command.getPlayerColor() != null) {
            if (command.getPlayerColor().equals(ChessGame.TeamColor.WHITE)) {
                if (gameData.getWhiteUsername() != null && !gameData.getWhiteUsername().equals(username)) {
                    throw new InvalidMoveException("Can't take another player's spot in the game");
                }
            } else if (command.getPlayerColor().equals(ChessGame.TeamColor.BLACK)) {
                if (gameData.getBlackUsername() != null && !gameData.getBlackUsername().equals(username)) {
                    throw new InvalidMoveException("Can't take another player's spot in the game");
                }
            }
        } else {
            throw new InvalidMoveException("Must enter a team color");
        }

    }

    public void verifyGameNotOver(ChessGame game) throws InvalidMoveException {
        if (game.getGameOverStatus()) {
            throw new InvalidMoveException("Game is over");
        }
    }

    public void verifyGameExists(JoinPlayerCommand command) throws SQLException, DataAccessException, InvalidMoveException {
        if (gService.getGame(command.getAuthString(), command.getGameID()) == null) {
            throw new InvalidMoveException("Game does not exist");
        }
    }

    public void verifyJoinedGame(JoinPlayerCommand command) throws SQLException, DataAccessException, InvalidMoveException {
        GameData gameData = gService.getGame(command.getAuthString(), command.getGameID());
        String username = cService.getUsername(command.getAuthString());
        if (command.getPlayerColor().equals(ChessGame.TeamColor.WHITE)) {
            if (!gameData.getWhiteUsername().equals(username)) {
                throw new InvalidMoveException("Player never joined game");
            }
        } else if (command.getPlayerColor().equals(ChessGame.TeamColor.BLACK)) {
            if (!gameData.getBlackUsername().equals(username)) {
                throw new InvalidMoveException("Player never joined game");
            }
        }

    }

    public void verifyNotEmptyGame(JoinPlayerCommand command) throws InvalidMoveException {
        if (command.getPlayerColor() == null) {
            throw new InvalidMoveException("Must enter a team color");
        }
    }

    public void verifyAuth(JoinPlayerCommand command) throws SQLException, DataAccessException, InvalidMoveException {
        if (cService.getUsername(command.getAuthString()) == null) {
            throw new InvalidMoveException("Not authorized");
        }
    }

    public void verifyJoinObserver(JoinObserverCommand command) throws InvalidMoveException, SQLException, DataAccessException {
        if (cService.getUsername(command.getAuthString()) == null) {
            throw new InvalidMoveException("Not authorized");
        }
        if (gService.getGame(command.getAuthString(), command.getGameID()) == null) {
            throw new InvalidMoveException("Game does not exist");
        }

    }
}