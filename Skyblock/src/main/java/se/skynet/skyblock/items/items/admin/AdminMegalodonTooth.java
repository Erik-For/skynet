package se.skynet.skyblock.items.items.admin;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.items.ItemRarity;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyblock.items.SkyblockItemID;
import se.skynet.skyblock.playerdata.Stat;

public class AdminMegalodonTooth extends SkyblockItem {
    public AdminMegalodonTooth() {
        super(Material.SKULL_ITEM, "Megalodon Tooth", SkyblockItemID.ADMIN_MEGALODON_TOOTH, ItemRarity.ADMIN, 1, false);
    }

    public AdminMegalodonTooth(ItemStack item) {
        super(item);
    }

    @Override
    protected void setupItem() {
        setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjBjODQzMjBmNGIwZmNkODM5MWI4YTIwZjUzYmQ0MTYxMmNhNmE1MGU3NTgzMDk1MjU5NzFmNzdhY2UyZCJ9fX0=");
        setAttribute(Stat.CRIT_CHANCE, 1000);
    }
}
