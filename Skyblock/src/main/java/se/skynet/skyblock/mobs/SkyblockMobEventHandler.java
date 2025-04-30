package se.skynet.skyblock.mobs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.util.Vector;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.Util;
import se.skynet.skyblock.misc.HologramHelper;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SkyblockMobEventHandler implements Listener {

    private final Skyblock plugin;
    public SkyblockMobEventHandler(Skyblock plugin) {
        this.plugin = plugin;
        // Register the event listener
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event instanceof EntityDamageByEntityEvent) {
            return;
        }
        if(!SkyblockMob.isWrapped(event.getEntity())) {
            return;
        }
        int damage = (int) event.getDamage();
        event.setDamage(0);
        SkyblockMob entity = new SkyblockMob((LivingEntity) event.getEntity());
        entity.setHealth(entity.getHealth() - damage);

        HologramHelper.createHologramTemporary(entity.getHandle().getLocation().add(new Vector(0, 1, 0)), Util.formatDamage(damage, false), 20, plugin);
    }

    @EventHandler
    public void onDamageByPlayer(EntityDamageByEntityEvent event) {
        if(!(SkyblockMob.isSkyblockMob(event.getEntity()))) { return; }
        SkyblockMob entity = SkyblockMob.getMob(event.getEntity());
        if(!(event.getDamager() instanceof Player)) {
            entity.setHealth(entity.getHealth() - (int) event.getDamage());
            event.setDamage(0);
            HologramHelper.createHologramTemporary(entity.getHandle().getLocation().add(new Vector(0, 1, 0)), Util.formatDamage((int) event.getDamage(), false), 20, plugin);
            return;
        }

        SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer((Player) event.getDamager());

        boolean crit = player.shouldCrit();
        double damage = player.calculateDamage(crit);

        entity.setHealth(entity.getHealth() - (int) damage);
        event.setDamage(0);
        HologramHelper.createHologramTemporary(entity.getHandle().getLocation().add(new Vector(0, 1, 0)), Util.formatDamage((int) damage, crit), 20, plugin);
    }

    @EventHandler
    public void onLivingEntitySpawn(EntitySpawnEvent event) {
        if(event.getEntity() instanceof LivingEntity && !SkyblockMob.isWrapped(event.getEntity()) && event.getEntity().getType() !=
                EntityType.PLAYER && event.getEntity().getType() != EntityType.ARMOR_STAND) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            // Wrap the entity in a CustomMob
            SkyblockMob customMob = new SkyblockMob(entity, (int) entity.getMaxHealth(), entity.getName(), 1, creature -> {});
        }
    }
}
