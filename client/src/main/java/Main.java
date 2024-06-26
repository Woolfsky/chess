import model.AuthData;
import web.CommandHandler;
import web.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Chess! Type help to get started.");
        CommandHandler handler = new CommandHandler();
        String state = "LOGGED_OUT: Not playing";

        while (!state.equals("QUIT")) {
            if (!state.equals("LOGGED_IN: Playing") && !state.equals("LOGGED_IN: Observing")) {
                System.out.printf("[%s] >>> ", state);
            }
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var parameters = line.split(" ");

            handler.setParametersState(parameters, state);
            state = handler.executeReturnState();
        }

        try {
            handler.facade.delete();
        } catch (Exception e) { 
            System.out.println("Failed to clear database: " + e.getMessage());
        }
    }
}