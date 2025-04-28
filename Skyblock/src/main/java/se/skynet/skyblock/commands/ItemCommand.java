package se.skynet.skyblock.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyblock.items.SkyblockItemID;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.command.Command;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;
import se.skynet.skyserverbase.util.NBTHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCommand extends Command {

    private final Skyblock plugin;

    public ItemCommand(Skyblock plugin) {
        super(plugin.getParentPlugin(), Rank.ADMIN);
        this.plugin = plugin;
    }

    @Override
    protected boolean executeCommand(Player player, CustomPlayerData customPlayerData, Command command, String label, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUsage: /item <item> <count?>");
            return true;
        }

        String itemName = args[0];
        int count = parseItemCount(args, player);
        if (count == -1) return true; // Invalid count, message already sent to the player

        SkyblockItemID itemType = getItemType(itemName, player);
        if (itemType == null) return true; // Invalid item type, message already sent to the player

        giveItemToPlayer(player, itemType, count);
        return true;
    }

    private int parseItemCount(String[] args, Player player) {
        if (args.length > 1) {
            try {
                return Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cPlease use a valid number for the count");
                return -1;
            }
        }
        return 1; // Default count
    }

    private SkyblockItemID getItemType(String itemName, Player player) {
        try {
            return SkyblockItemID.valueOf(itemName.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid item name: " + itemName);
            return null;
        }
    }

    private void giveItemToPlayer(Player player, SkyblockItemID itemType, int count) {
        try {
            SkyblockItem skyblockItem = SkyblockItem.constructSkyblockItem(itemType.getItemClass());
            ItemStack itemStack = skyblockItem.render(plugin.getPlayerManager().getSkyblockPlayer(player));

            if (!NBTHelper.hasTag(itemStack, "s_id")) {
                itemStack.setAmount(count);
            } else if (count != 1) {
                player.sendMessage("§cYou cannot stack this item, giving you 1 instead");
            }

            player.getInventory().addItem(itemStack);
            player.sendMessage("§aGave you " + itemStack.getItemMeta().getDisplayName());
        } catch (Exception e) {
            player.sendMessage("§cCould not create item: " + itemType.name());
            plugin.getLogger().severe("Error creating item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected List<String> tabComplete(Player player, CustomPlayerData customPlayerData, Command command, String label, String[] args) {
        List<String> allItems = Arrays.stream(SkyblockItemID.values())
                .filter(item -> item != SkyblockItemID.VANILLA)
                .map(SkyblockItemID::name)
                .collect(Collectors.toList());

        if (args.length == 0) {
            return allItems;
        } else if (args.length == 1) {
            return allItems.stream()
                    .filter(item -> item.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}