package se.skynet.skywars;

import org.bukkit.ChatColor;
import se.skynet.skywars.format.ChatMessageConstructors;
import se.skynet.skywars.loot.LootManager;
import se.skynet.skywars.loot.OldLootManager;
import se.skynet.skywars.manager.*;
import se.skynet.skywars.timing.Timer;

public class Game {

    private final PlayerVisibilityManager playerVisibilityManager;
    private final LootManager lootManager;
    private final Skywars plugin;
    private final PlayerManager playerManager;
    private final CageManager cageManager;
    private GameState gameState = GameState.WAITING;

    public Game(Skywars plugin) {
        this.plugin = plugin;

        playerVisibilityManager = new PlayerVisibilityManager(plugin);
        new PlayerDeathManager(this);
        new BlockManager(this);
        new HealthDispalyManager(this);
        cageManager = new CageManager(this);
        playerManager = new PlayerManager(this);
        lootManager = new LootManager();

        new ConfigLoader(this);
        setGameState(GameState.WAITING);
    }

    public void setGameState(GameState gameState) {
        if (this.gameState == gameState) return;

        this.gameState = gameState;
        switch (gameState) {
            case WAITING:
                break;
            case STARTING:
                plugin.getParentPlugin().getPlayerDataManager().setPersistent(true);
                new Timer(10, seconds -> {
                    if (seconds == 10 || seconds == 5 || seconds == 4 || seconds == 3 || seconds == 2 || seconds == 1) {
                        plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Game starting in " +  ChatColor.RED + seconds);
                    }
                }, seconds -> {
                    setGameState(GameState.CAGE_DESTRUCTION);
                }).start(plugin);
                break;
            case CAGE_DESTRUCTION:
                lootManager.fillChests();
                cageManager.destroyCages();
                new Timer(2, seconds -> {},
                        seconds -> {
                        setGameState(GameState.IN_GAME);
                    }
                ).start(plugin);
                break;
            case IN_GAME:

                break;
            case END:
                plugin.getServer().broadcastMessage(ChatMessageConstructors.winMessage(
                        playerManager.getPlayersInGame(),
                        playerManager.getPlayersAlive().get(0),
                        plugin
                ));


                new Timer(20, seconds -> {
                    if (seconds == 5 || seconds == 4 || seconds == 3 || seconds == 2 || seconds == 1) {
                        plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Returning to lobby in " + ChatColor.RED + seconds);
                    }
                }, seconds -> {
                    plugin.getParentPlugin().getBungeeProxyApi().sendAllToServerType("LOBBY", getPlugin().getParentPlugin().getServerName());
                    plugin.getParentPlugin().getBungeeProxyApi().unregisterServer(getPlugin().getParentPlugin().getServerName());
                    new Timer(1, seconds1 -> {
                    }, seconds1 -> {
                        System.exit(1);
                    }).start(plugin);
                }).start(plugin);

                break;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public PlayerVisibilityManager getPlayerVisibilityManager() {
        return playerVisibilityManager;
    }

    public Skywars getPlugin() {
        return plugin;
    }

    public LootManager getLootManager() {
        return lootManager;
    }


    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public CageManager getCageManager() {
        return cageManager;
    }
}
