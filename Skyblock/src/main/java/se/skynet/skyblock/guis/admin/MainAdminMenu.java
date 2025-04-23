package se.skynet.skyblock.guis.admin;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.guis.menu.MainSkyblockMenu;
import se.skynet.skyblock.guis.menu.MenuItems;
import se.skynet.skyblock.items.items.admin.AdminCoinWand;
import se.skynet.skyserverbase.gui.GUI;
import se.skynet.skyserverbase.gui.GUIClickHandler;
import se.skynet.skyserverbase.gui.ItemUtils;

import java.util.Arrays;

public class MainAdminMenu extends GUIClickHandler implements GUI, MenuItems {

    private final Skyblock plugin;
    private final SkyblockPlayer player;
    public MainAdminMenu(Skyblock plugin, SkyblockPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }
    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        handleClick(inventoryClickEvent);
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = plugin.getServer().createInventory(this, 9*6, "Skyblock Menu");

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemUtils.setName(paper, "Work in progress");
        ItemUtils.setLore(paper, Arrays.asList("This menu is a work in progress"));
        inv.setItem(getSlot(3, 5), paper);

        setClickAction(addBackArrow(inv), e -> {
            MainSkyblockMenu mainSkyblockMenu = new MainSkyblockMenu(plugin, this.player);
            player.getPlayer().openInventory(mainSkyblockMenu.getInventory());
            e.setCancelled(true);
        });

        setClickAction(addCloseButton(inv), e -> {
            player.getPlayer().closeInventory();
            e.setCancelled(true);
        });

        AdminCoinWand adminCoinWand = new AdminCoinWand();
        ItemStack coinWand = adminCoinWand.render();
        inv.setItem(getSlot(2, 2), coinWand);

        

        return inv;
    }
}
