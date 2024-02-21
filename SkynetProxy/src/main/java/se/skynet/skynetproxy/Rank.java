package se.skynet.skynetproxy;

import net.md_5.bungee.api.ChatColor;

public enum Rank {
    DEFAULT("default", "", ChatColor.GRAY, 0),
    MVP("MVP", ChatColor.GOLD + "MVP", ChatColor.GOLD, 1),
    MODERATOR("Moderator", ChatColor.DARK_GREEN + "Moderator", ChatColor.DARK_GREEN, 2),
    ADMIN("Admin", ChatColor.RED + "Admin", ChatColor.RED, 3),
    MANAGEMENT("Management", ChatColor.LIGHT_PURPLE + "Management", ChatColor.LIGHT_PURPLE, 4);
    private final String displayName;
    private final String prefix;

    private final ChatColor rankColor;
    private final int priority;
    Rank(String displayName, String prefix, ChatColor rankColor, int priority) {
        this.displayName = displayName;
        this.rankColor = rankColor;
        this.prefix = prefix;
        this.priority = priority;
    }

    public String getDisplayName() {
        return this.displayName;
    }
    public String getPrefix() {
        return prefix;
    }

    public ChatColor getRankColor() {
        return rankColor;
    }

    public boolean hasPriorityHigherThan(Rank rank) {
        return this.priority > rank.priority;
    }
    public boolean hasPriorityHigherThanOrEqual(Rank rank) {
        return this.priority >= rank.priority;
    }
}