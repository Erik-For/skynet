package se.skynet.skyblock.items;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import se.skynet.skyblock.SkyblockPlayer;

public interface SkyblockItemEvents {
    default void onItemRightClick(SkyblockItem item, PlayerInteractEvent event, SkyblockPlayer skyblockPlayer) {}

    default void onItemLeftClick(SkyblockItem item, PlayerInteractEvent event, SkyblockPlayer skyblockPlayer) {}

    default void onItemDrop(SkyblockItem item, PlayerDropItemEvent event) {}

    default void onItemPickup(SkyblockItem item, PlayerPickupItemEvent event) {}

}
