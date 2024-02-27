package se.skynet.skywars.manager;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import se.skynet.skywars.Game;
import se.skynet.skywars.GameState;
import se.skynet.skywars.SkywarsPlayer;

import java.util.HashMap;
import java.util.UUID;

// this class handles damage related events
// it also handles tagging of the player to know who is responsible for killing who
public class PlayerDeathManager implements Listener {

    private final Game game;
    private final HashMap<UUID, Tag> tagedPlayers = new HashMap<>();
    private final HashMap<UUID, SkywarsPlayer> playerKillsMap = new HashMap<>();

    public PlayerDeathManager(Game game) {
        this.game = game;
        game.getPlugin().getServer().getPluginManager().registerEvents(this, game.getPlugin());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if(game.getGameState() != GameState.IN_GAME) {
            event.setCancelled(true);
            return;
        }
        if(!game.getPlayerVisibilityManager().isShown(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageByAnotherPlayer(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player) ) return;

        Player target = (Player) event.getEntity();
        Player source = (Player) event.getDamager();

        Tag tag = new Tag(source.getUniqueId(), target.getUniqueId(), 7);
        tagedPlayers.put(target.getUniqueId(), tag);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(player.getHealth() - event.getFinalDamage() > 0) return;

        event.setCancelled(true);
        Player tagger = this.getTagger(player);
        if(tagger != null){
            if(playerKillsMap.containsKey(tagger.getUniqueId())){
                playerKillsMap.get(tagger.getUniqueId()).addKill();
            } else {
                SkywarsPlayer skywarsPlayer = new SkywarsPlayer(tagger, game);
                skywarsPlayer.addKill();
                playerKillsMap.put(tagger.getUniqueId(), skywarsPlayer);
            }
            game.getPlugin().getServer().broadcastMessage(
                    ChatColor.RED + player.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.RED + tagger.getName());
        } else {
            game.getPlugin().getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " Died");
        }

        player.setHealth(20);
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(new Location(player.getWorld(), 0, 150, 0));
        game.getPlayerVisibilityManager().hidePlayer(player);
    }

    private Player getTagger(Player player){
        Tag tag = tagedPlayers.get(player.getUniqueId());
        if(tag == null || !tag.isValid()) return null;
        return game.getPlugin().getServer().getPlayer(tag.getSource());
    }

    public static class Tag {

        private final UUID source;
        private final UUID target;
        private final long time;
        private final long expireTime;

        private Tag(UUID source, UUID target, long expireTimeSeconds) {
            this.source = source;
            this.target = target;
            this.time = System.currentTimeMillis();
            this.expireTime = expireTimeSeconds * 1000L;
        }

        public UUID getSource() {
            return source;
        }

        public UUID getTarget() {
            return target;
        }

        public boolean isValid(){
            return System.currentTimeMillis() - time < expireTime;
        }
    }

    public HashMap<UUID, SkywarsPlayer> getPlayerKills() {
        return playerKillsMap;
    }
}
