package se.skynet.skyserverbase;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import se.skynet.skyserverbase.command.BuildCommand;
import se.skynet.skyserverbase.command.ConfigCreatorCommand;
import se.skynet.skyserverbase.command.DisabledCommand;
import se.skynet.skyserverbase.command.FlyCommand;
import se.skynet.skyserverbase.database.DatabaseConnectionManager;
import se.skynet.skyserverbase.playerdata.PlayerDataManager;
import org.reflections.Reflections;
import se.skynet.skyserverbase.proxy.BungeeProxyApi;

public final class SkyServerBase extends JavaPlugin {

    private PlayerDataManager playerDataManager;
    private DatabaseConnectionManager databaseConnectionManager;
    private ProtocolManager protocolManager;
    private BungeeProxyApi bungeeProxyApi;
    private String servername;
    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();

        this.bungeeProxyApi = new BungeeProxyApi(this);

        this.databaseConnectionManager = new DatabaseConnectionManager();
        databaseConnectionManager.connect();

        this.playerDataManager = new PlayerDataManager(this);

        registerCommands();
        registerListeners();
        bungeeProxyApi.registerServer().whenComplete((name, throwable) -> {
            if(throwable != null){
                System.out.println("Failed to register server with bungee");
                throwable.printStackTrace();
                return;
            }
            servername = name;
        });
    }

    private void registerCommands() {
        this.getCommand("fly").setExecutor(new FlyCommand(this));
        this.getCommand("build").setExecutor(new BuildCommand(this));
        this.getCommand("ban").setExecutor(new DisabledCommand(this));
        this.getCommand("kick").setExecutor(new DisabledCommand(this));
        this.getCommand("configcreator").setExecutor(new ConfigCreatorCommand(this));
    }

    private void registerListeners() {
        Reflections reflections = new Reflections("se.skynet.skyserverbase.manager");
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

    public BungeeProxyApi getBungeeProxyApi() {
        return bungeeProxyApi;
    }

    public String getServerName() {
        return servername;
    }

}
