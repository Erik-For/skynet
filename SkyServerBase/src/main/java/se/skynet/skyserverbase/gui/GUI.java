package se.skynet.skyserverbase.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface GUI extends InventoryHolder {

     public void onClick(InventoryClickEvent event);

     public default int getSlot(int row, int column) {
            return (row - 1) * 9 + column - 1;
     }

}
