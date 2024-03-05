package se.skynet.skynetproxy;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.reflections.Reflections;
import se.skynet.skynetproxy.command.SkynetCommand;
import se.skynet.skynetproxy.database.DatabaseConnectionManager;
import se.skynet.skynetproxy.database.DatabaseMethods;
import se.skynet.skynetproxy.party.PartyManger;
import se.skynet.skynetproxy.playerdata.PlayerDataManager;
import se.skynet.skynetproxy.server.ServerManager;

import java.lang.reflect.InvocationTargetException;

public final class SkyProxy extends Plugin {
    private ServerManager serverManager;
    private DatabaseConnectionManager databaseConnectionManager;

    private PlayerDataManager playerDataManager;
    private PartyManger partyManager;

    @Override
    public void onEnable() {
        serverManager = new ServerManager(this);
        this.databaseConnectionManager = new DatabaseConnectionManager();
        databaseConnectionManager.connect();

        this.partyManager = new PartyManger(this);
        this.getProxy().getPluginManager().registerListener(this, partyManager);
        this.playerDataManager = new PlayerDataManager(this);

        new DatabaseMethods(databaseConnectionManager).createPlayerTable();
        new ServerApi(this);
        registerCommands();
        registerListeners();

    }

    private void registerCommands() {
        Reflections reflections = new Reflections("se.skynet.skynetproxy.command");
        for (Class<? extends SkynetCommand> commandClass : reflections.getSubTypesOf(SkynetCommand.class)) {
            try {
                this.getProxy().getPluginManager().registerCommand(this, commandClass.getConstructor(SkyProxy.class).newInstance(this));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                System.out.println("FATAL: Failed to register command " + commandClass.getSimpleName());
            }
        }
    }
    private void registerListeners() {
        Reflections reflections = new Reflections("se.skynet.skynetproxy.manager");
        for (Class<? extends Listener> listenerClass : reflections.getSubTypesOf(Listener.class)) {
            try {
                Listener listener = listenerClass.getConstructor(SkyProxy.class).newInstance(this);
                this.getProxy().getPluginManager().registerListener(this, listener);
            } catch (Exception e) {
                System.out.println("FATAL: Failed to register listener " + listenerClass.getSimpleName());
            }
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public DatabaseConnectionManager getDatabaseConnectionManager() {
        return databaseConnectionManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public PartyManger getPartyManager() {
        return partyManager;
    }
}
