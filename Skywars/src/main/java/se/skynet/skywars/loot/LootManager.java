package se.skynet.skywars.loot;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class LootManger {

    private final Set<Location> islandChests = new HashSet<>();
    private final Set<Location> middleChests = new HashSet<>();

    private final LootTable islandLootTable = new LootTable();
    private final LootTable middleLootTable = new LootTable();

    public void addIslandChest(Location location) {
        islandChests.add(location);
    }

    public void addMiddleChest(Location location) {
        middleChests.add(location);
    }

    public LootTable getIslandLootTable() {
        return islandLootTable;
    }

    public LootTable getMiddleLootTable() {
        return middleLootTable;
    }

    public void fillChests() {
        int itemCount = 5;
        for (Location location : islandChests) {
            if(location.getBlock().getType() == Material.CHEST) {
                Chest chest = (Chest) location.getBlock().getState();
                Iterator<ItemStack> itemStackIterator = getIslandLootTable().generateItems(itemCount).iterator();
                PrimitiveIterator.OfInt iterator = Arrays.stream(ThreadLocalRandom.current().ints(0, chest.getInventory().getSize() - 1).distinct().limit(itemCount).toArray()).iterator();
                while (iterator.hasNext()) {
                    chest.getInventory().setItem(iterator.next(), itemStackIterator.next());
                }
            }
        }
        for (Location location : middleChests) {
            if(location.getBlock().getType() == Material.CHEST){
                Chest chest = (Chest) location.getBlock().getState();
                Iterator<ItemStack> itemStackIterator = getMiddleLootTable().generateItems(itemCount).iterator();
                PrimitiveIterator.OfInt iterator = Arrays.stream(ThreadLocalRandom.current().ints(0, chest.getInventory().getSize() - 1).distinct().limit(itemCount).toArray()).iterator();
                while (iterator.hasNext()) {
                    chest.getInventory().setItem(iterator.next(), itemStackIterator.next());
                }
            }
        }
    }
}
