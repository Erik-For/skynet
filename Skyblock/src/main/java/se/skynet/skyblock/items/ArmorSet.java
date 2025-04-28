package se.skynet.skyblock.items;

import se.skynet.skyblock.playerdata.Stat;

public class ArmorSet {
    private SkyblockItem head;
    private SkyblockItem chest;
    private SkyblockItem legs;
    private SkyblockItem boots;

    public ArmorSet(){

    }

    public ArmorSet(SkyblockItem head, SkyblockItem chest, SkyblockItem legs, SkyblockItem boots) {
        this.head = head;
        this.chest = chest;
        this.legs = legs;
        this.boots = boots;
    }

    public void setBoots(SkyblockItem boots) {
        this.boots = boots;
    }

    public void setChest(SkyblockItem chest) {
        this.chest = chest;
    }

    public void setHead(SkyblockItem head) {
        this.head = head;
    }

    public void setLegs(SkyblockItem legs) {
        this.legs = legs;
    }

    public SkyblockItem getBoots() {
        return boots;
    }

    public SkyblockItem getChest() {
        return chest;
    }

    public SkyblockItem getHead() {
        return head;
    }

    public SkyblockItem getLegs() {
        return legs;
    }

    public Integer getStat(Stat stat) {
        int amount = 0;
        if (head != null) {
            amount += head.getAttribute(stat);
        }
        if (chest != null) {
            amount += chest.getAttribute(stat);
        }
        if (legs != null) {
            amount += legs.getAttribute(stat);
        }
        if (boots != null) {
            amount += boots.getAttribute(stat);
        }
        return amount;
    }
}
