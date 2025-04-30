package se.skynet.skyblock.items.items.admin;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.items.ItemRarity;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyblock.items.SkyblockItemID;
import se.skynet.skyblock.items.SkyblockItemType;
import se.skynet.skyblock.playerdata.Stat;

public class AdminTailsman extends SkyblockItem {


    public AdminTailsman() {
        super(Material.SKULL_ITEM, "Admin Tailsman", SkyblockItemID.ADMIN_TAILSMAN, ItemRarity.ADMIN, 1, false);
    }

    public AdminTailsman(ItemStack item) {
        super(item);
    }

    @Override
    protected void setupItem() {
        setAttribute(Stat.CRIT_CHANCE, 1000);
        setAttribute(Stat.STRENGTH, 1000);
        setAttribute(Stat.CRIT_DAMAGE, 1000);
        setType(SkyblockItemType.TAILSMAN);
        setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTc3YzljNjM4YmYzZGNkYTM0OGVkZWE0NGU5YTNkYjRhYmMxZTIzOTU1ODY2MTYxMWY4MGMxMTA0NzJhZCJ9fX0=");
    }
}
