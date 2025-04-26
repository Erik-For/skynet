package se.skynet.skyblock.managers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.mobs.SkyblockMob;
import se.skynet.skyblock.playerdata.PlayerProfile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class SkyblockPlayerManager implements Listener {

    private final HashMap<UUID, SkyblockPlayer> skyblockPlayers = new HashMap<>();
    private final Skyblock plugin;
    public SkyblockPlayerManager(Skyblock plugin) {
        this.plugin = plugin;

        new BukkitRunnable() {
            @Override
            public void run() {
                skyblockPlayers.forEach((uuid, skyblockPlayer) -> {skyblockPlayer.tick(); });
            }
        }.runTaskTimer(plugin, 0, 10L);
    }


    // wrap the player in a SkyblockPlayer object
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!plugin.getDatabaseMethods().hasProfile(player.getUniqueId())) {
            player.sendMessage("Creating profile...");
            plugin.getDatabaseMethods().createProfile(player.getUniqueId());
        } else {
            player.sendMessage("Loading profile...");
        }

        PlayerProfile playerProfile = plugin.getDatabaseMethods().loadProfile(player.getUniqueId());
        if(playerProfile == null) {
            player.sendMessage("Failed to load profile.");
            return;
        }
        SkyblockPlayer skyblockPlayer = new SkyblockPlayer(player, playerProfile);
        // add the player to the list of players
        skyblockPlayers.put(player.getUniqueId(), skyblockPlayer);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SkyblockPlayer skyblockPlayer = skyblockPlayers.get(player.getUniqueId());

        plugin.getDatabaseMethods().saveProfileData(skyblockPlayer.getProfile());
        // remove the player from the list of players

        //skyblockPlayers.remove(player.getUniqueId());
    }

    public SkyblockPlayer getSkyblockPlayer(Player player) {
        return skyblockPlayers.get(player.getUniqueId());
    }

    public HashMap<UUID, SkyblockPlayer> getSkyblockPlayers() {
        return skyblockPlayers;
    }

    @EventHandler
    public void playerDamageEvent(EntityDamageEvent event) {
        if(event instanceof EntityDamageByEntityEvent) return;
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        SkyblockPlayer skyblockPlayer = skyblockPlayers.get(player.getUniqueId());

        double originalDamage = event.getDamage();
        event.setDamage(0);

        EntityDamageEvent.DamageCause[] trueDamageCauses = {
                EntityDamageEvent.DamageCause.FALL,
                EntityDamageEvent.DamageCause.LAVA,
                EntityDamageEvent.DamageCause.STARVATION,
                EntityDamageEvent.DamageCause.SUFFOCATION,
                EntityDamageEvent.DamageCause.VOID,
                EntityDamageEvent.DamageCause.WITHER
        };
        EntityDamageEvent.DamageCause cause = event.getCause();
        boolean trueDamage = Arrays.stream(trueDamageCauses).anyMatch(trueDamageCause -> trueDamageCause == cause);

        skyblockPlayer.damage(originalDamage, trueDamage);
    }

    @EventHandler
    public void playerDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        SkyblockPlayer skyblockPlayer = skyblockPlayers.get(player.getUniqueId());

        double originalDamage = event.getDamage();
        event.setDamage(0);

        Entity damager = event.getDamager();
        if(!SkyblockMob.isSkyblockMob(damager)) {
            skyblockPlayer.damage(originalDamage, false);
            return;
        }
        SkyblockMob mob = SkyblockMob.getMob(damager);
        boolean b = mob.getType().doesTrueDamage();

        skyblockPlayer.damage(originalDamage, b);
    }
}
