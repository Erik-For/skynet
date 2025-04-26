package se.skynet.skyblock.items;

import se.skynet.skyblock.items.items.admin.AdminCoinWand;
import se.skynet.skyblock.items.items.end.SuperiorDargonHelmet;
import se.skynet.skyblock.items.items.nether.EmberRod;
import se.skynet.skyblock.items.items.special.AdminItem;

public enum SkyblockItemType {
    VANILLA(SkyblockItem.class),
    ADMIN_COIN_WAND(AdminCoinWand.class),
    EMBER_ROD(EmberRod.class),
    ADMIN_ITEM(AdminItem.class),
    SUPERIOR_DRAGON_HELMET(SuperiorDargonHelmet.class),

    ;

    private Class<? extends SkyblockItem> itemClass;

    SkyblockItemType(Class<? extends SkyblockItem> itemClass) {
        this.itemClass = itemClass;
    }

    public Class<? extends SkyblockItem> getItemClass() {
        return itemClass;
    }

}
