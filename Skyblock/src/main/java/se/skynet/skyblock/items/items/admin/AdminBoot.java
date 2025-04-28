package se.skynet.skyblock.items.items.admin;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.*;
import se.skynet.skyserverbase.Rank;

public class AdminBoot extends SkyblockItem implements SkyblockItemEvents {
    public AdminBoot() {
        super(Material.LEATHER_BOOTS, "Admin boots", SkyblockItemID.ADMIN_BOOT, ItemRarity.ADMIN, 1, false);
    }

    public AdminBoot(ItemStack item) {
        super(item);
    }

    @Override
    protected void setupItem() {
        addLore("When worn by an admin it kicks all players hit by the admin");
        setType(SkyblockItemType.NORMAL);
        setLeatherColor(Color.RED);
    }

    @Override
    public void onPlayerHitEntity(SkyblockItem item, EntityDamageByEntityEvent event, SkyblockPlayer skyblockPlayer) {
        Rank rank = Skyblock.getInstance().getParentPlugin().getPlayerDataManager().getPlayerData(skyblockPlayer.getPlayer().getUniqueId()).getRank();
        if(!rank.hasPriorityHigherThanOrEqual(Rank.ADMIN)) return;
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        event.setCancelled(true);
        player.kickPlayer("You got kicked buy an admin boot!");
    }
}
