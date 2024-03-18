import model.AuthData;
import web.CommandHandler;
import web.ServerFacade;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Chess! Type help to get started.");
        CommandHandler handler = new CommandHandler();;
        String state = "LOGGED_OUT: Not playing";
        while (!state.equals("QUIT")) {
            System.out.printf("[%s] >>> ", state);
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var parameters = line.split(" ");

            handler.setParametersState(parameters, state);
            state = handler.executeReturnState();
        }
    }
}