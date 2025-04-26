package se.skynet.skyblock.items;

import org.bukkit.ChatColor;
import se.skynet.skyblock.items.items.Ability;

import java.util.ArrayList;
import java.util.List;

public class ItemAbility {
    private Ability ability;
    private AbilityImplementation implementation;

    public ItemAbility(Ability ability, AbilityImplementation implementation) {
        this.ability = ability;
        this.implementation = implementation;
    }

    public Ability getAbility() {
        return ability;
    }

    public AbilityImplementation getImplementation() {
        return implementation;
    }
}
