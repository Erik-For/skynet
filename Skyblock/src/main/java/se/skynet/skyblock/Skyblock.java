package se.skynet.skyblock;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import se.skynet.skyblock.commands.SkyblockCommand;
import se.skynet.skyblock.managers.BlockManager;
import se.skynet.skyblock.managers.ItemManager;
import se.skynet.skyblock.managers.SkyblockMenuItemManager;
import se.skynet.skyblock.managers.SkyblockPlayerManager;
import se.skynet.skyblock.mobs.SkyblockMobEventHandler;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.util.NBTHelper;

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
        this.getServer().getPluginManager().registerEvents( new ItemManager(this), this);
        this.playerManager = new SkyblockPlayerManager(this);
        this.getServer().getPluginManager().registerEvents(this.playerManager, this);
        this.getCommand("skyblock").setExecutor(new SkyblockCommand(this));

        this.databaseMethods = new SkyblockDatabaseMethods(this.getParentPlugin().getDatabaseConnectionManager());
        this.databaseMethods.createProfileTable();
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
