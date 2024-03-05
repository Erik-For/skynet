package se.skynet.skywars.loot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LootCategory {

    private final int count;
    private final String name;
    private final List<LootItem> items;

    public LootCategory(String name, List<LootItem> items,int min, int max) {
        this.name = name;
        this.items = items;
        this.count = (int) (Math.random() * (max - min + 1) + min);
    }

    public List<ItemStack> getItems() {
        // return unique items
        try {
            List<ItemStack> itemList = new ArrayList<>();
            List<LootItem> itemSet = new ArrayList<>(items);
            for (int i = 0; i < count; i++) {
                LootItem lootItem = itemSet.get((int) (Math.random() * itemSet.size()));
                itemSet.remove(lootItem);
                itemList.add(lootItem.build());
            }
            return itemList;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to get items from " + name);
        }
        return Collections.emptyList();
    }
}
