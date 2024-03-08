package se.skynet.skyserverbase.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import se.skynet.skyserverbase.SkyServerBase;

public class LobbyManager implements Listener {

    private final SkyServerBase plugin;
    private boolean isLobby = false;

    public LobbyManager(SkyServerBase plugin) {
        this.plugin = plugin;
        this.isLobby = System.getenv("SERVER_TYPE").equalsIgnoreCase("LOBBY");
        System.out.println("Server type: " + (isLobby ? "LOBBY" : "GAME"));
    }

    @EventHandler
    public void damageEvent(EntityDamageEvent event) {
        if (isLobby) {
            if (event.getEntity()., instanceof Player) {
                event.setCancelled(true);
                if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    event.getEntity().teleport(event.getEntity().getWorld().getSpawnLocation());
                }
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (isLobby) {
            if(!plugin.getPlayerDataManager().getPlayerData(event.getPlayer().getUniqueId()).getPermissonAttachment().getPermissions().containsKey("skynet.build")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        if (isLobby) {
            if(!plugin.getPlayerDataManager().getPlayerData(event.getPlayer().getUniqueId()).getPermissonAttachment().getPermissions().containsKey("skynet.build")){
                event.setCancelled(true);
            }
        }
    }

}
