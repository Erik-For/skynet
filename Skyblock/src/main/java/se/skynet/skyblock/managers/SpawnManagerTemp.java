package se.skynet.skyblock.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.mobs.Mob;
import se.skynet.skyblock.mobs.SkyblockMob;

import java.util.*;

import static org.json.XMLTokener.entity;

public class SpawnManagerTemp implements Listener {

    private final Skyblock skyblock;
    private Map<UUID, SpawnLocation> spawnLocations = new HashMap<>();
    private Map<Integer, UUID> entitySpawnLocations = new HashMap<>();
    public class SpawnLocation {
        private List<Entity> entityList = new ArrayList<>();
        private Location location;
        private int entityCap;

        public SpawnLocation(Location location, int entityCap) {
            this.location = location;
            this.entityCap = entityCap;
        }

        public void addEntity(Entity entity) {
            if (entityList.size() < entityCap) {
                entityList.add(entity);
            }
        }
    }
    public SpawnManagerTemp(Skyblock skyblock) {
        this.skyblock =skyblock;

        Location loc1 = new Location(skyblock.getServer().getWorld("world"), -100,75, -50);
        Location loc2 = new Location(skyblock.getServer().getWorld("world"), -180,75, -150);

        // 100 random points between loc1 and loc2


        for (int i = 0; spawnLocations.size() < 15 && i < 1000; i++) {
            double x = loc1.getX() + Math.random() * (loc2.getX() - loc1.getX());
            double y = loc1.getY() + Math.random() * (loc2.getY() - loc1.getY());
            double z = loc1.getZ() + Math.random() * (loc2.getZ() - loc1.getZ());
            Location randomLocation = new Location(loc1.getWorld(), x, y, z);
            for (int j = 0; j < 10; j++) {
                Location add = randomLocation.add(0, -1, 0);
                if(add.getBlock().getType() == Material.GRASS){
                    spawnLocations.put(UUID.randomUUID(), new SpawnLocation(randomLocation.add(0, 1.5, 0), 2));
                    break;
                }
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, SpawnLocation> entry : spawnLocations.entrySet()) {
                    SpawnLocation spawnLocation = entry.getValue();
                    if (spawnLocation.entityList.size() < spawnLocation.entityCap) {
                        // spawn entity
                        SkyblockMob skyblockMob = SkyblockMob.spawnMob(Mob.GRAVEYARD_ZOMBIE, spawnLocation.location);
                        spawnLocation.addEntity(skyblockMob.getHandle());
                        entitySpawnLocations.put(skyblockMob.getHandle().getEntityId(), entry.getKey());
                    }
                }
            }
        }.runTaskTimer(skyblock, 0, 100L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void entityKill(EntityDeathEvent event) {
        if(entitySpawnLocations.containsKey(event.getEntity().getEntityId())) {
            UUID spawnLocationUUID = entitySpawnLocations.get(event.getEntity().getEntityId());
            SpawnLocation spawnLocation = spawnLocations.get(spawnLocationUUID);
            if (spawnLocation != null) {
                spawnLocation.entityList.remove(event.getEntity());
                entitySpawnLocations.remove(event.getEntity().getEntityId());
            }
        }
    }
}
