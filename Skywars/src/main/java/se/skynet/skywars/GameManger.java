package se.skynet.skywars;

import se.skynet.skywars.loot.LootManger;

public class GameManger {

    private final Skywars plugin;
    private final LootManger lootManger;
    private final ConfigManager configManager;
    private final PlayerManager playerJoinManager;
    private final CageManager cageManager;
    private GameState gameState;
    public GameManger(Skywars plugin) {
        this.plugin = plugin;
        this.lootManger = new LootManger(this);
        this.playerJoinManager = new PlayerManager(this);
        this.cageManager = new CageManager(this);
        this.configManager = new ConfigManager(this);
        setGameState(GameState.LOBBY);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        switch (gameState){
            case LOBBY:

            case STARTING:
                break;
            case CAGE_DESTRUCTION:
                lootManger.fillChests();
                this.cageManager.removeCages();
                break;
            case INGAME:
                break;
            case ENDING:
                break;
        }
    }


    public PlayerManager getPlayerJoinManager() {
        return playerJoinManager;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Skywars getPlugin() {
        return plugin;
    }

    public LootManger getLootManger() {
        return lootManger;
    }

    public CageManager getCageManager() {
        return cageManager;
    }
}
