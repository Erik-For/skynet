package se.skynet.skyblock.items.items.nether;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.items.items.Ability;
import se.skynet.skyblock.playerdata.Stat;

public class EmberRod extends SkyblockItem implements SkyblockItemEvents {


    public EmberRod() {
        super(Material.BLAZE_ROD, "Ember Rod", SkyblockItemType.EMBER_ROD, ItemRarity.EPIC, 1, false);
    }

    public EmberRod(ItemStack item) {
        super(item);
    }

    protected void setupItem() {

        setAttribute(Stat.DAMAGE, 80);
        setAttribute(Stat.STRENGTH, 35);
        setAttribute(Stat.CRIT_DAMAGE, 50);
        setAttribute(Stat.INTELLIGENCE, 50);

        // TODO add calculation based on ability
        addAbility(new ItemAbility(Ability.EMBER_ROD_FIRE_BLAST, new AbilityImplementation() {
            @Override
            public boolean onUse(AbilityAction action, Ability ability, SkyblockItem item, SkyblockPlayer player, ItemStack itemStack) {
                final int[] i = {0};
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (i[0] > 2) {
                            cancel();
                            return;
                        }
                        shootFireBall(player.getPlayer());
                        i[0]++;
                    }

                }.runTaskTimer(Skyblock.getInstance(), 0L, 7L);
                return true;
            }
        }));
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
