package se.skynet.skyblock.items.items.admin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.items.*;

import java.util.Arrays;

public class AdminCoinWand extends SkyblockItem implements SkyblockItemEvents {
    public AdminCoinWand() {
        super(Material.BLAZE_ROD, "Coin wand", SkyblockItemType.ADMIN_COIN_WAND, ItemRarity.ADMIN, 1,  false);

        setAttribute(ItemAttribute.DAMAGE, 100);
        addAbility(new ItemAbility("Coin wand", "RIGHT CLICK", Arrays.asList("Gives you 10m coins")));
    }

    public AdminCoinWand(ItemStack item) {
        super(item);
    }

    @Override
    public ItemStack render() {
        return super.render();
    }

    @Override
    public void onItemRightClick(SkyblockItem item, PlayerInteractEvent event) {
        event.getPlayer().sendMessage(ChatColor.GOLD + "You right clicked with the coin wand, but nothing happened because there are no coins in the game yet.");
        event.setCancelled(true);
    }
}
