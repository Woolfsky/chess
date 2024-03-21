package web;

import model.AuthData;
import ui.ChessRendering;

public class CommandHandler {
    String[] parameters;
    String state;

    public ServerFacade facade;

    AuthData authData = null;

    public CommandHandler(String[] p, String s) {
        parameters = p;
        state = s;
        facade = new ServerFacade(8080);
    }

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
                System.out.println("Games:\n" + games.get("games"));
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (parameters[0].equals("join")) {
            try {
                facade.joinGame(authData, Integer.parseInt(parameters[1]), parameters[2]);
                System.out.println("Joined game " + parameters[1]);
                return "LOGGED_IN: Playing";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (parameters[0].equals("observe")) {
            try {
                facade.joinGame(authData, Integer.parseInt(parameters[1]), null);
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
        System.out.print("Invalid command. Choose one of the following:\n");
        System.out.print("    logout - when you are done\n");
        System.out.print("    quit - to quit playing chess\n");
        System.out.print("    help - to list possible commands\n");
        return state;
    }

    public String loggedInPlaying() {
        ChessRendering render = new ChessRendering();
//        render.main([]);

        if (parameters[0].equals("help")) {
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
        System.out.print("Invalid command. Choose one of the following:\n");
        System.out.print("    logout - when you are done\n");
        System.out.print("    quit - to quit playing chess\n");
        System.out.print("    help - to list possible commands\n");
        return state;
    }


}
