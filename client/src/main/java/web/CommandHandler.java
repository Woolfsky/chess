package web;

import com.google.gson.Gson;
import model.AuthData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class CommandHandler {
    String[] parameters;
    String state;

    ServerFacade facade;

    public CommandHandler(String[] p, String s) {
        parameters = p;
        state = s;
        facade = new ServerFacade(8080);
    }

    public String executeReturnState() {
        switch (state) {
            case "LOGGED_OUT: Not playing":
                return loggedOutNotPlaying();
        }
        return "not implemented yet....";
    }

    public String loggedOutNotPlaying() {
        if (parameters[0].equals("register")) {
            try {
                facade.register(parameters[1], parameters[2], parameters[3]);
                System.out.println("New user registered as " + parameters[1]);
                return "LOGGED_IN: Not playing";
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
        return "ERROR: should not have gotten here...";
    }


}
