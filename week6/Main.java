public class Main {
    public static void main(String[] args) {
        GameLobby lobby = new GameLobby();

        Player alice = PlayerFactory.createPlayer("human", "Alice", lobby);
        Player bot = PlayerFactory.createPlayer("ai", "BotX", lobby);
        Player bob = PlayerFactory.createPlayer("spectator", "Bob", lobby);
        Player admin = PlayerFactory.createPlayer("admin", "Charlie", lobby);

        alice.joinGame();
        bot.joinGame();
        bob.joinGame();
        admin.joinGame();

        admin.sendMessage("Hello, everyone!");
        ((AdminPlayer) admin).kickPlayer("Bob");

        lobby.startMatch();
    }
}