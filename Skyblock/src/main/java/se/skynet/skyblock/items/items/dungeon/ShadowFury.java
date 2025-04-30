package se.skynet.skyblock.items.items.dungeon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.playerdata.Stat;

public class ShadowFury extends SkyblockItem implements SkyblockItemEvents {
    public ShadowFury() {
        super(Material.DIAMOND_SWORD, "Shadow Fury", SkyblockItemID.SHADOW_FURY, ItemRarity.LEGENDARY, 1, false);
    }

    public ShadowFury(ItemStack item) {
        super(item);
    }

    @Override
    protected void setupItem() {
        setAttribute(Stat.DAMAGE, 310);
        setAttribute(Stat.STRENGTH, 160);
        setAttribute(Stat.CRIT_DAMAGE, 110);
        setAttribute(Stat.SPEED, 40);
        setType(SkyblockItemType.SWORD);
        addAbility(new ItemAbility(Ability.SHADOW_FURY, new AbilityImplementation() {
            @Override
            public boolean onUse(AbilityAction action, Ability ability, SkyblockItem item, SkyblockPlayer player, ItemStack itemStack) {
                // Implement the ability logic here
                return true;
            }
        }));
    }
}
