package se.skynet.skyblock.guis.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyserverbase.gui.ItemUtils;

import java.util.Arrays;

public interface MenuItems {
    static int getSlot(int row, int column) {
        return (row - 1) * 9 + column - 1;
    }

    public default int addBackArrow(Inventory inventory) {
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemUtils.setName(arrow, ChatColor.GREEN + "Back");

        inventory.setItem(getSlot(inventory.getSize() / 9, 4), arrow);

        return getSlot(inventory.getSize() / 9, 4);
    }

    public default int addCloseButton(Inventory inventory) {
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemUtils.setName(closeButton, ChatColor.RED + "Close");

        inventory.setItem(getSlot(inventory.getSize() / 9, 5), closeButton);

        return getSlot(inventory.getSize() / 9, 5);
    }

}
