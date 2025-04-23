package se.skynet.skyblock.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyserverbase.gui.ItemUtils;
import se.skynet.skyserverbase.util.NBTHelper;

import java.util.*;

public abstract class SkyblockItem {
    private String name;
    private SkyblockItemType type;
    private int amount;
    private ItemRarity rarity;
    private boolean stackable;

    private Material material;
    private UUID id;
    private HashMap<ItemAttribute, Integer> attributes = new HashMap<>();
    private List<ItemAbility> abilities = new ArrayList<>();

    public SkyblockItem(Material material, String name, SkyblockItemType type, ItemRarity rarity, int amount, boolean stackable) {
        this.material = material;
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.amount = amount;
        this.stackable = stackable;
        this.id = UUID.randomUUID();
    }

    public SkyblockItem(ItemStack item) {
        this.material = item.getType();
        this.name = NBTHelper.getString(item, "s_name", "err");
        this.type = SkyblockItemType.valueOf(NBTHelper.getString(item, "s_type", "VANILLA"));
        this.rarity = ItemRarity.valueOf(NBTHelper.getString(item, "s_rarity", "COMMON"));
        if(NBTHelper.hasTag(item, "s_id")) {
            this.id = UUID.fromString(NBTHelper.getString(item, "s_id", "err"));
        } else {
            this.id = UUID.randomUUID();
            this.stackable = false;
        }
        if(item.getMaxStackSize() == 1) {
            this.stackable = false;
        }
        this.amount = item.getAmount();

    }

    public String getName() {
        return name;
    }

    public SkyblockItemType getType() {
        return type;
    }

    public ItemRarity getRarity() {
        return rarity;
    }


    public ItemStack render() {
        ItemStack itemStack = new ItemStack(material);
        if(!stackable) {
            itemStack = NBTHelper.setString(itemStack, "s_id", this.id.toString());
        }
        if(name != "") {
            itemStack = NBTHelper.setString(itemStack, "s_name", name);
            itemStack = NBTHelper.setString(itemStack, "s_type", type.toString());
        }
        itemStack = NBTHelper.setString(itemStack, "s_rarity", rarity.name());

        for (ItemAttribute attribute : attributes.keySet()) {
            itemStack = NBTHelper.setInt(itemStack, attribute.name(), attributes.get(attribute));
        }

        if(name != "") {
            ItemUtils.setName(itemStack, rarity.getColor() + name);
        }

        List<String> lore = new ArrayList<>();
        Arrays.stream(ItemAttribute.values()).filter(a -> attributes.containsKey(a)).forEach(a -> {
            if (attributes.get(a) != 0) {
                lore.add(ChatColor.GRAY + a.getName() + ":" + a.getColor() + " +" + attributes.get(a) + a.getUnit());
            }
        });

        lore.add("");
        for (ItemAbility ability : abilities) {
            lore.addAll(ability.render());
        }
        lore.add(rarity.getColor() + rarity.getName());
        ItemUtils.setLore(itemStack, lore);

        itemStack.setAmount(amount);

        return itemStack;
    }

    public void setAttribute(ItemAttribute attribute, int value) {
        attributes.put(attribute, value);
    }

    public int getAttribute(ItemAttribute attribute) {
        return attributes.getOrDefault(attribute, 0);
    }

    public void removeAttribute(ItemAttribute attribute) {
        attributes.remove(attribute);
    }

    public void addAbility(ItemAbility ability) {
        abilities.add(ability);
    }

    public HashMap<ItemAttribute, Integer> getAttributes() {
        return attributes;
    }

    public static boolean isSkyblockItem(ItemStack item) {
        return NBTHelper.hasTag(item, "s_type");
    }
}
