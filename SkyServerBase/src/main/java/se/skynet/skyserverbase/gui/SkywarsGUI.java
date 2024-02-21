package se.skynet.skyserverbase.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyserverbase.SkyServerBase;

public class SkywarsGUI implements GUI {

    private final SkyServerBase plugin;

    public SkywarsGUI(SkyServerBase plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if(event.getCurrentItem().getType() == Material.DIAMOND) {
            event.getWhoClicked().sendMessage("You clicked diamond");
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 9*4, "Skywars");
        inv.setItem(0+9*1+1, new ItemStack(Material.DIAMOND, 1));

        return inv;
    }
}
