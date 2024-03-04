package se.skynet.skywars.loot;

import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentPossibilities {
    private final float min;
    private final float max;
    private final List<Tuple<Enchantment, Integer>> enchantments = new ArrayList<>();

    public EnchantmentPossibilities(String data, float min, float max) {
        this.min = min;
        this.max = max;
        String[] split = data.split(",");

        for (String s : split) {
            String[] enchantment = s.split("-");

            Enchantment byName = Enchantment.getByName(enchantment[0]);
            if (byName != null) {
                enchantments.add(new Tuple<>(byName, Integer.parseInt(enchantment[1])));
            } else {
                System.out.println("WARNING: Enchantment " + enchantment[0] + " does not exist");
            }
        }
    }

    public boolean isInRange(float value) {
        return value > min && value <= max;
    }

    public List<Tuple<Enchantment, Integer>> getEnchantments() {
        return enchantments;
    }
}
