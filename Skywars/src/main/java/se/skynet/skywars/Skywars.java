package se.skynet.skywars;

import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;
import se.skynet.skyserverbase.SkyServerBase;

public final class Skywars extends JavaPlugin {


    private final SkyServerBase parentPlugin = SkyServerBase.getPlugin(SkyServerBase.class);

    private GameManger gameManager;
    @Override
    public void onEnable() {
        gameManager = new GameManger(this);
        this.getCommand("start").setExecutor(new StartCommand(gameManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SkyServerBase getParentPlugin() {
        return parentPlugin;
    }
}
