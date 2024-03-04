package se.skynet.skywars.loot;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import se.skynet.skywars.util.CircularIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LootLocationSet {
    private final List<Location> islandChests;

    public LootLocationSet(List<Location> islandChests) {
        this.islandChests = islandChests;
    }

    public void fillChests(List<ItemStack> items){
        Iterator<ItemStack> itemIterator = items.iterator();
        CircularIterator<Location> locations = new CircularIterator<>(islandChests);
        while (true){
            if(!itemIterator.hasNext()){
                break;
            }
            Location loc = locations.next();
            ItemStack item = itemIterator.next();
            if(loc.getBlock().getType() == Material.CHEST){
                Chest chest = (Chest) loc.getBlock().getState();
                chest.getInventory().addItem(item);
            }
        }
    }

}
