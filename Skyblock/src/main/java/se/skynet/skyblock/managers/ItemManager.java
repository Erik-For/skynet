package se.skynet.skyblock.managers;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyblock.items.SkyblockItemEvents;
import se.skynet.skyblock.items.SkyblockItemType;
import se.skynet.skyblock.items.VanillaItem;
import se.skynet.skyserverbase.util.NBTHelper;

import java.util.HashMap;

public class ItemManager implements Listener {

    private final Skyblock plugin;
    private final HashMap<SkyblockItemType, SkyblockItemEvents> items = new HashMap<>();

    public ItemManager(Skyblock plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void itemPickupEvent(PlayerPickupItemEvent event) {
        if(!SkyblockItem.isSkyblockItem(event.getItem().getItemStack())) {
            Item item = event.getItem();
            ItemStack itemStack = item.getItemStack();
            ItemStack render = new VanillaItem(itemStack.getType(), "", itemStack.getAmount(), itemStack.getMaxStackSize()).render();
            event.getPlayer().getInventory().addItem(render);
            item.remove();
            event.setCancelled(true);
        } else {
            SkyblockItemType type = NBTHelper.getString(event.getItem().getItemStack(), "s_type", "VANILLA").equals("VANILLA") ? SkyblockItemType.VANILLA : SkyblockItemType.valueOf(NBTHelper.getString(event.getItem().getItemStack(), "s_type", "VANILLA"));
            if(type == SkyblockItemType.VANILLA){
                return;
            }
            try {
                SkyblockItem skyblockItem = type.getItemClass().newInstance();
                if(!(skyblockItem instanceof SkyblockItemEvents)){
                    return;
                }
                SkyblockItemEvents item = (SkyblockItemEvents) skyblockItem;
                item.onItemPickup((SkyblockItem) item, event);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return;
            }

        }
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        if(!SkyblockItem.isSkyblockItem(event.getItem())){
            return;
        }
        SkyblockItemType type = NBTHelper.getString(event.getItem(), "s_type", "VANILLA").equals("VANILLA") ? SkyblockItemType.VANILLA : SkyblockItemType.valueOf(NBTHelper.getString(event.getItem(), "s_type", "VANILLA"));
        if(type == SkyblockItemType.VANILLA){
            return;
        }
        try{
            SkyblockItem skyblockItem = type.getItemClass().newInstance();
            if(!(skyblockItem instanceof SkyblockItemEvents)){
                return;
            }
            SkyblockItemEvents item = (SkyblockItemEvents) skyblockItem;
            switch(event.getAction()){
                case RIGHT_CLICK_BLOCK:
                case RIGHT_CLICK_AIR:
                    item.onItemRightClick((SkyblockItem) item, event);
                    break;
                case LEFT_CLICK_BLOCK:
                case LEFT_CLICK_AIR:
                    item.onItemLeftClick((SkyblockItem) item, event);
                    break;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if(!SkyblockItem.isSkyblockItem(event.getItemDrop().getItemStack())){
            return;
        }
        SkyblockItemType type = NBTHelper.getString(event.getItemDrop().getItemStack(), "s_type", "VANILLA").equals("VANILLA") ? SkyblockItemType.VANILLA : SkyblockItemType.valueOf(NBTHelper.getString(event.getItemDrop().getItemStack(), "s_type", "VANILLA"));
        if(type == SkyblockItemType.VANILLA){
            return;
        }

        try {
            SkyblockItem skyblockItem = type.getItemClass().newInstance();
            // Check if the item is a SkyblockItemEvents
            if (!(skyblockItem instanceof SkyblockItemEvents)) {
                return;
            }
            SkyblockItemEvents skyblockItemEventHandler = (SkyblockItemEvents) skyblockItem;
            skyblockItemEventHandler.onItemDrop(skyblockItem, event);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
