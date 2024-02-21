package se.skynet.skywars;

public class GameManger {

    private final Skywars plugin;
    private GameState gameState;
    public GameManger(Skywars plugin) {
        this.plugin = plugin;
        setGameState(GameState.LOBBY);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        switch (gameState){
            case LOBBY:

            case STARTING:
                break;
            case INGAME:
                break;
            case ENDING:
                break;
        }
    }
}
