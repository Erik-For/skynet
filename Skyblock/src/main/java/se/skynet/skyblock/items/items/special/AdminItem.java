package se.skynet.skyblock.items.items.special;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.items.ItemRarity;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyblock.items.SkyblockItemEvents;
import se.skynet.skyblock.items.SkyblockItemID;

public class AdminItem extends SkyblockItem implements SkyblockItemEvents {
    public AdminItem() {
        super(Material.NAME_TAG, "Admin Item", SkyblockItemID.ADMIN_ITEM, ItemRarity.SPECIAL, 1, false);
    }

    public AdminItem(ItemStack item) {
        super(item);
    }

    @Override
    protected void setupItem() {
        addLore(ChatColor.GRAY + "This was an admin item but");
        addLore(ChatColor.GRAY + "it was deleted because it was");
        addLore(ChatColor.GRAY + "obtained by a non admin");
    }
}
