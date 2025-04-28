package se.skynet.skyblock.items;

import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.SkyblockPlayer;

public interface AbilityImplementation {

    boolean onUse(AbilityAction action, Ability ability, SkyblockItem item, SkyblockPlayer player, ItemStack itemStack);
}
