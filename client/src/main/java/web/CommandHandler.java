package web;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import ui.ChessRendering;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.List;

public class CommandHandler implements WebSocketCommunicator.SocketListener {
    String[] parameters;
    String state;
    public ServerFacade facade;
    AuthData authData = null;
    ChessGame game = new ChessGame();
    ChessGame.TeamColor color = null;
    WebSocketCommunicator ws;
    String username;

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
                username = parameters[1];
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
            }

        }
        if (parameters[0].equals("login")) {
            try {
                AuthData auth = facade.login(parameters[1], parameters[2]);
                authData = auth;
                System.out.println("Logged in as " + parameters[1]);
                username = parameters[1];
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
                System.out.println(e.getMessage());
            }
        }
        if (parameters[0].equals("create")) {
            try {
                facade.createGame(authData, parameters[1]);
                System.out.println("Created game " + parameters[1]);
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (parameters[0].equals("list")) {
            try {
                var games = facade.listGames(authData);
                List gamesList = games.get("games");
                System.out.println("Games:");
                for (int i = 1; i <= gamesList.size(); i++) {
                    String game = String.valueOf(gamesList.get(i-1));
                    game = game.replace("{gameID=", "Game ID: ");
                    game = game.replace("whiteUsername=", "White Username: ");
                    game = game.replace("blackUsername=", "Black Username: ");
                    game = game.replace("gameName=", "Game Name: ");
                    game = game.replace("}", "");
                    game = game.replaceAll(".0,", ",");
                    System.out.print("    " + i + ") " + game + "\n");
                }
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (parameters[0].equals("join")) {
            try {
                facade.joinGame(authData, Integer.parseInt(parameters[1]), parameters[2]);
                // websocket join game, keep track of session
                ws = new WebSocketCommunicator(this);
                ws.joinPlayer(Integer.parseInt(parameters[1]), color, username, authData.getAuthToken());

                assignColor();
                ChessRendering rendering;
                if (color == null) {
                    rendering = new ChessRendering(game);
                } else {
                    rendering = new ChessRendering(game, this.color);
                }
                rendering.renderPerspective();

                System.out.println("Joined game " + parameters[1]);
                return "LOGGED_IN: Playing";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (parameters[0].equals("observe")) {
            try {
                facade.joinGame(authData, Integer.parseInt(parameters[1]), null);
                ChessRendering rendering = new ChessRendering(game);
                rendering.renderPerspective();
                System.out.println("Observing game " + parameters[1]);
                return "LOGGED_IN: Observing";
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
            // implement make move functionality
            return "LOGGED_IN: Playing";
        }
        if (parameters[0].equals("highlight")) {
            ChessRendering rendering = new ChessRendering(game, this.color);
            rendering.highlight(parameters[1]);
            return "LOGGED_IN: Playing";
        }
        if (parameters[0].equals("redraw")) {
            ChessRendering rendering = new ChessRendering(game, this.color);
            rendering.renderPerspective();
            return "LOGGED_IN: Playing";
        }
        if (parameters[0].equals("resign")) {
            // implement resign functionality
            return "LOGGED_IN: Playing";
        }
        if (parameters[0].equals("leave")) {
            return "LOGGED_IN: Not playing";
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


    @Override
    public void updateGame(ChessGame game) {
        this.game = game;
    }

    @Override
    public void notify(UserGameCommand gameCommand) {
        System.out.print("Notified here in CommandHandler!!!");
    }
}
