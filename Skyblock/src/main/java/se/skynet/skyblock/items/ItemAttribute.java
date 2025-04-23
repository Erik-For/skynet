package se.skynet.skyblock.items;

import org.bukkit.ChatColor;

public enum ItemAttribute {
    DAMAGE("Damage", ChatColor.RED, ""),
    STRENGTH("Strength", ChatColor.RED, ""),
    CRIT_DAMAGE("Crit Damage", ChatColor.RED, "%"),
    CRIT_CHANCE("Crit Chance", ChatColor.RED, "%"),
    DEFENSE("Defense", ChatColor.GREEN, ""),
    HEALTH("Health", ChatColor.GREEN, ""),
    SPEED("Speed", ChatColor.GREEN, ""),
    INTELLIGENCE("Intelligence", ChatColor.GREEN, ""),
    ;

    private String name; // Used to display nicely
    private ChatColor color;
    private String unit;

    ItemAttribute(String name, ChatColor color, String unit) {
        this.name = name;
        this.color = color;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public ChatColor getColor() {
        return color;
    }
}
