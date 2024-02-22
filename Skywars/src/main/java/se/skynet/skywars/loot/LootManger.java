package se.skynet.skywars.loot;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import se.skynet.skywars.GameManger;

import java.util.HashSet;
import java.util.Set;

public class LootManger {

    private final GameManger manger;
    private final Set<Location> islandChests = new HashSet<>();
    private final Set<Location> middleChests = new HashSet<>();

    private final LootTable islandLootTable = new LootTable();
    private final LootTable middleLootTable = new LootTable();

    public LootManger(GameManger manger) {
        this.manger = manger;
    }

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
        for (Location location : islandChests) {
            if(location.getBlock().getType() == Material.CHEST){
                Chest chest = (Chest) location.getBlock().getState();
                getIslandLootTable().generateItems(5).forEach(item -> chest.getInventory().addItem(item));
            }
        }
        for (Location location : middleChests) {
            if(location.getBlock().getType() == Material.CHEST){
                Chest chest = (Chest) location.getBlock().getState();
                getMiddleLootTable().generateItems(5).forEach(item -> chest.getInventory().addItem(item));
            }
        }
    }
}
