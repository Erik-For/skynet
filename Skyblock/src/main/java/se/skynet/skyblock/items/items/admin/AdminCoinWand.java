package se.skynet.skyblock.items.items.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.ItemAbility;
import se.skynet.skyblock.items.ItemRarity;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyblock.items.SkyblockItemEvents;
import se.skynet.skyblock.items.SkyblockItemType;
import se.skynet.skyblock.playerdata.Stat;
import se.skynet.skyserverbase.Rank;

import java.util.Arrays;

public class AdminCoinWand extends SkyblockItem implements SkyblockItemEvents {
    public AdminCoinWand() {
        super(Material.BLAZE_ROD, "Coin wand", SkyblockItemType.ADMIN_COIN_WAND, ItemRarity.ADMIN, 1,  false);

        setAttribute(Stat.DAMAGE, 100);
        addAbility(new ItemAbility("Coin wand", "RIGHT CLICK", Arrays.asList("Gives you coins")));
    }

    public AdminCoinWand(ItemStack item) {
        super(item);
    }

    @Override
    public ItemStack render(SkyblockPlayer player) {
        return super.render(player);
    }

    @Override
    public void onItemRightClick(SkyblockItem item, PlayerInteractEvent event) {
        event.setCancelled(true);
        Skyblock instance = Skyblock.getInstance();
        SkyblockPlayer skyblockPlayer = instance.getPlayerManager().getSkyblockPlayer(event.getPlayer());
        if (skyblockPlayer == null) {
            return;
        }
        Rank rank = instance.getParentPlugin().getPlayerDataManager().getPlayerData(event.getPlayer().getUniqueId()).getRank();
        float randomAmount = (float) Math.floor((Math.random() * 10000000) * 10)/10;
        if(rank.hasPriorityHigherThanOrEqual(Rank.ADMIN)) {
            skyblockPlayer.getProfile().addCoins(randomAmount);
            event.getPlayer().sendMessage(ChatColor.GREEN + "You have been given coins");
        } else {
            event.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to use this item");
        }

    }
}
