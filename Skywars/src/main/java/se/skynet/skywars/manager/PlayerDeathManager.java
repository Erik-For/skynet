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
import org.bukkit.event.player.PlayerQuitEvent;
import se.skynet.skywars.Game;
import se.skynet.skywars.GameState;
import se.skynet.skywars.SkywarsPlayer;
import se.skynet.skywars.Tag;

import java.util.HashMap;
import java.util.UUID;

// this class handles damage related events
// it also handles tagging of the player to know who is responsible for killing who
public class PlayerDeathManager implements Listener {

    private final Game game;

    public PlayerDeathManager(Game game) {
        this.game = game;
        game.getPlugin().getServer().getPluginManager().registerEvents(this, game.getPlugin());
    }

    // cant damage players if not in game
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if(game.getGameState() != GameState.IN_GAME) {
            event.setCancelled(true);
            return;
        }
    }

    // if a player is damaged by another player, the tag is updated
    @EventHandler
    public void onPlayerDamageByAnotherPlayer(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player) ) return;

        Player target = (Player) event.getEntity();
        Player source = (Player) event.getDamager();

        Tag tag = new Tag(source.getUniqueId(), target.getUniqueId(), 7);
        game.getPlayerManager().getPlayer(target.getUniqueId()).setLatestTag(tag);
    }

    // if a player dies, the tag is used to determine who killed who
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(player.getHealth() - event.getFinalDamage() > 0) return;
        event.setCancelled(true);

        SkywarsPlayer skywarsPlayer = game.getPlayerManager().getPlayer(player.getUniqueId());
        SkywarsPlayer tagger = game.getPlayerManager().getPlayer(skywarsPlayer.getLatestTag().getSource());

        killPlayer(tagger, skywarsPlayer);

        player.setHealth(20);
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(new Location(player.getWorld(), 0, 150, 0));
        game.getPlayerVisibilityManager().hidePlayer(player);
    }


    // if a player leaves the game, the tag is used to punish the player for combat logging, ew
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(game.getGameState() != GameState.IN_GAME) return;
        SkywarsPlayer skywarsPlayer = game.getPlayerManager().getPlayer(player.getUniqueId());
        if(!skywarsPlayer.isAlive()) return;

        skywarsPlayer.setAlive(false);
        killPlayer(
                game.getPlayerManager().getPlayer(skywarsPlayer.getLatestTag().getSource()),
                game.getPlayerManager().getPlayer(player.getUniqueId())
                );
    }

    private void killPlayer(SkywarsPlayer tagger, SkywarsPlayer player){
        player.setAlive(false);
        if(tagger != null){
            tagger.addKill();
            game.getPlugin().getServer().broadcastMessage(
                    ChatColor.RED + player.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.RED + tagger.getName());
        } else {
            game.getPlugin().getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " Died");
        }

        if(game.getPlayerManager().getPlayersAlive().size() == 1){
            game.setGameState(GameState.END);
        }
    }
}
