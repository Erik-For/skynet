package se.skynet.skyserverbase.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface GUI extends InventoryHolder {

     public void onClick(InventoryClickEvent event);

}
