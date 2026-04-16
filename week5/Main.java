class Main {
    public static void main(String[] args) {
        GameLobby lobby = new GameLobby();
        Player alice = new HumanPlayer("Alice", lobby);
        Player bot = new AIPlayer("BotX", lobby);
        Player bob = new Spectator("Bob", lobby);

        alice.joinGame();
        bot.joinGame();
        bob.joinGame();

        alice.sendMessage("LOL");
        bob.sendMessage("Shut up, Alice");  // Spectator tries to send a message
        lobby.startMatch();
        bob.leaveGame();
        bot.leaveGame();
        alice.leaveGame();
    }
}