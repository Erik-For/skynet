package se.skynet.skyserverbase.playerdata;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.database.DatabaseMethods;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager implements Listener {

    private SkyServerBase plugin;
    private HashMap<UUID, CustomPlayerData> playerData = new HashMap<>();

    public PlayerDataManager(SkyServerBase plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    public CustomPlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DatabaseMethods cursor = new DatabaseMethods(plugin.getDatabaseConnectionManager());
        CustomPlayerData data = cursor.getPlayerData(player.getUniqueId());

        if (data == null) {
            event.getPlayer().kickPlayer("An error occurred while fetching your player data");
        }

        data.setPermissonAttachment(player.addAttachment(plugin));
        if(data.getRank().hasPriorityHigherThanOrEqual(Rank.ADMIN)){
            player.setOp(true);
        }
        playerData.put(player.getUniqueId(), data);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().setOp(false);
        playerData.remove(event.getPlayer().getUniqueId());
    }
}
