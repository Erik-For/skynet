package se.skynet.skyblock.items;

import org.bukkit.Material;

public class VanillaItem extends SkyblockItem {


    public VanillaItem(Material material, String name, int amount, int maxStackSize) {
        super(material, name, SkyblockItemType.VANILLA, ItemRarity.COMMON, amount, maxStackSize != 1);

    }

    @Override
    protected void setupItem() {
        
    }
}
