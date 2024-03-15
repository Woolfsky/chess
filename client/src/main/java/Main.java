import chess.*;

public class Main {
    public static void main(String[] args) {
        for (var i = 0; i < args.length; i++) {
            System.out.printf("%d. %s%n", i+1, args[i]);
        }
    }
}