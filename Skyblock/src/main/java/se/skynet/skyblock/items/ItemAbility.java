package se.skynet.skyblock.items;

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
