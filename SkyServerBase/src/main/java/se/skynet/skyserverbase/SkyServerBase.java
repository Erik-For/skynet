package se.skynet.skyserverbase;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import se.skynet.skyserverbase.command.*;
import se.skynet.skyserverbase.database.DatabaseConnectionManager;
import se.skynet.skyserverbase.manager.PlayerVisibilityManager;
import se.skynet.skyserverbase.playerdata.PlayerDataManager;
import org.reflections.Reflections;
import se.skynet.skyserverbase.proxy.BungeeProxyApi;

public final class SkyServerBase extends JavaPlugin {

    private PlayerDataManager playerDataManager;
    private DatabaseConnectionManager databaseConnectionManager;
    private ProtocolManager protocolManager;
    private PlayerVisibilityManager playerVisibilityManager;
    private BungeeProxyApi bungeeProxyApi;
    private String servername;
    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();

        this.bungeeProxyApi = new BungeeProxyApi(this);

        this.databaseConnectionManager = new DatabaseConnectionManager();
        databaseConnectionManager.connect();

        this.playerDataManager = new PlayerDataManager(this);
        this.playerVisibilityManager = new PlayerVisibilityManager(this);

        registerCommands();
        registerListeners();
        disableMinecraftFeatures();
        bungeeProxyApi.registerServer().whenComplete((name, throwable) -> {
            if(throwable != null){
                System.out.println("Failed to register server with bungee");
                throwable.printStackTrace();
                return;
            }
            servername = name;

            if(System.getenv().containsKey("DEV_USERNAME")) {
                bungeeProxyApi.movePlayer(System.getenv("DEV_USERNAME"), name);
            }
        });
    }

    private void registerCommands() {
        this.getCommand("fly").setExecutor(new FlyCommand(this));
        this.getCommand("build").setExecutor(new BuildCommand(this));
        this.getCommand("ban").setExecutor(new DisabledCommand(this));
        this.getCommand("kick").setExecutor(new DisabledCommand(this));
        this.getCommand("nick").setExecutor(new NickCommand(this));
        this.getCommand("unnick").setExecutor(new UnnickCommand(this));
        this.getCommand("hide").setExecutor(new HideCommand(this));
        this.getCommand("gm").setExecutor(new GamemodeCommand(this));
        this.getCommand("configcreator").setExecutor(new ConfigCreatorCommand(this));
    }

    private void registerListeners() {
        Reflections reflections = new Reflections("se.skynet.skyserverbase.manager.headless");
        for (Class<? extends Listener> listenerClass : reflections.getSubTypesOf(Listener.class)) {
            try {
                Listener listener = listenerClass.getConstructor(SkyServerBase.class).newInstance(this);
                this.getServer().getPluginManager().registerEvents(listener, this);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("FATAL: Failed to register listener " + listenerClass.getSimpleName());
            }
        }
    }

    private void disableMinecraftFeatures() {
        this.getServer().getWorlds().forEach(world -> {
            world.setAutoSave(false);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doWeatherCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doTileDrops", "false");
            world.setGameRuleValue("doEntityDrops", "false");
            world.setGameRuleValue("mobGriefing", "false");
            world.setGameRuleValue("naturalRegeneration", "false");

            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("logAdminCommands", "false");
            world.setGameRuleValue("commandBlockOutput", "false");
        });
    }
    @Override
    public void onDisable() {
        this.bungeeProxyApi.unregisterServer(servername);
    }

    public DatabaseConnectionManager getDatabaseConnectionManager() {
        return databaseConnectionManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public PlayerVisibilityManager getPlayerVisibilityManager() {
        return playerVisibilityManager;
    }

    public BungeeProxyApi getBungeeProxyApi() {
        return bungeeProxyApi;
    }

    public String getServerName() {
        return servername;
    }


}
