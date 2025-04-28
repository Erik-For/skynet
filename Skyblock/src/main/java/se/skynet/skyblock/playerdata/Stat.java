package se.skynet.skyblock.playerdata;

import org.bukkit.ChatColor;

public enum Stat {
    DAMAGE("Damage", ChatColor.RED, ""),
    STRENGTH("Strength", ChatColor.RED, ""),
    CRIT_DAMAGE("Crit Damage", ChatColor.RED, "%"),
    CRIT_CHANCE("Crit Chance", ChatColor.RED, "%"),
    DEFENSE("Defense", ChatColor.GREEN, ""),
    HEALTH("Health", ChatColor.GREEN, ""),
    SPEED("Speed", ChatColor.GREEN, ""),
    INTELLIGENCE("Intelligence", ChatColor.GREEN, ""),
    MAGIC_FIND("Magic find", ChatColor.AQUA, "");

    private String name; // Used to display nicely
    private ChatColor color;
    private String unit;

    Stat(String name, ChatColor color, String unit) {
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
