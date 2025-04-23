package se.skynet.skyblock.guis.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.Util;
import se.skynet.skyblock.playerdata.SkillType;
import se.skynet.skyserverbase.gui.GUI;
import se.skynet.skyserverbase.gui.GUIClickHandler;
import se.skynet.skyserverbase.gui.ItemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillMenu extends GUIClickHandler implements GUI, MenuItems{

    private final Skyblock plugin;
    private final SkyblockPlayer player;
    public SkillMenu(Skyblock plugin, SkyblockPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }
    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        inventoryClickEvent.setCancelled(true);
        handleClick(inventoryClickEvent);
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = plugin.getServer().createInventory(this, 9 * 6, "Skyblock Menu");

        setClickAction(addBackArrow(inv), e -> {
            MainSkyblockMenu mainSkyblockMenu = new MainSkyblockMenu(plugin, this.player);
            player.getPlayer().openInventory(mainSkyblockMenu.getInventory());
        });

        setClickAction(addCloseButton(inv), e -> {
            player.getPlayer().closeInventory();
        });

        inv.setItem(getSlot(3, 3), constructSkillItem(
                player,
                SkillType.COMBAT,
                Material.STONE_SWORD,
                Arrays.asList("Fight mobs and special bosses to", "earn Combat XP!")
        ));
        inv.setItem(getSlot(3, 4), constructSkillItem(
                player,
                SkillType.FARMING,
                Material.GOLD_HOE,
                Arrays.asList("Harvest crops and shear sheep to", "earn Farming XP!")
        ));
        inv.setItem(getSlot(3, 5), constructSkillItem(
                player,
                SkillType.FISHING,
                Material.FISHING_ROD,
                Arrays.asList("Visit your local pond and", "fish to earn Fishing XP!")
        ));
        inv.setItem(getSlot(3, 6), constructSkillItem(
                player,
                SkillType.MINING,
                Material.STONE_PICKAXE,
                Arrays.asList("Dive into deep caves and find rare","ores and valuable materials to earn", "Mining XP!")
        ));
        inv.setItem(getSlot(3, 7), constructSkillItem(
                player,
                SkillType.FORAGING,
                Material.SAPLING,
                Arrays.asList("Cut trees and forage for other", "plats to ern Foraging XP!")
        ));

        return inv;
    }

    private ItemStack constructSkillItem(SkyblockPlayer player, SkillType skillType, Material material, List<String> subtitle) {
        float progress = player.getProfile().getSkill(skillType).getProgress();
        int level = player.getProfile().getSkill(skillType).getLevel();

        int length = 20;
        int progressBarLength = (int) (progress * length);

        String progressbar = Util.insertAt(Util.repeatCharecter("-", length),ChatColor.WHITE + "", progressBarLength);

        List<String> lore = new ArrayList<>();
        subtitle.forEach(s -> {
            lore.add(ChatColor.GRAY + s);
        });
        lore.addAll(Arrays.asList(
                "",
                ChatColor.GRAY + "Progress to to level " + Util.intToRoman(level + 1) + ": " + ChatColor.YELLOW + Util.formatNumber(progress * 100, 2) + "%",
                ChatColor.GREEN + progressbar
        ));

        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to view!");

        return ItemUtils.getItem(
                material,
                ChatColor.GREEN + skillType.getName() + " " + Util.intToRoman(level),
                lore
        );
    }

}
