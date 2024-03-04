package se.skynet.skywars.manager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import se.skynet.skywars.Game;

public class HealthDispalyManager implements Listener {

    private final Game game;

    public HealthDispalyManager(Game game) {
        this.game = game;
        game.getPlugin().getServer().getPluginManager().registerEvents(this, game.getPlugin());
        Scoreboard mainScoreboard = game.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        Objective showHealthList = mainScoreboard.registerNewObjective("showhealthlist", "dummy");
        showHealthList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        Objective showHealthHead = mainScoreboard.registerNewObjective("showhealthhead", Criterias.HEALTH);
        showHealthHead.setDisplayName(ChatColor.DARK_RED + "❤");
        showHealthHead.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        game.getPlugin().getServer().getScoreboardManager().getMainScoreboard().getObjective("showhealthlist").getScore(player.getName()).setScore((int) player.getHealth());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (org.bukkit.entity.Player) event.getEntity();
            game.getPlugin().getServer().getScoreboardManager().getMainScoreboard().getObjective("showhealthlist").getScore(player.getName()).setScore((int) player.getHealth());
        }
    }
}
