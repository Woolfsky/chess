import chess.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Chess! Type help to get started.");
        String state = "Logged out";
        while (true) {
            System.out.printf("Type a command >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var parameters = line.split(" ");

            CommandHandler handler = new CommandHandler(parameters, state);

            for (var p : parameters) {
                System.out.printf("You said: %s%n", p);
            }
        }
    }
}