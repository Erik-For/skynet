package se.skynet.skynetproxy.playerdata;


import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.database.DatabaseMethods;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager implements Listener {

    private SkyProxy plugin;
    private HashMap<UUID, CustomPlayerData> playerData = new HashMap<>();

    public PlayerDataManager(SkyProxy plugin) {
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    public CustomPlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PostLoginEvent event){
        ProxiedPlayer player = event.getPlayer();
        DatabaseMethods cursor = new DatabaseMethods(plugin.getDatabaseConnectionManager());
        CustomPlayerData data = cursor.getPlayerRank(player.getUniqueId());
        playerData.put(player.getUniqueId(), data);
        player.addGroups(data.getRank().toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerDisconnectEvent event){
        ProxiedPlayer player = event.getPlayer();
        for (String group : player.getGroups()) {
            player.removeGroups(group);
        }
        playerData.remove(player.getUniqueId());
    }
}
