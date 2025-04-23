package se.skynet.skyblock.items;

import org.bukkit.ChatColor;

public enum ItemRarity {
    COMMON(ChatColor.WHITE),
    UNCOMMON(ChatColor.GREEN),
    RARE(ChatColor.BLUE),
    EPIC(ChatColor.DARK_PURPLE),
    LEGENDARY(ChatColor.GOLD),
    MYTHIC(ChatColor.LIGHT_PURPLE),
    DIVINE(ChatColor.AQUA),
    SPECIAL(ChatColor.RED),
    VERY_SPECIAL(ChatColor.RED),
    ULTIMATE(ChatColor.DARK_RED),
    ADMIN(ChatColor.DARK_RED),
    ;



    private ChatColor color;

    ItemRarity(ChatColor color) {
        this.color = color;
    }
    public String getName() {
        return this.name().replace("_", " ");
    }

    public ChatColor getColor() {
        return color;
    }

    public ItemRarity getNext() {
        switch (this) {
            case COMMON:
                return UNCOMMON;
            case UNCOMMON:
                return RARE;
            case RARE:
                return EPIC;
            case EPIC:
                return LEGENDARY;
            case LEGENDARY:
                return MYTHIC;
            case MYTHIC:
                return DIVINE;
            case SPECIAL:
                return VERY_SPECIAL;
        }
        return this;
    }
}
