package se.skynet.skywars;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TagManager implements Listener {

    private final GameManger gameManager;

    private final Map<UUID, Tag> taggedPlayers = new HashMap<>();

    public TagManager(GameManger gameManager) {
        this.gameManager = gameManager;
        gameManager.getPlugin().getServer().getPluginManager().registerEvents(this, gameManager.getPlugin());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void entityDamageEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (!gameManager.getPlayerManager().getPlayersAlive().contains(damager)) {
                event.setCancelled(true);
                return;
            }
            Player damaged = (Player) event.getEntity();
            if(gameManager.getGameState() == GameState.INGAME){
                Tag tag = new Tag(damager);
                taggedPlayers.put(damaged.getUniqueId(), tag);
            }
        }
    }

    public Player getTaggedPlayer(Player player){
        Tag tag = taggedPlayers.get(player.getUniqueId());
        if(tag == null) {
            return null;
        }
        if(tag.isVaid()) {
            return tag.getTagger();
        }
        return null;
    }

}
