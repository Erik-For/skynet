package se.skynet.skyblock.items.items.end;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.playerdata.Stat;

public class AspectOfTheDragons extends SkyblockItem implements SkyblockItemEvents {
    public AspectOfTheDragons() {
        super(Material.DIAMOND_SWORD, "Aspect of the Dragons", SkyblockItemID.ASPECT_OF_THE_DRAGONS, ItemRarity.LEGENDARY, 1, false);
    }

    public AspectOfTheDragons(ItemStack item) {
        super(item);
    }

    @Override
    protected void setupItem() {
        setAttribute(Stat.DAMAGE, 225);
        setAttribute(Stat.STRENGTH, 100);
        setType(SkyblockItemType.SWORD);
        addAbility(new ItemAbility(Ability.DRAGON_RAGE, new AbilityImplementation() {
            @Override
            public boolean onUse(AbilityAction action, Ability ability, SkyblockItem item, SkyblockPlayer player, ItemStack itemStack) {
                // implement the ability logic here with particels and stuff
                return true;
            }
        }));
    }
}
