package web;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import ui.ChessRendering;
import webSocketMessages.userCommands.UserGameCommand;

import javax.imageio.IIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler implements WebSocketCommunicator.SocketListener {
    private String[] parameters;
    private String state;
    public ServerFacade facade;
    private AuthData authData;
    private ChessGame game = new ChessGame();
    private ChessGame.TeamColor color;
    private WebSocketCommunicator ws;
    private int gameID;

    public CommandHandler() {}

    public void setParametersState(String[] p, String s) {
        parameters = p;
        state = s;
        facade = new ServerFacade(8080);
    }

    public String executeReturnState() {
        return switch (state) {
            case "LOGGED_OUT: Not playing" -> loggedOutNotPlaying();
            case "LOGGED_IN: Not playing" -> loggedInNotPlaying();
            case "LOGGED_IN: Playing" -> loggedInPlaying();
            case "LOGGED_IN: Observing" -> loggedInObserving();
            default -> "Error: state is messed up, sorry...try restarting your terminal";
        };
    }

    public String loggedOutNotPlaying() {
        if (parameters[0].equals("register")) {
            try {
                AuthData auth = facade.register(parameters[1], parameters[2], parameters[3]);
                authData = auth;
                System.out.println("New user registered as " + parameters[1]);
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
            }

        }
        if (parameters[0].equals("login")) {
            try {
                AuthData auth = facade.login(parameters[1], parameters[2]);
                authData = auth;
                System.out.println("Logged in as " + parameters[1]);
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
            }
        }
        if (parameters[0].equals("help")) {
            System.out.print("    register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n");
            System.out.print("    login <USERNAME> <PASSWORD> - to play chess\n");
            System.out.print("    quit - to quit playing chess\n");
            System.out.print("    help - to list possible commands\n");
            return "LOGGED_OUT: Not playing";
        }
        if (parameters[0].equals("quit")) {
            return "QUIT";
        }
        System.out.print("Invalid command. Choose one of the following:\n");
        System.out.print("    register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n");
        System.out.print("    login <USERNAME> <PASSWORD> - to play chess\n");
        System.out.print("    quit - to quit playing chess\n");
        System.out.print("    help - to list possible commands\n");
        return state;
    }

    public String loggedInNotPlaying() {
        if (parameters[0].equals("help")) {
            System.out.print("    create <NAME> - to create an game\n");
            System.out.print("    list - to list games\n");
            System.out.print("    join <ID> [WHITE|BLACK|<empty>] - to join a game\n");
            System.out.print("    observe <ID> - to observe a game\n");
            System.out.print("    logout - when you are done\n");
            System.out.print("    quit - to quit playing chess\n");
            System.out.print("    help - to list possible commands\n");
            return "LOGGED_IN: Not playing";
        }
        if (parameters[0].equals("quit")) {
            return "QUIT";
        }
        if (parameters[0].equals("logout")) {
            try {
                facade.logout(authData);
                authData = null;
                System.out.println("Logged out");
                return "LOGGED_OUT: Not playing";
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                System.out.println("Unable to logout");
            }
        }
        if (parameters[0].equals("create")) {
            try {
                facade.createGame(authData, parameters[1]);
                System.out.println("Created game " + parameters[1]);
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                System.out.println("Invalid command, follow the format: create <name>");
            }
        }
        if (parameters[0].equals("list")) {
            try {
                List<GameData> gamesList = facade.listGames(authData);
                System.out.println("Games:");
                for (int i = 1; i <= gamesList.size(); i++) {
                    GameData game = gamesList.get(i-1);
                    int gameInt = (int) game.getGameID();
                    System.out.print("    " + i + ") Game ID: " + gameInt + ", White Username: " + game.getWhiteUsername() + ", Black Username: " + game.getBlackUsername() + ", Game Name: " + game.getGameName() + "\n");
                }
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                System.out.println("Invalid command, simply type list");
            }
        }
        if (parameters[0].equals("join")) {
            try {
                assignColor();
                assignGameID();
                facade.joinGame(authData, Integer.parseInt(parameters[1]), parameters[2]);

                ws = new WebSocketCommunicator(this);
                ws.joinPlayer(Integer.parseInt(parameters[1]), color, authData.getAuthToken());

                System.out.println("Joined game " + parameters[1]);

                return "LOGGED_IN: Playing";
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                System.out.println("Invalid join command, follow the format: join <game id> <player color>");
            }
        }
        if (parameters[0].equals("observe")) {
            try {
                facade.joinGame(authData, Integer.parseInt(parameters[1]), null);

                ws = new WebSocketCommunicator(this);
                ws.joinObserver(Integer.parseInt(parameters[1]), authData.getAuthToken());

                System.out.println("Observing game " + parameters[1]);
                return "LOGGED_IN: Observing";
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                System.out.println("Invalid observe command, follow the format: observe <game id>");
            }
        }
        System.out.print("Invalid command. Choose one of the following:\n");
        System.out.print("    create <NAME> - to create an game\n");
        System.out.print("    list - to list games\n");
        System.out.print("    join <ID> [WHITE|BLACK|<empty>] - to join a game\n");
        System.out.print("    observe <ID> - to observe a game\n");
        System.out.print("    logout - when you are done\n");
        System.out.print("    quit - to quit playing chess\n");
        System.out.print("    help - to list possible commands\n");
        return state;
    }

    public String loggedInObserving() {
        if (parameters[0].equals("help")) {
            System.out.print("    redraw - to regenerate the chess board\n");
            System.out.print("    leave - to leave the game\n");
            System.out.print("    help - to list possible commands\n");
            return "LOGGED_IN: Observing";
        }
        if (parameters[0].equals("redraw")) {
            ChessRendering rendering = new ChessRendering(game);
            rendering.renderPerspective();
            return "LOGGED_IN: Observing";
        }
        if (parameters[0].equals("leave")) {
            return "LOGGED_IN: Not playing";
        }
        System.out.print("Invalid command. Choose one of the following:\n");
        System.out.print("    redraw - to regenerate the chess board\n");
        System.out.print("    leave - to leave the game\n");
        System.out.print("    help - to list possible commands\n");
        return state;
    }

    public String loggedInPlaying() {
        if (parameters[0].equals("help")) {
            System.out.print("    move <move> - to move a chess piece\n");
            System.out.print("    highlight <position> - to highlight the legal moves for a piece\n");
            System.out.print("    redraw - to regenerate the chess board\n");
            System.out.print("    resign - to forfeit the game\n");
            System.out.print("    leave - to leave the game\n");
            System.out.print("    help - to list possible commands\n");
            return "LOGGED_IN: Playing";
        }
        if (parameters[0].equals("move")) {
            try {
                if (parameters.length != 2) {
                    System.out.println("Invalid move, move must be in the following format: move a2a3");
                    return "LOGGED_IN: Playing";
                }
                ChessMove move = parseMove(parameters[1]);
                ws.makeMove(this.gameID, move, authData.getAuthToken());
                return "LOGGED_IN: Playing";
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                System.out.println("Invalid move");
                return "LOGGED_IN: Playing";
            }
        }
        if (parameters[0].equals("highlight")) {
            if (parameters.length != 2) {
                System.out.println("Invalid command");
            } else {
                ChessRendering rendering = new ChessRendering(game, this.color);
                rendering.highlight(parameters[1]);
            }
            return "LOGGED_IN: Playing";
        }
        if (parameters[0].equals("redraw")) {
            ChessRendering rendering = new ChessRendering(game, this.color);
            rendering.renderPerspective();
            return "LOGGED_IN: Playing";
        }
        if (parameters[0].equals("resign")) {
            try {
                ws.resign(this.gameID, authData.getAuthToken());
                return "LOGGED_IN: Playing";
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                System.out.println("Unable to resign");
                return "LOGGED_IN: Playing";
            }
        }
        if (parameters[0].equals("leave")) {
            try {
                ws.leave(this.gameID, authData.getAuthToken());
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                System.out.println("Unable to leave");
            }

        }

        System.out.print("Invalid command. Choose one of the following:\n");
        System.out.print("    move <move> - to move a chess piece\n");
        System.out.print("    highlight <position> - to highlight the legal moves for a piece\n");
        System.out.print("    redraw - to regenerate the chess board\n");
        System.out.print("    resign - to forfeit the game\n");
        System.out.print("    leave - to leave the game\n");
        System.out.print("    help - to list possible commands\n");
        return state;
    }

    public void assignColor() {
        if (parameters.length == 3) {
            if (parameters[2].equals("WHITE")) { this.color = ChessGame.TeamColor.WHITE; }
            else if (parameters[2].equals("BLACK")) { this.color = ChessGame.TeamColor.BLACK; }
        }
    }

    public void assignGameID() {
        if (parameters.length == 3) {
            this.gameID = Integer.parseInt(parameters[1]);
        }
    }

    public ChessMove parseMove(String rawMove) throws InvalidMoveException {
        if (rawMove.length() == 4) { // this is a no-promotion move... do we need to implement a promotion move? what do they pass in?
            if (validPosition(rawMove.substring(0,2)) && validPosition(rawMove.substring(2,4))) {
                ChessPosition start = generatePosition(rawMove.substring(0,2));
                ChessPosition end = generatePosition(rawMove.substring(2,4));
                if (game.getBoard().getPiece(start) == null) {
                    throw new InvalidMoveException("Invalid move, that board position is empty");
                }
                return new ChessMove(start, end, null);
            }
        } else {
            throw new InvalidMoveException("move is too long or too short");
        }
        return null;
    }

    public boolean validPosition(String input) {
        List<String> validLetters = new ArrayList<>();
        List<String> validNumbers = new ArrayList<>();
        validLetters.add("a");
        validLetters.add("b");
        validLetters.add("c");
        validLetters.add("d");
        validLetters.add("e");
        validLetters.add("f");
        validLetters.add("g");
        validLetters.add("h");
        validNumbers.add("1");
        validNumbers.add("2");
        validNumbers.add("3");
        validNumbers.add("4");
        validNumbers.add("5");
        validNumbers.add("6");
        validNumbers.add("7");
        validNumbers.add("8");
        return (input.length() == 2
                && validLetters.contains(String.valueOf(input.charAt(0)))
                && validNumbers.contains(String.valueOf(input.charAt(1))));
    }

    private static ChessPosition generatePosition(String location) {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("a", 1);
        mapping.put("b", 2);
        mapping.put("c", 3);
        mapping.put("d", 4);
        mapping.put("e", 5);
        mapping.put("f", 6);
        mapping.put("g", 7);
        mapping.put("h", 8);
        Integer col = mapping.get(String.valueOf(location.charAt(0)));
        Integer row = Character.getNumericValue(location.charAt(1));
        return new ChessPosition(row, col);
    }

    @Override
    public void updateRenderGame(ChessGame game) {
        this.game = game;
        ChessRendering rendering = new ChessRendering(this.game, color);
        rendering.renderPerspective();
    }

    @Override
    public void notify(UserGameCommand gameCommand) {
        System.out.print("Notified here in CommandHandler!!!");
    }

}
