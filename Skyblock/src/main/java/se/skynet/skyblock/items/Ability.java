package se.skynet.skyblock.items;

import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.AbilityAction;

import java.util.ArrayList;
import java.util.List;

public enum Ability {
    EMBER_ROD_FIRE_BLAST("Fire Blast", AbilityAction.RIGHT_CLICK, "§7Shoots 3 Balls which deal §c30§7 damage in rapid succession in from of you.", 150, 30, true),
    COIN_WAND_GIVE_COINS("Coin Wand", AbilityAction.RIGHT_CLICK, "§7Gives you between §60§7 and §61,000,000§7 coins.", 0, 0, true),
    DRAGON_RAGE("Dragon Rage", AbilityAction.RIGHT_CLICK, "§7All Monsters in front of you take §a12,000§7 damage. Hit monsters take large knockback.", 100, 0, true),
    SHADOW_FURY("Shadow Fury", AbilityAction.RIGHT_CLICK, "§7Teleport to the target location and deal §c100§7 damage to all monsters in a §a3§7 block radius.", 0, 15, true);
    private final String displayName;
    private final AbilityAction abilityAction;
    private final String description;
    private final int manacost;
    private final int cooldown;
    private final boolean isCoolDownUniqe;

    Ability(String displayName, AbilityAction abilityAction, String description, int manacost, int cooldown, boolean isCoolDownUnique) {
        this.displayName = displayName;
        this.abilityAction = abilityAction;
        this.description = description;
        this.manacost = manacost;
        this.cooldown = cooldown;
        this.isCoolDownUniqe = isCoolDownUnique;
    }

    public List<String> render(SkyblockPlayer player) {
        List<String> lore = new ArrayList<>();
        
        lore.add("§6Ability: " + displayName + " §6§l" + abilityAction.getActionText());
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : description.split(" ")) {
            if(stringBuilder.length() + s.length() > 50) {
                lore.add("§7" + stringBuilder.toString());
                stringBuilder = new StringBuilder();
            } else {
                stringBuilder.append(s).append(" ");
            }
        }
        if(stringBuilder.length() > 0) {
            lore.add("§7" + stringBuilder.toString());
        }
        if (manacost > 0) {
            lore.add("§8Mana Cost: §b" + manacost);
        }
        if (cooldown > 0) {
            lore.add("§8Cooldown: §a" + cooldown + "s");
        }
        lore.add("");
        return lore;
    }

    public AbilityAction getAbilityAction() {
        return abilityAction;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getManacost() {
        return manacost;
    }
}
