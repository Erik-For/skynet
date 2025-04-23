package se.skynet.skyblock.items;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public interface SkyblockItemEvents {
    default void onItemRightClick(SkyblockItem item, PlayerInteractEvent event) {}

    default void onItemLeftClick(SkyblockItem item, PlayerInteractEvent event) {}

    default void onItemDrop(SkyblockItem item, PlayerDropItemEvent event) {}

    default void onItemPickup(SkyblockItem item, PlayerPickupItemEvent event) {}

}
