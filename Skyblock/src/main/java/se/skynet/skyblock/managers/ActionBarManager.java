package se.skynet.skyblock.managers;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.playerdata.Stat;
import se.skynet.skyserverbase.util.ScoreboardHelper;

public class ActionBarManager implements Listener {

    private final Skyblock skyblock;

    public ActionBarManager(Skyblock skyblock) {
        this.skyblock = skyblock;

        new BukkitRunnable() {
            @Override
            public void run() {
                renderAll();
            }
        }.runTaskTimer(skyblock, 0, 20L);
    }

    private void renderAll() {
        skyblock.getPlayerManager().getSkyblockPlayers().values().forEach(this::render);
    }
    private void render(SkyblockPlayer player) {
        // Send action bar to player
        int max_hp = player.calculateStatMax(Stat.HEALTH);
        int max_defense = player.calculateStatMax(Stat.DEFENSE);
        int max_mana = player.calculateStatMax(Stat.INTELLIGENCE);

        int hp = player.getStat(Stat.HEALTH);
        int defense = player.getStat(Stat.DEFENSE);
        int mana = player.getStat(Stat.INTELLIGENCE);

        ScoreboardHelper.sendActionBar(player.getPlayer(), ChatColor.RED + "" + hp + "/" + max_hp + "❤          " +
                ChatColor.GREEN + max_defense + "✦           " +
                ChatColor.AQUA + mana + "/" + max_mana + "✎");
    }
}
