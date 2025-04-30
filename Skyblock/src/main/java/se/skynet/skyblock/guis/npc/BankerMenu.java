package se.skynet.skyblock.guis.npc;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.Util;
import se.skynet.skyblock.guis.menu.MenuItems;
import se.skynet.skyserverbase.gui.GUI;
import se.skynet.skyserverbase.gui.GUIClickHandler;
import se.skynet.skyserverbase.gui.ItemUtils;

import java.util.Arrays;

public class BankerMenu extends GUIClickHandler implements GUI, MenuItems {

    private final Skyblock plugin;
    private final SkyblockPlayer player;
    public BankerMenu(Skyblock plugin, SkyblockPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }
    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        handleClick(inventoryClickEvent);
        inventoryClickEvent.setCancelled(true);
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = plugin.getServer().createInventory(this, 9 * 3, "Banker Menu");

        // Add deposit item
        inventory.setItem(getSlot(2, 3), ItemUtils.getItem(
                Material.CHEST,
                "§aDeposit",
                Arrays.asList("§7Deposit your coins into the bank",
                        "",
                        "§eCurrent Balance: §6" + Util.formatNumberWithCommas(player.getProfile().getBankCoins()) + " coins"
                )
        ));

        // Add withdraw item with bank balance in the lore
        inventory.setItem(getSlot(2, 7), ItemUtils.getItem(
                Material.FURNACE,
                "§aWithdraw",
                Arrays.asList(
                        "§7Withdraw your coins from the bank",
                        "",
                        "§eCurrent Balance: §6" + Util.formatNumberWithCommas(player.getProfile().getBankCoins()) + " coins"
                )
        ));

        setClickAction(getSlot(2, 3), a -> {
            plugin.getParentPlugin().getSignGUIManger().open(player.getPlayer(), new String[]{"", "Enter amount to deposit"}, (c) -> {
                String amountStr = c[0];
                if (amountStr == null || amountStr.isEmpty()) {
                    player.getPlayer().sendMessage("§cPlease enter a valid amount");
                    return;
                }
                int multiplier = 1;
                if (amountStr.endsWith("k")) {
                    multiplier = 1000;
                    amountStr = amountStr.substring(0, amountStr.length() - 1);
                } else if (amountStr.endsWith("m")) {
                    multiplier = 1000000;
                    amountStr = amountStr.substring(0, amountStr.length() - 1);
                } else if (amountStr.endsWith("b")) {
                    multiplier = 1000000000;
                    amountStr = amountStr.substring(0, amountStr.length() - 1);
                }
                double amount;
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {
                    player.getPlayer().sendMessage("§cPlease enter a valid amount");
                    return;
                }
                amount *= multiplier;
                if (amount > player.getProfile().getCoins()) {
                    player.getPlayer().sendMessage("§cYou don't have enough coins to deposit this amount");
                    return;
                }
                player.getProfile().setCoins(player.getProfile().getCoins() - amount);
                player.getProfile().setBankCoins(player.getProfile().getBankCoins() + amount);
            });
        });

        setClickAction(getSlot(2, 7), a -> {
            plugin.getParentPlugin().getSignGUIManger().open(player.getPlayer(), new String[]{"", "Enter amount to withdraw"}, (c) -> {
                String amountStr = c[0];
                if (amountStr == null || amountStr.isEmpty()) {
                    player.getPlayer().sendMessage("§cPlease enter a valid amount");
                    return;
                }
                int multiplier = 1;
                if (amountStr.endsWith("k")) {
                    multiplier = 1000;
                    amountStr = amountStr.substring(0, amountStr.length() - 1);
                } else if (amountStr.endsWith("m")) {
                    multiplier = 1000000;
                    amountStr = amountStr.substring(0, amountStr.length() - 1);
                } else if (amountStr.endsWith("b")) {
                    multiplier = 1000000000;
                    amountStr = amountStr.substring(0, amountStr.length() - 1);
                }
                double amount;
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {
                    player.getPlayer().sendMessage("§cPlease enter a valid amount");
                    return;
                }
                amount *= multiplier;
                if (amount > player.getProfile().getBankCoins()) {
                    player.getPlayer().sendMessage("§cYou don't have enough coins in the bank to withdraw this amount");
                    return;
                }
                player.getProfile().setCoins(player.getProfile().getCoins() + amount);
                player.getProfile().setBankCoins(player.getProfile().getBankCoins() - amount);
            });
        });

        return inventory;
    }
}
