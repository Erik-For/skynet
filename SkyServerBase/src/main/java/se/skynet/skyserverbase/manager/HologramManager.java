package se.skynet.skyserverbase.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class HologramManager {
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


    public static void destroyHologram(int entityId) {
        Bukkit.getServer().getWorlds().forEach(world -> {
            world.getEntities().stream().filter(e -> e.getEntityId() == entityId && e instanceof ArmorStand).forEach(Entity::remove);
        });
    }

    public static void modifyHologram(int entityId, String text) {
        Bukkit.getServer().getWorlds().forEach(world -> {
            world.getEntities().stream().filter(e -> e.getEntityId() == entityId && e instanceof ArmorStand).forEach(e -> {
                e.setCustomName(text);
            });
        });
    }

}
