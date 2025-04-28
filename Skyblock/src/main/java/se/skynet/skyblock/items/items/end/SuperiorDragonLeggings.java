package se.skynet.skyblock.items.items.end;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.playerdata.Stat;

public class SuperiorDragonLeggings extends SkyblockItem implements SkyblockItemEvents {
    public SuperiorDragonLeggings() {
        super(Material.LEATHER_LEGGINGS, "Superior Dragon Leggings", SkyblockItemID.SUPERIOR_DRAGON_LEGGINGS, ItemRarity.LEGENDARY, 1, false);
    }

    public SuperiorDragonLeggings(ItemStack item) {
        super(item);
    }

    @Override
    protected void setupItem() {
        setType(SkyblockItemType.LEGGINGS);
        setLeatherColor(Color.fromRGB(242, 223, 17));
        setAttribute(Stat.STRENGTH, 10);
        setAttribute(Stat.CRIT_CHANCE, 2);
        setAttribute(Stat.CRIT_DAMAGE, 10);
        setAttribute(Stat.HEALTH, 130);
        setAttribute(Stat.DEFENSE, 170);
        setAttribute(Stat.SPEED, 3);
        setAttribute(Stat.INTELLIGENCE, 25);
    }
}
