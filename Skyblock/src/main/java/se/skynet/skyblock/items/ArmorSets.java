package se.skynet.skyblock.items;

import java.util.Arrays;
import java.util.List;

public enum ArmorSets {
    SuperiorDragonArmor("Superior Dragon Armor", SkyblockItemID.SUPERIOR_DRAGON_HELMET, SkyblockItemID.SUPERIOR_DRAGON_CHESTPLATE, SkyblockItemID.SUPERIOR_DRAGON_LEGGINGS, SkyblockItemID.SUPERIOR_DRAGON_BOOTS),;



    private final String name;
    private SkyblockItemID head;
    private SkyblockItemID chest;
    private SkyblockItemID legs;
    private SkyblockItemID feet;
    ArmorSets(String name, SkyblockItemID head, SkyblockItemID chest, SkyblockItemID legs, SkyblockItemID feet) {
        this.name = name;
        this.head = head;
        this.chest = chest;
        this.legs = legs;
        this.feet = feet;
    }

    public String getName() {
        return name;
    }

    public boolean isFullSet(ArmorSet set) {
        if (this.head != null) {
            if(set.getHead() == null) {
                return false;
            }
            if (set.getHead().getItemID() != head) {
                return false;
            }
        }
        if (this.chest != null) {
            if(set.getChest() == null) {
                return false;
            }
            if (set.getChest().getItemID() != chest) {
                return false;
            }
        }
        if (this.legs != null) {
            if(set.getLegs() == null) {
                return false;
            }
            if (set.getLegs().getItemID() != legs) {
                return false;
            }
        }
        if (this.feet != null) {
            if(set.getBoots() == null) {
                return false;
            }
            if (set.getBoots().getItemID() != feet) {
                return false;
            }
        }

        return true;
    }
}
