package se.skynet.skyblock.items.items.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.items.items.Ability;
import se.skynet.skyblock.playerdata.Stat;
import se.skynet.skyserverbase.Rank;

import java.util.Arrays;

public class AdminCoinWand extends SkyblockItem implements SkyblockItemEvents {
    public AdminCoinWand() {
        super(Material.BLAZE_ROD, "Coin wand", SkyblockItemType.ADMIN_COIN_WAND, ItemRarity.ADMIN, 1,  false);
    }

    public AdminCoinWand(ItemStack item) {
        super(item);
    }

    protected void setupItem() {
        setAttribute(Stat.DAMAGE, 100);
        addAbility(new ItemAbility(Ability.COIN_WAND_GIVE_COINS, new AbilityImplementation() {
            @Override
            public boolean onUse(AbilityAction action, Ability ability, SkyblockItem item, SkyblockPlayer player, ItemStack itemStack) {
                Skyblock instance = Skyblock.getInstance();
                Rank rank = instance.getParentPlugin().getPlayerDataManager().getPlayerData(player.getPlayer().getUniqueId()).getRank();
                if(rank.hasPriorityHigherThanOrEqual(Rank.ADMIN)) {
                    float randomAmount = (float) Math.floor((Math.random() * 10000000) * 10)/10;
                    player.getProfile().addCoins(randomAmount);
                    player.getPlayer().sendMessage(ChatColor.GREEN + "You have been given coins");
                } else {
                    player.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to use this item");
                }
                return true;
            }
        }));
    }
    @Override
    public ItemStack render(SkyblockPlayer player) {
        return super.render(player);
    }
}
