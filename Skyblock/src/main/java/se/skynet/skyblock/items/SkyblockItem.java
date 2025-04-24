package se.skynet.skyblock.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.items.special.AdminItem;
import se.skynet.skyblock.playerdata.Stat;
import se.skynet.skyserverbase.Rank;
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
    private List<String> lore = new ArrayList<>();
    private Map<Stat, Integer> attributes = new HashMap<>();
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
        if(NBTHelper.hasTag(item, "attributes")) {
            Map<String, Object> attributes = NBTHelper.getNestedCompound(item, "attributes");
            attributes.forEach((k,v) -> {
                try {
                    Stat stat = Stat.valueOf(k);
                    this.attributes.put(stat, (Integer) v);
                } catch (IllegalArgumentException e) {
                    // Ignore invalid stat
                }
            });
        }
        if(item.getMaxStackSize() == 1) {
            this.stackable = false;
        }
        this.amount = item.getAmount();

    }

    public void addLore(String lore) {
        this.lore.add(lore);
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


    public ItemStack render(SkyblockPlayer player) {
        if(player.isInDevMode()){
            player.getPlayer().sendMessage(ChatColor.RED + "Rendering item: " + name + " Rarity: " + rarity.getName() + " Type: " + type.name());
        }
        if(this.getRarity() == ItemRarity.ADMIN && !Skyblock.getInstance().getParentPlugin().getPlayerDataManager().getPlayerData(player.getPlayer().getUniqueId()).getRank().hasPriorityHigherThanOrEqual(Rank.ADMIN)) {
            player.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to use this item, removing it from your inventory");;
            return new AdminItem().render(player);
        }

        ItemStack itemStack = new ItemStack(material);
        if(!stackable) {
            itemStack = NBTHelper.setString(itemStack, "s_id", this.id.toString());
        }
        if(name != "") {
            itemStack = NBTHelper.setString(itemStack, "s_name", name);
            itemStack = NBTHelper.setString(itemStack, "s_type", type.toString());
        }
        itemStack = NBTHelper.setString(itemStack, "s_rarity", rarity.name());


        Map<String, Object> attributes = new HashMap<>();
        this.attributes.forEach((k,v) -> {
            attributes.put(k.name(), v);
        });
        if(attributes.size() > 0) {
            itemStack = NBTHelper.setNestedCompound(itemStack, "attributes", attributes);
        }
        if(name != "") {
            ItemUtils.setName(itemStack, rarity.getColor() + name);
        }

        List<String> lore = new ArrayList<>(this.lore);
        Arrays.stream(Stat.values()).filter(a -> attributes.containsKey(a)).forEach(a -> {
            if (this.attributes.get(a) != 0) {
                lore.add(ChatColor.GRAY + a.getName() + ":" + a.getColor() + " +" + attributes.get(a) + a.getUnit());
            }
        });

        lore.add("");
        for (ItemAbility ability : abilities) {
            lore.addAll(ability.render());
        }
        lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.getName());
        ItemUtils.setLore(itemStack, lore);

        itemStack.setAmount(amount);

        return itemStack;
    }



    public void setAttribute(Stat attribute, int value) {
        attributes.put(attribute, value);
    }

    public int getAttribute(Stat attribute) {
        return attributes.getOrDefault(attribute, 0);
    }

    public void removeAttribute(Stat attribute) {
        attributes.remove(attribute);
    }

    public void addAbility(ItemAbility ability) {
        abilities.add(ability);
    }

    public Map<Stat, Integer> getAttributes() {
        return attributes;
    }

    public static boolean isSkyblockItem(ItemStack item) {
        return NBTHelper.hasTag(item, "s_type");
    }
}
