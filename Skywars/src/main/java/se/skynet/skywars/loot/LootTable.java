package se.skynet.skywars.loot;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootTable {

    private final List<LootItem> lootItems  = new ArrayList<>();

    public void addLootItem(LootItem item){
        lootItems.add(item);
    }

    public List<ItemStack> generateItems(int count) {
        List<ItemStack> items = new ArrayList<>();
        int i = 0;
        while(i <= count){
            // random element in lootItems
            LootItem item = lootItems.get((int) (Math.random() * lootItems.size()));
            if(item.shouldDrop()){
                items.add(item.constructItemStack());
                i++;
            }
        }

        return items;
    }

}
