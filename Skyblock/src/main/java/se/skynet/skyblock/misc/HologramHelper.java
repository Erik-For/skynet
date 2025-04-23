package se.skynet.skyblock.misc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import se.skynet.skyblock.Skyblock;

public class HologramHelper {


    public static int createHologram(Location loc, String text) {
        World world = loc.getWorld();
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
        //armorStand.teleport(loc);
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
        armorStand.setMarker(true);

        return armorStand.getEntityId();
    }

    public static void destroyHologram(int entityId, Skyblock plugin) {
        plugin.getServer().getWorlds().forEach(world -> {
            world.getEntities().stream().filter(e -> e.getEntityId() == entityId && e instanceof ArmorStand).forEach(Entity::remove);
        });
    }

    public static void createHologramTemporary(Location loc, String text, int duration, Skyblock plugin) {
        int entityId = createHologram(loc, text);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> destroyHologram(entityId, plugin), duration);
    }


}
