package se.skynet.skywars;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class BukkitEventManager implements Listener {

    private final GameManger gameManger;
    public BukkitEventManager(GameManger gameManger) {
        this.gameManger = gameManger;
        gameManger.getPlugin().getServer().getPluginManager().registerEvents(this, gameManger.getPlugin());
        gameManger.getPlugin().getServer().getWorlds().get(0).setGameRuleValue("doDaylightCycle", "false");
        gameManger.getPlugin().getServer().getWorlds().get(0).setTime(0);
    }

    // prevent weather changes
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    // prevent mob spawning
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    // prevnet block breaking and placing when GameState is not INGAME

    // prevent block breaking
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(gameManger.getGameState() != GameState.INGAME){
            event.setCancelled(true);
        }
    }

    // prevent block placing
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(gameManger.getGameState() != GameState.INGAME){
            event.setCancelled(true);
        }
    }

}
