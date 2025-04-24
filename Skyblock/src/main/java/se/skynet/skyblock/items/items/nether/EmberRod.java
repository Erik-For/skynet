package se.skynet.skyblock.items.items.nether;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.playerdata.Stat;

import java.util.Arrays;

public class EmberRod extends SkyblockItem implements SkyblockItemEvents {


    public EmberRod() {
        super(Material.BLAZE_ROD, "Ember Rod", SkyblockItemType.EMBER_ROD, ItemRarity.EPIC, 1, false);

        setAttribute(Stat.DAMAGE, 80);
        setAttribute(Stat.STRENGTH, 35);
        setAttribute(Stat.CRIT_DAMAGE, 50);
        setAttribute(Stat.INTELLIGENCE, 50);

        // TODO add calculation based on ability
        addAbility(new ItemAbility("Fire Blast", "RIGHT CLICK", Arrays.asList(
                "Shoot 3 Fireballs which deal " + ChatColor.RED + "30" + ChatColor.GRAY + " damage in",
                "rapid succession in front of you."
        ), 150, 30));
    }

    @Override
    public void onItemRightClick(SkyblockItem item, PlayerInteractEvent event) {
        Player player = event.getPlayer();

        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (i[0] > 2) {
                    cancel();
                    return;
                }
                shootFireBall(player);
                i[0]++;
            }

        }.runTaskTimer(Skyblock.getInstance(), 0L, 7L);
    }

    private void shootFireBall(Player player) {
        Location fireballLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(2));
        Fireball spawn = player.getWorld().spawn(fireballLocation, Fireball.class);
        spawn.setDirection(player.getLocation().getDirection());
        spawn.setIsIncendiary(false);
        spawn.setYield(0);
        spawn.setShooter(player);
    }


}
