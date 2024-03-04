package se.skynet.skywars;

import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.YamlConfiguration;
import se.skynet.skywars.loot.EnchantmentPossibilities;
import se.skynet.skywars.loot.LootCategory;
import se.skynet.skywars.loot.LootItem;
import se.skynet.skywars.loot.LootLocationSet;
import se.skynet.skywars.loot.MaterialVariation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigLoader {

    private final Game game;

    public ConfigLoader(Game game) {
        this.game = game;
        YamlConfiguration lootConfig = new YamlConfiguration();
        YamlConfiguration locationsConfig = new YamlConfiguration();
        File lootTableFile = null;
        File locationsFile = null;

        try {
            lootTableFile = Paths.get(game.getPlugin().getServer().getWorldContainer().getCanonicalPath(), "loot.yml").toFile();
            locationsFile = Paths.get(game.getPlugin().getServer().getWorldContainer().getCanonicalPath(), "/world", "skywars.yml").toFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to get loot table or locations file");
            System.exit(0);
        }
        if (lootTableFile == null || locationsFile == null) {
            System.out.println("Failed to get loot table or locations file");
            System.exit(0);
        }

        try {
            lootConfig.load(lootTableFile);
            locationsConfig.load(locationsFile);
            if(locationsConfig == null || lootConfig == null){
                throw new IOException("Failed to load loot table or locations file");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load loot table or locations file");
            System.exit(0);
        }

        if(locationsConfig.contains("bounds")){
            int xl = locationsConfig.getInt("bounds.xl");
            int yl = locationsConfig.getInt("bounds.yl");
            int ym = locationsConfig.getInt("bounds.ym");
            int zl = locationsConfig.getInt("bounds.zl");

            WorldBorder worldBorder = game.getPlugin().getServer().getWorlds().get(0).getWorldBorder();
            worldBorder.setCenter(0, 0);
            worldBorder.setSize(xl);
        }

        locationsConfig.getConfigurationSection("islands").getKeys(false).forEach(key -> {
            System.out.println(key);
            List<Location> locations = new ArrayList<>();

            ((List<?>) locationsConfig.get("islands." + key)).forEach(value -> {
                Map<?, ?> islandLocation = (Map<?, ?>) value;

                int x = (int) islandLocation.get("x");
                int y = (int) islandLocation.get("y");
                int z = (int) islandLocation.get("z");
                locations.add(new Location(game.getPlugin().getServer().getWorlds().get(0), x, y, z));
            });
            game.getLootManager().addIslandLocations(new LootLocationSet(locations));
        });

        locationsConfig.getConfigurationSection("middle").getKeys(false).forEach(key -> {
            System.out.println(key);
            List<Location> locations = new ArrayList<>();

            ((List<?>) locationsConfig.get("middle." + key)).forEach(value -> {
                Map<?, ?> islandLocation = (Map<?, ?>) value;

                int x = (int) islandLocation.get("x");
                int y = (int) islandLocation.get("y");
                int z = (int) islandLocation.get("z");
                locations.add(new Location(game.getPlugin().getServer().getWorlds().get(0), x, y, z));
            });
            game.getLootManager().addMidLocations(new LootLocationSet(locations));
        });

        locationsConfig.getMapList("spawn").forEach(map -> {
            int x = (int) map.get("x");
            int y = (int) map.get("y");
            int z = (int) map.get("z");
            Location location = new Location(game.getPlugin().getServer().getWorlds().get(0), x + 0.5, y, z + 0.5, 0, 0);
            game.getCageManager().addSpawnLocation(location);
        });

        lootConfig.getMapList("island-loot").forEach(map -> {
            game.getLootManager().addIslandLootCategory(getLootCategory(map));
        });

        lootConfig.getMapList("mid-chests").forEach(map -> {
            game.getLootManager().addMidLootCategory(getLootCategory(map));
        });

        game.getPlugin().getLogger().info("Loaded loot table and locations");
    }

    private LootCategory getLootCategory(Map<?,?> map) {
        String name = (String) map.get("name");
        int min;
        int max;

        if(map.containsKey("count")){
            min = (int) map.get("count");
            max = (int) map.get("count");
        } else if(map.containsKey("min") && map.containsKey("max")) {
            min = (int) map.get("min");
            max =  (int) map.get("max");
        } else {
            min = 1;
            max = 1;
        }
        List<LootItem> lootItems = new ArrayList<>();
        for (Map<?,?> lootItemMap : (List<Map<?,?>>) map.get("items")){

            String material = (String) lootItemMap.get("material");
            int minAmount;
            int maxAmount;
            int step = lootItemMap.containsKey("step") ? (int) lootItemMap.get("step") : 1;

            if(lootItemMap.containsKey("count")){
                minAmount = (int) lootItemMap.get("count");
                maxAmount = (int) lootItemMap.get("count");
            } else if(lootItemMap.containsKey("min") && lootItemMap.containsKey("max")) {
                minAmount = (int) lootItemMap.get("min");
                maxAmount =  (int) lootItemMap.get("max");
            } else {
                minAmount = 1;
                maxAmount = 1;
            }

            List<MaterialVariation> materialVariations = new ArrayList<>();
            List<EnchantmentPossibilities> enchantmentPossibilities = new ArrayList<>();
            if(lootItemMap.containsKey("types")){
                for (String type : (List<String>) lootItemMap.get("types")){
                    String[] split = type.split(":");
                    String[] range = split[0].split("-");

                    float minimum = Float.parseFloat(range[0]);
                    float maximum = Float.parseFloat(range[1]);
                    materialVariations.add(new MaterialVariation(split[1], minimum, maximum));
                }
            }

            if(lootItemMap.containsKey("enchantments")){
                for(String ench : (List<String>) lootItemMap.get("enchantments")){
                    String[] split = ench.split(":");
                    String[] range = split[0].split("-");

                    float minimum = Float.parseFloat(range[0]);
                    float maximum = Float.parseFloat(range[1]);
                    enchantmentPossibilities.add(new EnchantmentPossibilities(split[1], minimum, maximum));
                }
            }

            lootItems.add(new LootItem(material, materialVariations, enchantmentPossibilities, minAmount, maxAmount, step));
        }
        return new LootCategory(name, lootItems, min, max);
    }
}
