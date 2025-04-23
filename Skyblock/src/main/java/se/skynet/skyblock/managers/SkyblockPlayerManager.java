package se.skynet.skyblock.managers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.playerdata.PlayerProfile;

import java.util.HashMap;
import java.util.UUID;

public class SkyblockPlayerManager implements Listener {

    private final HashMap<UUID, SkyblockPlayer> skyblockPlayers = new HashMap<>();
    private final Skyblock plugin;
    public SkyblockPlayerManager(Skyblock plugin) {
        this.plugin = plugin;
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
}
