package se.skynet.skywars;

import org.bukkit.plugin.java.JavaPlugin;
import se.skynet.skyserverbase.SkyServerBase;

public class Skywars extends JavaPlugin {

    private final SkyServerBase parentPlugin = SkyServerBase.getPlugin(SkyServerBase.class);
    private Game game;

    @Override
    public void onEnable() {
        parentPlugin.getVanillaFeatureManager().setPvpEnabled(true);
        game = new Game(this);
        getCommand("start").setExecutor(new StartCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public SkyServerBase getParentPlugin() {
        return parentPlugin;
    }

    public Game getGame() {
        return game;
    }
}
