package se.skynet.skywars.loot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootTable {

    private final List<OldLootItem> lootItems  = new ArrayList<>();

    public void addLootItem(OldLootItem item){
        lootItems.add(item);
    }

    public List<ItemStack> generateItems(int count) {
        List<ItemStack> items = new ArrayList<>();
        if(lootItems.isEmpty()){
            return items;
        }
        int i = 1;
        while(i <= count){
            // random element in lootItems
            OldLootItem item = lootItems.get((int) (Math.random() * lootItems.size()));
            if(item.shouldDrop()){
                ItemStack itemStack = item.constructItemStack();
                if(itemStack.getType() != Material.AIR){
                    items.add(itemStack);
                    i++;
                }
            }
        }

        return items;
    }

}
