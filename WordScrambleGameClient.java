import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class WordScrambleGameClient {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            WordScrambleGame game = (WordScrambleGame) registry.lookup("WordScrambleGame");

            System.out.print("Enter your name: ");
            String playerName = scanner.nextLine();
            game.registerPlayer(playerName);

            while (true) {
                String scrambledWord = game.getScrambledWord();
                System.out.println("Scrambled Word: " + scrambledWord);
                System.out.print("Your guess: ");
                String guess = scanner.nextLine();

                if (game.submitGuess(playerName, guess)) {
                    System.out.println("Correct! +1 point");
                } else {
                    System.out.println("Incorrect. Try again!");
                }

                System.out.println(game.getLeaderboard());
                System.out.print("Play again? (y/n): ");
                String choice = scanner.nextLine();
                if (!choice.equalsIgnoreCase("y")) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}