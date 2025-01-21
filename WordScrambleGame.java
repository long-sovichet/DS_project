import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WordScrambleGame extends Remote {
    String getScrambledWord() throws RemoteException;
    boolean submitGuess(String playerName, String guess) throws RemoteException;
    String getLeaderboard() throws RemoteException;
    void registerPlayer(String playerName) throws RemoteException;
}