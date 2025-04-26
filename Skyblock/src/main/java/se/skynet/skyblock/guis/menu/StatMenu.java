package se.skynet.skyblock.guis.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyserverbase.gui.GUI;
import se.skynet.skyserverbase.gui.GUIClickHandler;
import se.skynet.skyserverbase.gui.ItemUtils;

import java.util.Arrays;

public class StatMenu extends GUIClickHandler implements GUI, MenuItems {

    private final Skyblock plugin;
    private final SkyblockPlayer player;
    public StatMenu(Skyblock plugin, SkyblockPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }
    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        inventoryClickEvent.setCancelled(true);
        handleClick(inventoryClickEvent);
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

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
        });

        setClickAction(addCloseButton(inv), e -> {
            player.getPlayer().closeInventory();
        });


        return inv;
    }
}
