package se.skynet.skyblock.items.items.end;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.playerdata.Stat;

import java.util.Arrays;

public class SuperiorDargonHelmet extends SkyblockItem implements SkyblockItemEvents {
    public SuperiorDargonHelmet() {
        super(Material.SKULL_ITEM, "Superior Dragon Helmet", SkyblockItemType.SUPERIOR_DRAGON_HELMET, ItemRarity.LEGENDARY, 1, false);
    }

    public SuperiorDargonHelmet(ItemStack item) {
        super(item);
        //setupItem();
    }

    protected void setupItem() {
        setAttribute(Stat.STRENGTH, 10);
        setAttribute(Stat.CRIT_CHANCE, 2);
        setAttribute(Stat.CRIT_DAMAGE, 10);
        setAttribute(Stat.HEALTH, 90);
        setAttribute(Stat.DEFENSE, 130);
        setAttribute(Stat.SPEED, 3);
        setAttribute(Stat.INTELLIGENCE, 25);

        /*
                addAbility(new ItemAbility("Full set bonus", "Superior Blood", ChatColor.GRAY + "(0/4)", Arrays.asList(
                "Most of your stats are increased by",
                ChatColor.GREEN + "5%" + ChatColor.GRAY + " and " + ChatColor.GOLD + "Aspect of the Dragons" + ChatColor.GRAY + " ability",
                "deals " + ChatColor.GREEN + "50%" + ChatColor.GRAY + " more damage."
        )));
         */

        setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU1OGVmYmU2Njk3NjA5OWNmZDYyNzYwZDllMDUxNzBkMmJiOGY1MWU2ODgyOWFiOGEwNTFjNDhjYmM0MTVjYiJ9fX0=");
    }
}
