package se.skynet.skyblock.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.guis.menu.MainSkyblockMenu;
import se.skynet.skyserverbase.gui.ItemUtils;

import java.util.Arrays;

public class SkyblockMenuItemManager implements Listener {

    private final Skyblock plugin;

    public SkyblockMenuItemManager(Skyblock plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void preventSkyblockMenuMove(InventoryClickEvent event) {
        if(event.getSlot() == 8) {
            event.setCancelled(true);
            HumanEntity whoClicked = event.getWhoClicked();
            if(whoClicked instanceof Player) {
                SkyblockPlayer skyblockPlayer = plugin.getPlayerManager().getSkyblockPlayer(((Player) whoClicked));
                MainSkyblockMenu skyblockMenu = new MainSkyblockMenu(plugin, skyblockPlayer);
                skyblockPlayer.getPlayer().openInventory(skyblockMenu.getInventory());
            }
        }
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().setItem(8, ItemUtils.getItem(Material.NETHER_STAR, ChatColor.GREEN + "Skyblock Menu " + ChatColor.GRAY + "(Click)", Arrays.asList(
                ChatColor.GRAY + "View all of your Skyblock progress,",
                ChatColor.GRAY + "including your Skills, Collections,",
                ChatColor.GRAY + "Recipes, and more!",
                "",
                ChatColor.YELLOW + "Click to open!"
        )));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.hasItem() && event.getItem().getType() == Material.NETHER_STAR && event.getItem().getItemMeta().getDisplayName().contains("Skyblock Menu")) {
            event.setCancelled(true);
            SkyblockPlayer skyblockPlayer = plugin.getPlayerManager().getSkyblockPlayer(event.getPlayer());
            MainSkyblockMenu skyblockMenu = new MainSkyblockMenu(plugin, skyblockPlayer);
            skyblockPlayer.getPlayer().openInventory(skyblockMenu.getInventory());
        }
    }
}
