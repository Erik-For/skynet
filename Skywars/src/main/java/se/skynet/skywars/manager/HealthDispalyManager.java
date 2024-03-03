package se.skynet.skywars.manager;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
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
        Objective showHealthList = mainScoreboard.registerNewObjective("showhealthlist", Criterias.HEALTH);
        showHealthList.setDisplayName(ChatColor.DARK_RED + "❤");
        showHealthList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        Objective showHealthHead = mainScoreboard.registerNewObjective("showhealthhead", Criterias.HEALTH);
        showHealthHead.setDisplayName(ChatColor.DARK_RED + "❤");
        showHealthHead.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }


}
