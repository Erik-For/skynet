package se.skynet.skywars;

import com.google.gson.ExclusionStrategy;
import com.google.gson.GsonBuilder;
import net.minecraft.server.v1_8_R3.Tuple;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import se.skynet.skywars.loot.LootItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private final GameManger manger;

    public ConfigManager(GameManger manger) {
        this.manger = manger;
        YamlConfiguration lootConfig = new YamlConfiguration();
        YamlConfiguration locationsConfig = new YamlConfiguration();
        File lootTableFile = null;
        File locationsFile = null;

        try {
            lootTableFile = Paths.get(manger.getPlugin().getServer().getWorldContainer().getCanonicalPath(), "loot.yml").toFile();
            locationsFile = Paths.get(manger.getPlugin().getServer().getWorldContainer().getCanonicalPath(), "/world", "skywars.yml").toFile();
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

        locationsConfig.getMapList("island-chests").forEach(map -> {
            int x = (int) map.get("x");
            int y = (int) map.get("y");
            int z = (int) map.get("z");
            manger.getLootManger().addIslandChest(new Location(manger.getPlugin().getServer().getWorlds().get(0), x, y, z));
        });
        locationsConfig.getMapList("middle-chests").forEach(map -> {
            int x = (int) map.get("x");
            int y = (int) map.get("y");
            int z = (int) map.get("z");
            manger.getLootManger().addMiddleChest(new Location(manger.getPlugin().getServer().getWorlds().get(0), x, y, z));
        });

        locationsConfig.getMapList("spawn-points").forEach(map -> {
            int x = (int) map.get("x");
            int y = (int) map.get("y");
            int z = (int) map.get("z");
            Location location = new Location(manger.getPlugin().getServer().getWorlds().get(0), x + 0.5, y, z + 0.5, 0, 0);
            manger.getPlayerJoinManager().addSpawnLocation(location);
            manger.getCageManager().addCageLocation(location);
        });

        lootConfig.getMapList("island-chests").forEach(map -> {
            manger.getLootManger().getIslandLootTable().addLootItem(getLootItem(map));
        });

        lootConfig.getMapList("mid-chests").forEach(map -> {
            manger.getLootManger().getMiddleLootTable().addLootItem(getLootItem(map));
        });

        manger.getPlugin().getLogger().info("Loaded loot table and locations");
    }

    private LootItem getLootItem(Map<?, ?> map) {
        String name = (String) map.get("material");
        int min = (int) map.get("min");
        int max = (int) map.get("max");
        double chance = (double) map.get("chance");

        int countIncrement = 1;
        if (map.containsKey("count-increment")){
            countIncrement = (int) map.get("count-increment");
        }
        List<Map<?, ?>> enchantmentList = new ArrayList<>();
        if(map.containsKey("enchantments")) {
            enchantmentList = (List<Map<?, ?>>) map.get("enchantments");
        }
        List<Tuple<Enchantment, Integer>> enchantments = new ArrayList<>();
        enchantmentList.forEach(enchantmentMap -> {
            String enchantmentName = (String) enchantmentMap.get("type");
            int level = (int) enchantmentMap.get("level");
            if (Enchantment.getByName(enchantmentName) != null){
                enchantments.add(new Tuple<>(Enchantment.getByName(enchantmentName), level));
            } else {
                System.out.println("Invalid enchantment in loot table");
            }
        });

        Material material;
        if (EnumUtils.isValidEnum(Material.class, name)){
            material = Material.getMaterial(name);
        } else {
            System.out.println("Invalid material in loot table");
            material = Material.AIR;
        }
        return new LootItem(
                material,
                chance,
                min,
                max,
                countIncrement,
                enchantments
        );
    }
}
