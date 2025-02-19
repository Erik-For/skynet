package se.skynet.skywars.manager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import se.skynet.skywars.Game;
import se.skynet.skywars.GameState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class CageManager implements Listener {

    private final Game game;
    private final Set<Location> spawnLocations = new HashSet<>();
    private final HashMap<UUID, Location> usedLocations = new HashMap<>();

    public CageManager(Game game) {
        this.game = game;
        game.getPlugin().getServer().getPluginManager().registerEvents(this, game.getPlugin());
    }

    public void test(EntityDamageByEntityEvent event) {

        // get the player that shot the arrow

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(game.getGameState() != GameState.WAITING) {
            player.setGameMode(org.bukkit.GameMode.SPECTATOR);
            return;
        }
        Iterator<Location> iterator = spawnLocations.iterator();
        if(spawnLocations.size() == usedLocations.size()) {
            player.kickPlayer("The game is full");
            return;
        }

        while (iterator.hasNext()) {
            Location location = iterator.next();
            if (!usedLocations.containsValue(location)) {
                usedLocations.put(player.getUniqueId(), location);
                player.teleport(location);
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        usedLocations.remove(player.getUniqueId());
    }


    public void addSpawnLocation(Location location) {
        spawnLocations.add(location);
    }

    public void destroyCages() {
        spawnLocations.forEach(location -> {
            // remove 5x5x5 area around the location
            for (int x = -2; x <= 2; x++) {
                for (int y = -1; y <= 4; y++) {
                    for (int z = -2; z <= 2; z++) {
                        location.clone().add(x, y, z).getBlock().setType(Material.AIR);
                    }
                }
            }
        });
    }
}
