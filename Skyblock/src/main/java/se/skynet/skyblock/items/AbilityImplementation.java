package se.skynet.skyblock.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.items.Ability;

public interface AbilityImplementation {

    boolean onUse(AbilityAction action, Ability ability, SkyblockItem item, SkyblockPlayer player, ItemStack itemStack);
}
