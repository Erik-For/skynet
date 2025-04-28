package se.skynet.skyblock.items.items.end;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.playerdata.Stat;

public class SuperiorDragonBoots extends SkyblockItem implements SkyblockItemEvents {
    public SuperiorDragonBoots() {
        super(Material.LEATHER_BOOTS, "Superior Dragon Boots", SkyblockItemID.SUPERIOR_DRAGON_BOOTS, ItemRarity.LEGENDARY, 1, false);
    }

    public SuperiorDragonBoots(ItemStack item) {
        super(item);
    }

    @Override
    protected void setupItem() {
        setType(SkyblockItemType.BOOTS);
        setLeatherColor(Color.fromRGB(242, 93, 24));
        setAttribute(Stat.STRENGTH, 10);
        setAttribute(Stat.CRIT_CHANCE, 2);
        setAttribute(Stat.CRIT_DAMAGE, 10);
        setAttribute(Stat.HEALTH, 80);
        setAttribute(Stat.DEFENSE, 110);
        setAttribute(Stat.SPEED, 3);
        setAttribute(Stat.INTELLIGENCE, 25);
    }
}
