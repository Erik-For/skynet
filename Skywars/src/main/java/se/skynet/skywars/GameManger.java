package se.skynet.skywars;

import org.bukkit.ChatColor;
import se.skynet.skywars.loot.LootManger;
import se.skynet.skywars.timing.Timer;

import java.util.UUID;

public class GameManger {


    private final Skywars plugin;
    private final LootManger lootManger;
    private final ConfigManager configManager;
    private final PlayerManager playerManager;
    private final CageManager cageManager;
    private final BukkitEventManager bukkitEventManager;
    private final TagManager tagManager;
    private GameState gameState = GameState.LOBBY;
    public GameManger(Skywars plugin) {
        this.plugin = plugin;
        this.lootManger = new LootManger(this);
        this.playerManager = new PlayerManager(this);
        this.cageManager = new CageManager(this);
        this.configManager = new ConfigManager(this);
        this.bukkitEventManager = new BukkitEventManager(this);
        this.tagManager = new TagManager(this);
        setGameState(GameState.LOBBY);
    }

    public void setGameState(GameState gameState) {
        System.out.println(gameState);
        this.gameState = gameState;
        switch (gameState){
            case LOBBY:
                
                break;
            case STARTING:
                System.out.println("Timmer starting");
                new Timer(5, seconds -> {
                    plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Game starting in " + ChatColor.RED + Integer.toString(seconds));
                },seconds -> {
                    setGameState(GameState.CAGE_DESTRUCTION);
                }).start(plugin);
                break;
            case CAGE_DESTRUCTION:
                lootManger.fillChests();
                this.cageManager.removeCages();
                new Timer(2, seconds -> {}, seconds -> {
                    setGameState(GameState.INGAME);
                }).start(plugin);
                break;
            case INGAME:

                break;
            case END:
                // print win message, 1,2,3 place in kills and winner

                
                break;
        }
    }


    public PlayerManager getPlayerManager() {
        return playerManager;
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

    public TagManager getTagManager() {
        return tagManager;
    }
}
