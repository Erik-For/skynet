package se.skynet.skyblock.guis.admin;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.guis.menu.MainSkyblockMenu;
import se.skynet.skyblock.guis.menu.MenuItems;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyblock.items.SkyblockItemType;
import se.skynet.skyserverbase.gui.GUI;
import se.skynet.skyserverbase.gui.GUIClickHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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
        Inventory inv = plugin.getServer().createInventory(this, 9*6, "Skyblock Admin Menu");

        setClickAction(addBackArrow(inv), e -> {
            MainSkyblockMenu mainSkyblockMenu = new MainSkyblockMenu(plugin, this.player);
            player.getPlayer().openInventory(mainSkyblockMenu.getInventory());
            e.setCancelled(true);
        });

        setClickAction(addCloseButton(inv), e -> {
            player.getPlayer().closeInventory();
            e.setCancelled(true);
        });
        AtomicInteger i = new AtomicInteger();
        Arrays.stream(SkyblockItemType.values()).filter(v -> !v.equals(SkyblockItemType.VANILLA)).forEach(itemType -> {
            try {
                int iInner = i.getAndIncrement();
                int row = iInner / 7 + 2;
                int col = iInner % 7 + 2;
                if (row > 8) {
                    return;
                }
                inv.setItem(getSlot(row, col), itemType.getItemClass().getConstructor().newInstance().render(player));
                setClickAction(getSlot(row, col), e -> {
                    e.setCancelled(true);
                    SkyblockItem item = null;
                    try {
                        item = itemType.getItemClass().getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (item != null) {
                        player.getPlayer().getInventory().addItem(item.render(player));
                    }
                });


            } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        return inv;
    }
}
