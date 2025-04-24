package se.skynet.skyblock.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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
        int hotbarButton = event.getHotbarButton();
        if (hotbarButton != -1 && event.getClickedInventory().getItem(hotbarButton) != null && event.getClickedInventory().getItem(hotbarButton).getType() == Material.NETHER_STAR && event.getClickedInventory().getItem(hotbarButton).getItemMeta().getDisplayName().contains("Skyblock Menu")) {
            event.setCancelled(true);
            return;

        }
        if(event.getSlot() == 8 || (event.getCurrentItem().getType() == Material.NETHER_STAR && event.getCurrentItem().getItemMeta().getDisplayName().contains("Skyblock Menu"))) {
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
    public void onItemDrop(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR && event.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("Skyblock Menu")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot drop the Skyblock Menu item!");
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
