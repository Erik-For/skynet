package se.skynet.skywars;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class CageManager implements Listener {

    private final List<Location> cageLocations = new ArrayList<>();
    private final GameManger gameManger;

    public CageManager(GameManger gameManger) {
        this.gameManger = gameManger;
        gameManger.getPlugin().getServer().getPluginManager().registerEvents(this, gameManger.getPlugin());
    }

    public void addCageLocation(Location location){
        cageLocations.add(location);
    }

    public void removeCages(){
        cageLocations.forEach(location -> {
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
