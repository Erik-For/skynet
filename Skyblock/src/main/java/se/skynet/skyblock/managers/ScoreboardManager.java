package se.skynet.skyblock.managers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.Util;
import se.skynet.skyserverbase.util.ScoreboardHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ScoreboardManager implements Listener {

    private final Skyblock plugin;

    public ScoreboardManager(Skyblock plugin) {
        this.plugin = plugin;

        new BukkitRunnable() {
            @Override
            public void run() {
                renderAll();
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        // Create a scoreboard for the player
        SkyblockPlayer skyblockPlayer = plugin.getPlayerManager().getSkyblockPlayer(event.getPlayer());
        if (skyblockPlayer == null) {
            ScoreboardHelper.setScoreboard(skyblockPlayer.getPlayer(), Arrays.asList("Skyblock", "Error loading", "your profie", "please contact", "an admin"));
            return;
        }
        render(skyblockPlayer);
    }

    public void renderAll() {
        for (SkyblockPlayer player : plugin.getPlayerManager().getSkyblockPlayers().values()) {
            render(player);
        }
    }
    public void render(SkyblockPlayer player) {
        if (player!= null) {
            // format "dd/mm/yy "
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

            ScoreboardHelper.setScoreboard(player.getPlayer(), Arrays.asList(
                    ChatColor.YELLOW + "" + ChatColor.BOLD + "Skyblock",
                    ChatColor.GRAY + date,
                    ChatColor.DARK_GRAY + plugin.getParentPlugin().getServerName(),
                    " ",
                    "Purse: " + ChatColor.GOLD + Util.formatNumberWithCommas(player.getProfile().getCoins()),
                    "  ",
                    "Objective: ",
                    ChatColor.YELLOW + "Collect 1000 coins",
                    " " + ChatColor.GOLD + Util.formatNumberShorthand(player.getProfile().getCoins()) + "/" + ChatColor.GOLD + "1000",
                    "   ",
                    ChatColor.YELLOW + "mc.twatter.se"
            ));
        }
    }
}
