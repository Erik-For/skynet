package se.skynet.skywars.loot;


import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootTable {

    private final List<LootCategory> lootCategories = new ArrayList<>();

    public LootTable() {
    }

    public void addLootCategory(LootCategory lootCategory) {
        lootCategories.add(lootCategory);
    }

    public void fillChests(List<LootLocationSet> lootLocationSets) {
        for(LootLocationSet lootLocationSet : lootLocationSets) {
            List<ItemStack> items = new ArrayList<>();
            for(LootCategory lootCategory : lootCategories) {
                List<ItemStack> items1 = lootCategory.getItems();
                items.addAll(items1);
            }
            lootLocationSet.fillChests(items);
        }
    }

}
