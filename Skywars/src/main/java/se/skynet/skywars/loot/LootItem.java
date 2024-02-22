package se.skynet.skywars.loot;

import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LootItem {


    private final Material material;
    private final double chance;
    private final int minAmount;
    private final int maxAmount;
    private final int amountIncrement; // the smallest step in amount of items
    private final List<Tuple<Enchantment, Integer>> enchantments;

    public LootItem(Material material, double chance, int minAmount, int maxAmount, int amountIncrement, List<Tuple<Enchantment, Integer>> enchantments) {
        this.material = material;
        this.chance = chance;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.amountIncrement = (amountIncrement == 0) ? 1 : amountIncrement;
        this.enchantments = enchantments;
    }

    public boolean shouldDrop(){
        return Math.random() <= chance;
    }

    public ItemStack constructItemStack(){
        ItemStack item = new ItemStack(material);
        int count = 1;
        if(minAmount == maxAmount){
            count = minAmount;
        } else {
            count = ThreadLocalRandom.current().nextInt(0,(maxAmount-minAmount)/amountIncrement+1) * amountIncrement + minAmount;
        }
        item.setAmount(count);
        ItemMeta meta = item.getItemMeta();
        for (Tuple<Enchantment, Integer> enchantment : enchantments) {
            meta.addEnchant(enchantment.a(), enchantment.b(), true);
        }
        item.setItemMeta(meta);
        return item;
    }
}
