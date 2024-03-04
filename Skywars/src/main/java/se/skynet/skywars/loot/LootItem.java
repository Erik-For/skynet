package se.skynet.skywars.loot;

import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LootItem {

    private final String material;
    private final List<MaterialVariation> materialVariations;
    private final List<EnchantmentPossibilities> enchantmentPossibilities;
    private final int min;
    private final int max;
    private final int step;

    public LootItem(String material, List<MaterialVariation> materialVariations, List<EnchantmentPossibilities> enchantmentPossibilities, int min, int max, int step) {
        this.material = material;
        this.materialVariations = materialVariations;
        this.min = min;
        this.max = max;
        this.enchantmentPossibilities = enchantmentPossibilities;
        this.step = step;
    }

    private Material getMaterial() {
        float v = (float) Math.random();
        for (MaterialVariation materialVariation : materialVariations) {
            if (materialVariation.isInRange(v)) {
                try {
                    Material material1 = Material.valueOf(materialVariation.getMaterialVariation(material));

                    return material1;
                } catch (IllegalArgumentException e) {
                    System.out.println("WARNING: Material " + materialVariation.getMaterialVariation(material) + " does not exist");
                    return Material.AIR;
                }
            }
        }
        return Material.getMaterial(material);
    }

    private List<Tuple<Enchantment, Integer>> getEnchantments() {
        float v = (float) Math.random();
        for (EnchantmentPossibilities enchantmentPossibility : enchantmentPossibilities) {
            if (enchantmentPossibility.isInRange(v)) {
                return enchantmentPossibility.getEnchantments();
            }
        }
        return Collections.emptyList();
    }

    public int getAmount() {
        return ThreadLocalRandom.current().nextInt(0,(max-min)/step+1) * step + min;
    }

    public ItemStack build() {
        Material material = getMaterial();
        ItemStack item = new ItemStack(getMaterial(), getAmount());

        if(material == Material.AIR){
            return item;
        }
        for (Tuple<Enchantment, Integer> enchantment : getEnchantments()) {
            item.addUnsafeEnchantment(
                    enchantment.a(),
                    enchantment.b()
            );
        }

        return item;
    }


}
