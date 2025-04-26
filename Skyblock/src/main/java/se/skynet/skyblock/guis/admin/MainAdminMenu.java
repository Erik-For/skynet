package se.skynet.skyblock.guis.admin;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.guis.menu.MainSkyblockMenu;
import se.skynet.skyblock.guis.menu.MenuItems;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyblock.items.SkyblockItemType;
import se.skynet.skyserverbase.gui.GUI;
import se.skynet.skyserverbase.gui.GUIClickHandler;
import se.skynet.skyserverbase.gui.ItemUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MainAdminMenu extends GUIClickHandler implements GUI, MenuItems {

    private final Skyblock plugin;
    private final SkyblockPlayer player;
    private String searchString = "";
    public MainAdminMenu(Skyblock plugin, SkyblockPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        handleClick(inventoryClickEvent);
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {
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

        ItemStack searchItem = ItemUtils.getItem(Material.NAME_TAG, "§aSearch Item", Arrays.asList("§eLeft §7click to search for an item", "§eRight §7click to clear search"));
        int searchItemSlot = getSlot(6, 6);
        inv.setItem(searchItemSlot, searchItem);
        setClickAction(searchItemSlot, e -> {
            e.setCancelled(true);
            if(e.getClick() == ClickType.LEFT) {
                player.getPlayer().closeInventory();
                plugin.getParentPlugin().getSignGUIManger().open(player.getPlayer(), new String[]{"", "Search items"}, strings -> {
                    searchString = strings[1];
                    player.getPlayer().openInventory(getInventory());
                });
            } else if (e.getClick() == ClickType.RIGHT) {
                searchString = "";
                player.getPlayer().closeInventory();
                player.getPlayer().openInventory(getInventory());
            }
        });


        AtomicInteger i = new AtomicInteger();
        Arrays.stream(SkyblockItemType.values()).filter(this::shouldInclude).filter(v -> !v.equals(SkyblockItemType.VANILLA)).forEach(itemType -> {
            int iInner = i.getAndIncrement();
            int row = iInner / 7 + 2;
            int col = iInner % 7 + 2;
            if (row > 8) {
                return;
            }
            inv.setItem(getSlot(row, col), SkyblockItem.constructSkyblockItem(itemType.getItemClass()).render(player));
            setClickAction(getSlot(row, col), e -> {
                e.setCancelled(true);
                SkyblockItem item = SkyblockItem.constructSkyblockItem(itemType.getItemClass());
                if (item != null) {
                    player.getPlayer().getInventory().addItem(item.render(player));
                }
            });
        });

        return inv;
    }

    private boolean shouldInclude(SkyblockItemType itemType) {
        if (searchString.isEmpty()) {
            return true;
        }
        String itemName = itemType.getItemClass().getSimpleName();
        return itemName.toLowerCase().contains(searchString.toLowerCase());
    }
}
