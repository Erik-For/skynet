package se.skynet.skyblock.guis.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.Util;
import se.skynet.skyblock.guis.admin.MainAdminMenu;
import se.skynet.skyblock.playerdata.Stat;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.gui.GUI;
import se.skynet.skyserverbase.gui.GUIClickHandler;
import se.skynet.skyserverbase.gui.ItemUtils;

import java.util.*;

public class MainSkyblockMenu extends GUIClickHandler implements GUI, MenuItems {

    private final Skyblock plugin;
    private final SkyblockPlayer player;

    public MainSkyblockMenu(Skyblock plugin, SkyblockPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        handleClick(event);
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    @Override
    public Inventory getInventory() {
        Inventory skyblockMenu = plugin.getServer().createInventory(this, 9 * 6, "Skyblock Menu");
        // row 2
        // col 5
        ItemStack statsPlayerHead = new ItemStack(ItemUtils.getPlayerHead(player.getPlayer().getName()));
        // generate random stats for testing
        Map<String, Object> stats = new HashMap<>();
        stats.put("Health", player.calculateStatMax(Stat.HEALTH));
        stats.put("Defense", player.calculateStatMax(Stat.DEFENSE));
        stats.put("Speed", player.calculateStatMax(Stat.SPEED));
        stats.put("Strength", player.calculateStatMax(Stat.STRENGTH));
        stats.put("Intelligence", player.calculateStatMax(Stat.INTELLIGENCE));
        stats.put("Crit Chance", player.calculateStatMax(Stat.CRIT_CHANCE));
        stats.put("Crit Damage", player.calculateStatMax(Stat.CRIT_DAMAGE));
        stats.put("Magic Find", player.calculateStatMax(Stat.MAGIC_FIND));

        applySkyBlockStatsAsLore(statsPlayerHead, stats);
        skyblockMenu.setItem(getSlot(2, 5), statsPlayerHead);
        setClickAction(getSlot(2, 5), (e) -> {
            // Handle click event for player head
            StatMenu statMenu = new StatMenu(plugin, player);
            player.getPlayer().openInventory(statMenu.getInventory());
        });

        ItemStack skillSword = new ItemStack(Material.DIAMOND_SWORD);
        ItemUtils.setName(skillSword, ChatColor.GREEN + "Your skills");
        ItemUtils.setLore(skillSword, Arrays.asList(
                ChatColor.GRAY + "View your skills and progress!",
                "",
                ChatColor.GOLD + Util.formatNumber(player.getProfile().getSkillAvrage(), 1) + " Skill Avg",
                "",
                ChatColor.YELLOW + "Click to view!"
        ));
        skyblockMenu.setItem(getSlot(3, 2), skillSword);
        setClickAction(getSlot(3, 2), (e) -> {
            SkillMenu skillMenu = new SkillMenu(plugin, player);
            player.getPlayer().openInventory(skillMenu.getInventory());
        });

        setClickAction(addCloseButton(skyblockMenu), e -> {
            player.getPlayer().closeInventory();
        });

        if(plugin.getParentPlugin().getPlayerDataManager().getPlayerData(player.getPlayer().getUniqueId()).getRank().hasPriorityHigherThanOrEqual(Rank.ADMIN)) {
            ItemStack console = ItemUtils.getPlayerHeadFromTexture("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc3ZDhjODQ2NGU0ZGVkZTRhOTA2ODg5N2Y1NWRkZTI5MmYzY2VjYTE5YjUxMGQ5ZjM4OTJkZTM0OWFiZjg0In19fQ==");
            ItemUtils.setName(console, ChatColor.RED + "Admin tools");
            ItemUtils.setLore(console, Arrays.asList(
                    ChatColor.GRAY + "Admin tools to be able to test",
                    ChatColor.GRAY + "and manage the game!"
            ));
            skyblockMenu.setItem(getSlot(6, 9), console);
            setClickAction(getSlot(6, 9), e -> {
                MainAdminMenu menu = new MainAdminMenu(plugin, player);
                player.getPlayer().openInventory(menu.getInventory());
            });
        }


        return skyblockMenu;
    }

    /**
     * Applies SkyBlock profile stats as lore to a given ItemStack
     *
     * @param item  The ItemStack to apply lore to
     * @param stats Map containing stat names and their values
     */
    public void applySkyBlockStatsAsLore(ItemStack item, Map<String, Object> stats) {
        if (item == null) return;

        ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        meta.setDisplayName(ChatColor.GREEN + "Your SkyBlock Profile");

        // Add header
        lore.add(ChatColor.GRAY + "View your equipment, stats, and more!");
        lore.add("");

        // Add each stat with appropriate color
        if (stats.containsKey("Health"))
            lore.add(ChatColor.RED + "❤ Health " + ChatColor.WHITE + stats.get("Health"));

        if (stats.containsKey("Defense"))
            lore.add(ChatColor.GREEN + "❈ Defense " + ChatColor.WHITE + stats.get("Defense"));

        if (stats.containsKey("Speed"))
            lore.add(ChatColor.WHITE + "✦ Speed " + ChatColor.WHITE + stats.get("Speed"));

        if (stats.containsKey("Strength"))
            lore.add(ChatColor.RED + "❁ Strength " + ChatColor.WHITE + stats.get("Strength"));

        if (stats.containsKey("Intelligence"))
            lore.add(ChatColor.AQUA + "✎ Intelligence " + ChatColor.WHITE + stats.get("Intelligence"));

        if (stats.containsKey("Crit Chance"))
            lore.add(ChatColor.BLUE + "☣ Crit Chance " + ChatColor.WHITE + stats.get("Crit Chance") + "%");

        if (stats.containsKey("Crit Damage"))
            lore.add(ChatColor.BLUE + "☠ Crit Damage " + ChatColor.WHITE + stats.get("Crit Damage") + "%");

        if (stats.containsKey("Magic Find"))
            lore.add(ChatColor.AQUA + "✯ Magic Find " + ChatColor.WHITE + stats.get("Magic Find"));

        // Add footer
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to view!");

        // Set the lore to the item meta
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
