package se.skynet.skyblock;

import org.bukkit.plugin.java.JavaPlugin;
import se.skynet.skyblock.commands.ItemCommand;
import se.skynet.skyblock.commands.SkyblockCommand;
import se.skynet.skyblock.managers.*;
import se.skynet.skyblock.mobs.SkyblockMobEventHandler;
import se.skynet.skyserverbase.SkyServerBase;

public final class Skyblock extends JavaPlugin {

    private final SkyServerBase parentPlugin = SkyServerBase.getPlugin(SkyServerBase.class);
    private SkyblockPlayerManager playerManager;
    private SkyblockDatabaseMethods databaseMethods;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new SkyblockMobEventHandler(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockManager(), this);
        this.getServer().getPluginManager().registerEvents(new SkyblockMenuItemManager(this), this);
        this.getServer().getPluginManager().registerEvents( new SkyblockItemManager(this), this);
        this.getServer().getPluginManager().registerEvents(new ScoreboardManager(this), this);
        this.getServer().getPluginManager().registerEvents(new SpawnManagerTemp(this), this);
        this.playerManager = new SkyblockPlayerManager(this);
        this.getServer().getPluginManager().registerEvents(this.playerManager, this);
        SkyblockCommand.registerCommand(this.getCommand("skyblock"), new SkyblockCommand(this));
        SkyblockCommand.registerCommand(this.getCommand("item"), new ItemCommand(this));

        this.databaseMethods = new SkyblockDatabaseMethods(this.getParentPlugin().getDatabaseConnectionManager());
        this.databaseMethods.createProfileTable();

        this.getParentPlugin().getVanillaFeatureManager().setDayNightCycleEnabled(true);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SkyServerBase getParentPlugin() {
        return parentPlugin;
    }

    public SkyblockPlayerManager getPlayerManager() {
        return playerManager;
    }

    public SkyblockDatabaseMethods getDatabaseMethods() {
        return databaseMethods;
    }
    public static Skyblock getInstance() {
        return getPlugin(Skyblock.class);
    }
}
