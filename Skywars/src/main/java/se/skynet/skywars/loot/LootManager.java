package se.skynet.skywars.loot;

import java.util.ArrayList;
import java.util.List;

public class LootManager {

    private LootTable islandLoot;
    private LootTable midLoot;

    private List<LootLocationSet> islandLocations = new ArrayList<>();
    private List<LootLocationSet> midLocations = new ArrayList<>();

    public LootManager() {
        islandLoot = new LootTable();
        midLoot = new LootTable();
    }

    public void addIslandLootCategory(LootCategory lootCategory) {
        islandLoot.addLootCategory(lootCategory);
    }

    public void addMidLootCategory(LootCategory lootCategory) {
        midLoot.addLootCategory(lootCategory);
    }

    public void addIslandLocations(LootLocationSet islandLocations) {
        this.islandLocations.add(islandLocations);
    }

    public void addMidLocations(LootLocationSet midLocations) {
        this.midLocations.add(midLocations);
    }

    public void fillChests() {
        islandLoot.fillChests(islandLocations);
        midLoot.fillChests(midLocations);
    }
}
