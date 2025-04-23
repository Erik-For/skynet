package se.skynet.skyblock.items;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ItemAbility {

    private final String name;
    private final String action;

    private final List<String> lore;
    private Integer manaCost;
    private Integer cooldown;

    public ItemAbility(String name, String action, List<String > lore) {
        this.name = name;
        this.action = action;
        this.lore = lore;
    }

    public ItemAbility(String name, String action,  List<String> lore, Integer manaCost, Integer cooldown) {
        this.name = name;
        this.action = action;
        this.lore = lore;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
    }

    public List<String> render() {
        List<String> rendered = new ArrayList<>();
        rendered.add(ChatColor.GOLD + "Ability: " + name + "  " +  ChatColor.YELLOW + ChatColor.BOLD + action);
        if (lore != null) {
            for (String line : lore) {
                rendered.add(ChatColor.GRAY + line);
            }
        }
        if (manaCost != null) {
            rendered.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + manaCost);
        }
        if (cooldown != null) {
            rendered.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.DARK_AQUA + cooldown + "s");
        }
        rendered.add("");
        return rendered;
    }
}
