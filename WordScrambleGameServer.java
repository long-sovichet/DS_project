import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;

public class WordScrambleGameServer extends UnicastRemoteObject implements WordScrambleGame {
    private List<String> wordList = Arrays.asList("java", "rmi", "algorithm");
    private Map<String, Integer> leaderboard = new HashMap<>();
    private String currentWord; // Store the original word

    protected WordScrambleGameServer() throws RemoteException {
        super();
    }

    @Override
    public String getScrambledWord() throws RemoteException {
        Random random = new Random();
        currentWord = wordList.get(random.nextInt(wordList.size())); // Store the original word
        return scrambleWord(currentWord); // Send the scrambled word to the client
    }

    @Override
    public boolean submitGuess(String playerName, String guess) throws RemoteException {
        // Compare the guess with the original word (currentWord)
        if (guess.equalsIgnoreCase(currentWord)) {
            leaderboard.put(playerName, leaderboard.getOrDefault(playerName, 0) + 1);
            return true;
        }
        return false;
    }

    @Override
    public String getLeaderboard() throws RemoteException {
        StringBuilder sb = new StringBuilder("Leaderboard:\n");
        leaderboard.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"));
        return sb.toString();
    }

    @Override
    public void registerPlayer(String playerName) throws RemoteException {
        leaderboard.put(playerName, 0);
    }

    private String scrambleWord(String word) {
        List<Character> letters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            letters.add(c);
        }
        String scrambled;
        do {
            Collections.shuffle(letters);
            StringBuilder scrambledBuilder = new StringBuilder();
            for (char c : letters) {
                scrambledBuilder.append(c);
            }
            scrambled = scrambledBuilder.toString();
        } while (scrambled.equals(word)); // Ensure the scrambled word is different from the original word
        return scrambled;
    }

    public static void main(String[] args) {
        try {
            WordScrambleGameServer server = new WordScrambleGameServer();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("WordScrambleGame", server);
            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}