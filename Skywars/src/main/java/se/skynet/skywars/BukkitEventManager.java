package se.skynet.skywars;

import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class BukkitEventManager implements Listener {

    private final GameManger gameManger;
    public BukkitEventManager(GameManger gameManger) {
        this.gameManger = gameManger;
        gameManger.getPlugin().getServer().getPluginManager().registerEvents(this, gameManger.getPlugin());
        gameManger.getPlugin().getServer().getWorlds().get(0).setGameRuleValue("doDaylightCycle", "false");
        gameManger.getPlugin().getServer().getWorlds().get(0).setTime(0);
        gameManger.getPlugin().getServer().getWorlds().get(0).getEntities().stream().forEach(entity -> {
            if(entity instanceof Wither){
                entity.remove();
            }
        });
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
        if (!gameManger.getPlayerManager().getPlayersAlive().contains(event.getPlayer())){
            event.setCancelled(true);
        }
        if(gameManger.getGameState() != GameState.INGAME){
            event.setCancelled(true);
        }
    }

    // prevent block placing
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!gameManger.getPlayerManager().getPlayersAlive().contains(event.getPlayer())){
            event.setCancelled(true);
        }
        if(gameManger.getGameState() != GameState.INGAME){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        if (!gameManger.getPlayerManager().getPlayersAlive().contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }

}
