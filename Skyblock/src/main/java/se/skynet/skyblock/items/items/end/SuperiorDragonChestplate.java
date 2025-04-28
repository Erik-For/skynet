package se.skynet.skyblock.items.items.end;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.playerdata.Stat;

public class SuperiorDragonChestplate extends SkyblockItem implements SkyblockItemEvents {
    public SuperiorDragonChestplate() {
        super(Material.LEATHER_CHESTPLATE, "Superior Dragon Chestplate", SkyblockItemID.SUPERIOR_DRAGON_CHESTPLATE, ItemRarity.LEGENDARY, 1, false);
    }

    public SuperiorDragonChestplate(ItemStack item) {
        super(item);
    }

    @Override
    protected void setupItem() {
        setType(SkyblockItemType.CHESTPLATE);
        setLeatherColor(Color.fromRGB(242, 223, 17));
        setAttribute(Stat.STRENGTH, 10);
        setAttribute(Stat.CRIT_CHANCE, 2);
        setAttribute(Stat.CRIT_DAMAGE, 10);
        setAttribute(Stat.HEALTH, 150);
        setAttribute(Stat.DEFENSE, 190);
        setAttribute(Stat.SPEED, 3);
        setAttribute(Stat.INTELLIGENCE, 25);
    }
}
