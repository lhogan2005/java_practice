import java.util.ArrayList;
import java.util.List;

public class GameLobby {
    private List<Player> players;
    private List<Player> playingPlayers = new ArrayList<>();

    
    public GameLobby() {
        this.players = new ArrayList<>();
    }

    void registerPlayer(Player player) {
        players.add(player);
        System.out.printf("[GameLobby] %s %s has joined the lobby.%n", player.getPlayerType(), player.getPlayerName());
    }
    
    void removePlayer(Player player) {
        players.remove(player);
        System.out.printf("[GameLobby] %s %s has left the lobby.%n", player.getPlayerType(), player.getPlayerName());
    }

    void sendMessage(String message, Player sender) {
        System.out.printf("[%s] sends: \"%s\"%n", sender.getPlayerName(), message);
        System.out.printf("[GameLobby] Message from %s: \"%s\"%n", sender.getPlayerName(), message);
        // Need this because players is just a list. If we call recieve messagea on a list of players, we can't, there is for one no method and two we want each player to receive the message.
        for (Player player : players) {
            if ((!player.equals(sender))){
                player.receiveMessage(message);
            }
        }
    }

    void joinGame(Player player) {
        registerPlayer(player);
    }

    void leaveGame(Player player) {
        removePlayer(player);
    }

    void receiveMessage(String name, String message) {
        System.out.printf("[%s] received: \"%s\"%n", name, message);
    }

    void startMatch() {
        for (Player player : players) {
            if (player.getPlayerType().equals("HumanPlayer") || player.getPlayerType().equals("AIPlayer")) {
                playingPlayers.add(player);
            }
        }
        if (playingPlayers.size() >= 2) {
            System.out.printf("[GameLobby] Starting game with players: ");
            int i = 0;
            for (Player player : playingPlayers) {
                i++;
                System.out.print(player);
                if (!(i == playingPlayers.size())) {
                    System.out.printf(", ");
                }
            }
            System.out.printf("%n");
        } else {
            System.out.println("[GameLobby] Not enough players to start a match.");
        }
    }
}

interface Player {
    void joinGame();
    void leaveGame();
    void sendMessage(String message);
    void receiveMessage(String message);
    String getPlayerType();
    String getPlayerName();
}

abstract class AbstractPlayer implements Player {
    protected String name;
    protected GameLobby lobby;

    public AbstractPlayer(String name, GameLobby lobby) {
        this.name = name;
        this.lobby = lobby;
    }

    @Override
    public void sendMessage(String message) {
        // Here this returns the current player object
        // When we call a method we don't need to pass type in
        lobby.sendMessage(message, this);
    }

    @Override
    public void receiveMessage(String message) {
        lobby.receiveMessage(name, message);
    }

    public abstract String getPlayerType();

    @Override
    public String getPlayerName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getPlayerName();
    }
}

class HumanPlayer extends AbstractPlayer {
    
    public HumanPlayer(String name, GameLobby lobby) {
        super(name, lobby);
    }

    @Override
    public void joinGame() {
        lobby.joinGame(this);
    }

    @Override
    public void leaveGame() {
        lobby.leaveGame(this);
    }

    @Override
    public String getPlayerType() {
        return "HumanPlayer";
    }
}

class AIPlayer extends AbstractPlayer {
    public AIPlayer(String name, GameLobby lobby) {
        super(name, lobby);
    }

    @Override
    public void joinGame() {
        lobby.joinGame(this);
    }

    @Override
    public void leaveGame() {
        lobby.leaveGame(this);
    }

    @Override
    public String getPlayerType() {
        return "AIPlayer";
    }
}

class Spectator extends AbstractPlayer {
    public Spectator(String name, GameLobby lobby) {
        super(name, lobby);
    }

    @Override
    public void joinGame() {
        lobby.joinGame(this);
    }

    @Override
    public void leaveGame() {
        lobby.leaveGame(this);
    }

    @Override
    public String getPlayerType() {
        return "Spectator";
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("[GameLobby] Spectators cannot send messages.");
    }
}