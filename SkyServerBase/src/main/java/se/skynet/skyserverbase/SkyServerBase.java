package se.skynet.skyserverbase;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import se.skynet.skyserverbase.command.*;
import se.skynet.skyserverbase.database.DatabaseConnectionManager;
import se.skynet.skyserverbase.manager.PlayerVisibilityManager;
import se.skynet.skyserverbase.manager.SignGUIManger;
import se.skynet.skyserverbase.manager.VanillaFeatureManager;
import se.skynet.skyserverbase.manager.WorldConfigManager;
import se.skynet.skyserverbase.playerdata.PlayerDataManager;
import org.reflections.Reflections;
import se.skynet.skyserverbase.proxy.BungeeProxyApi;

public final class SkyServerBase extends JavaPlugin {

    private PlayerDataManager playerDataManager;
    private DatabaseConnectionManager databaseConnectionManager;
    private ProtocolManager protocolManager;
    private PlayerVisibilityManager playerVisibilityManager;
    private BungeeProxyApi bungeeProxyApi;
    private VanillaFeatureManager vanillaFeatureManager;
    private WorldConfigManager worldConfigManager;
    private SignGUIManger signGUIManger;
    private String servername;
    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();

        this.bungeeProxyApi = new BungeeProxyApi(this);

        this.databaseConnectionManager = new DatabaseConnectionManager();
        databaseConnectionManager.connect();

        this.playerDataManager = new PlayerDataManager(this);
        this.playerVisibilityManager = new PlayerVisibilityManager(this);

        this.vanillaFeatureManager = new VanillaFeatureManager(this);
        this.worldConfigManager = new WorldConfigManager(this);

        this.signGUIManger = new SignGUIManger(this);

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
        Command.registerCommand(this.getCommand("fly"), new FlyCommand(this));
        Command.registerCommand(this.getCommand("build"), new BuildCommand(this));
        Command.registerCommand(this.getCommand("ban"), new DisabledCommand(this));
        Command.registerCommand(this.getCommand("kick"), new DisabledCommand(this));
        Command.registerCommand(this.getCommand("nick"), new NickCommand(this));
        Command.registerCommand(this.getCommand("unnick"), new UnnickCommand(this));
        Command.registerCommand(this.getCommand("hide"), new HideCommand(this));
        Command.registerCommand(this.getCommand("gm"), new GamemodeCommand(this));
        Command.registerCommand(this.getCommand("configcreator"), new ConfigCreatorCommand(this));
        Command.registerCommand(this.getCommand("executeas"), new ExecuteAsCommand(this));
        Command.registerCommand(this.getCommand("trollpacket"), new TrollPacketCommand(this));
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
            world.setGameRuleValue("doFireTick", "false");

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

    public VanillaFeatureManager getVanillaFeatureManager() {
        return vanillaFeatureManager;
    }

    public BungeeProxyApi getBungeeProxyApi() {
        return bungeeProxyApi;
    }

    public String getServerName() {
        return servername;
    }

    public SignGUIManger getSignGUIManger() {
        return signGUIManger;
    }

    public WorldConfigManager getWorldConfigManager() {
        return worldConfigManager;
    }
}
