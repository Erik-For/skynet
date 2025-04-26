package se.skynet.skyblock.items;

import org.bukkit.Bukkit;
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

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class SkyblockItem {
    // Core properties
    private final String name;
    private final SkyblockItemType type;
    private final ItemRarity rarity;
    private final UUID id;
    private final boolean stackable;

    // Item appearance
    private Material material;
    private String texture;
    private int amount;
    private final List<String> lore = new ArrayList<>();

    // Item stats and functionality
    private final Map<Stat, Integer> attributes = new HashMap<>();
    private final List<ItemAbility> abilities = new ArrayList<>();

    /**
     * Creates a new Skyblock item with the given properties.
     */
    public SkyblockItem(Material material, String name, SkyblockItemType type, ItemRarity rarity, int amount, boolean stackable) {
        this.material = material;
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.amount = amount;
        this.stackable = stackable;
        this.id = UUID.randomUUID();
        setupItem();
    }

    /**
     * Creates a Skyblock item from an existing ItemStack.
     */
    public SkyblockItem(ItemStack item) {
        if(!isSkyblockItem(item)) {
            this.material = item.getType();
            this.name = "";
            this.type = SkyblockItemType.VANILLA;
            this.rarity = ItemRarity.COMMON;
            this.amount = item.getAmount();
            this.stackable = item.getMaxStackSize() != 1;
            this.id = UUID.randomUUID();
            return;
        }

        this.material = item.getType();
        this.name = NBTHelper.getString(item, "s_name", "err");
        this.type = SkyblockItemType.valueOf(NBTHelper.getString(item, "s_type", "VANILLA"));
        this.rarity = ItemRarity.valueOf(NBTHelper.getString(item, "s_rarity", "COMMON"));
        this.amount = item.getAmount();

        // Load item ID if present (non-stackable items)
        if (NBTHelper.hasTag(item, "s_id")) {
            this.id = UUID.fromString(NBTHelper.getString(item, "s_id", ""));
            this.stackable = false;
        } else {
            this.id = UUID.randomUUID();
            this.stackable = item.getMaxStackSize() != 1;
        }

        /*
        // Load item attributes (this should be done in the ItemStack based constructor)
        if (NBTHelper.hasTag(item, "s_attributes")) {
            Map<String, Object> attributeMap = NBTHelper.getNestedCompound(item, "s_attributes");
            attributeMap.forEach((key, value) -> {
                try {
                    Stat stat = Stat.valueOf(key);
                    this.attributes.put(stat, (Integer) value);
                } catch (IllegalArgumentException e) {
                    // Ignore invalid stats
                }
            });
        }
         */
        setupItem();
    }

    protected abstract void setupItem();

    // ---------- Item Attributes ----------

    public void setAttribute(Stat attribute, int value) {
        attributes.put(attribute, value);
    }

    public int getAttribute(Stat attribute) {
        return attributes.getOrDefault(attribute, 0);
    }

    public void removeAttribute(Stat attribute) {
        attributes.remove(attribute);
    }

    public Map<Stat, Integer> getAttributes() {
        return attributes;
    }

    public void addAbility(ItemAbility ability) {
        abilities.add(ability);
    }

    public List<ItemAbility> getAbilities() {
        return abilities;
    }

    public void addLore(String loreLine) {
        this.lore.add(loreLine);
    }

    public void setSkullTexture(String texture) {
        this.texture = texture;
    }

    // ---------- Getters ----------

    public String getName() {
        return name;
    }

    public SkyblockItemType getType() {
        return type;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public UUID getId() {
        return id;
    }

    // ---------- Item Rendering ----------

    /**
     * Creates a new ItemStack representing this Skyblock item.
     */
    public ItemStack render(SkyblockPlayer player) {
        ItemStack item = new ItemStack(material);
        if (material == Material.SKULL_ITEM && texture != null) {
            item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        }
        return render(item, player);
    }

    /**
     * Applies Skyblock item properties to an existing ItemStack.
     */
    public ItemStack render(ItemStack itemStack, SkyblockPlayer player) {
        // Debug mode info
        if (player.isInDevMode()) {
            player.getPlayer().sendMessage(ChatColor.RED + "Rendering item: " + name +
                    " Rarity: " + rarity.getName() + " Type: " + type.name());
        }

        // Admin item permission check
        if (this.getRarity() == ItemRarity.ADMIN &&
                !Skyblock.getInstance().getParentPlugin().getPlayerDataManager()
                        .getPlayerData(player.getPlayer().getUniqueId()).getRank()
                        .hasPriorityHigherThanOrEqual(Rank.ADMIN)) {
            player.getPlayer().sendMessage(ChatColor.RED +
                    "You do not have permission to use this item, removing it from your inventory");
            return new AdminItem().render(player);
        }

        // Apply skull texture if needed
        if (material == Material.SKULL_ITEM && texture != null) {
            ItemUtils.setSkullTexture(itemStack, texture);
        }

        // Apply base NBT data
        if (!stackable) {
            itemStack = NBTHelper.setString(itemStack, "s_id", id.toString());
        }

        if (!name.isEmpty()) {
            itemStack = NBTHelper.setString(itemStack, "s_name", name);
            itemStack = NBTHelper.setString(itemStack, "s_type", type.toString());
        }

        itemStack = NBTHelper.setString(itemStack, "s_rarity", rarity.name());

        /*
        // Store attributes in NBT (this is deprecated, use the attributes map instead)
        if (!attributes.isEmpty()) {
            Map<String, Object> attributeMap = new HashMap<>();
            this.attributes.forEach((key, value) -> attributeMap.put(key.name(), value));
            itemStack = NBTHelper.setNestedCompound(itemStack, "s_attributes", attributeMap);
        }
         */

        // Set display name
        if (!name.isEmpty()) {
            ItemUtils.setName(itemStack, rarity.getColor() + name);
        }

        // Build item lore
        List<String> displayLore = new ArrayList<>(this.lore);

        // Add attributes to lore
        Arrays.stream(Stat.values())
                .filter(attributes::containsKey)
                .forEach(stat -> {
                    int value = attributes.get(stat);
                    if (value != 0) {
                        displayLore.add(ChatColor.GRAY + stat.getName() + ":" +
                                stat.getColor() + " +" + value + stat.getUnit());
                    }
                });

        // Add abilities to lore
        displayLore.add("");
        for (ItemAbility ability : abilities) {
            displayLore.addAll(ability.getAbility().render(player));
        }

        // Add rarity to lore
        displayLore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.getName());

        // Apply lore and amount
        ItemUtils.setLore(itemStack, displayLore);
        itemStack.setAmount(amount);

        return itemStack;
    }

    /**
     * Checks if an ItemStack is a Skyblock item.
     */
    public static boolean isSkyblockItem(ItemStack item) {
        return NBTHelper.hasTag(item, "s_type");
    }

    public static SkyblockItem constructSkyblockItem(Class<? extends SkyblockItem> classType) {
        try {
            return classType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Bukkit.getLogger().warning("Failed to construct SkyblockItem of type " + classType.getName());
            e.printStackTrace();
            return null;
        }
    }

    public static SkyblockItem constructSkyblockItem(Class<? extends SkyblockItem> classType, ItemStack itemStack) {
        try {
            return classType.getConstructor(ItemStack.class).newInstance(itemStack);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            Bukkit.getLogger().warning("Failed to construct SkyblockItem of type " + classType.getName());
            e.printStackTrace();
            return null;
        }
    }
}