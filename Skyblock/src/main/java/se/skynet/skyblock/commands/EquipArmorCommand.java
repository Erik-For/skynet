package se.skynet.skyblock.commands;

import org.bukkit.entity.Player;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.ArmorSets;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.command.Command;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EquipArmorCommand extends Command {

    private final Skyblock plugin;

    public EquipArmorCommand(Skyblock plugin) {
        super(plugin.getParentPlugin(), Rank.ADMIN);
        this.plugin = plugin;
    }

    @Override
    protected boolean executeCommand(Player player, CustomPlayerData customPlayerData, Command command, String s, String[] strings) {
        if(strings.length < 1) {
            player.sendMessage("§cPlease use /equiparmor <armor>");
            return true;
        }

        String armorType = strings[0];
        ArmorSets armorSet = ArmorSets.valueOf(armorType);
        if(armorSet == null) {
            player.sendMessage("§cInvalid armor type");
            return true;
        }

        SkyblockPlayer skyblockPlayer = plugin.getPlayerManager().getSkyblockPlayer(player);
        player.getInventory().setHelmet(SkyblockItem.constructSkyblockItem(armorSet.getHead().getItemClass()).render(skyblockPlayer));
        player.getInventory().setChestplate(SkyblockItem.constructSkyblockItem(armorSet.getChest().getItemClass()).render(skyblockPlayer));
        player.getInventory().setLeggings(SkyblockItem.constructSkyblockItem(armorSet.getLegs().getItemClass()).render(skyblockPlayer));
        player.getInventory().setBoots(SkyblockItem.constructSkyblockItem(armorSet.getBoots().getItemClass()).render(skyblockPlayer));

        return true;
    }

    @Override
    protected List<String> tabComplete(Player player, CustomPlayerData customPlayerData, Command command, String s, String[] strings) {
        if(strings.length == 0) {
            return Arrays.stream(ArmorSets.values()).map(ArmorSets::name).collect(Collectors.toList());
        } else if(strings.length == 1) {
            return Arrays.stream(ArmorSets.values())
                    .filter(armorSet -> armorSet.name().toLowerCase().startsWith(strings[0].toLowerCase()))
                    .map(ArmorSets::name)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
