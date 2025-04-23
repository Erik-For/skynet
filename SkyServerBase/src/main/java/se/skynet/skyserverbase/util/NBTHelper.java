package se.skynet.skyserverbase.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class NBTHelper {

    public static NBTTagCompound getNBTCompound(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        return compound;
    }

    public static ItemStack applyNBTCompound(ItemStack item, NBTTagCompound compound) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        nmsItem.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static ItemStack setString(ItemStack item, String key, String value) {
        NBTTagCompound compound = getNBTCompound(item);
        compound.setString(key, value);
        return applyNBTCompound(item, compound);
    }


    public static String getString(ItemStack item, String key, String defaultValue) {
        NBTTagCompound compound = getNBTCompound(item);
        return compound.hasKey(key) ? compound.getString(key) : defaultValue;
    }


    public static ItemStack setInt(ItemStack item, String key, int value) {
        NBTTagCompound compound = getNBTCompound(item);
        compound.setInt(key, value);
        return applyNBTCompound(item, compound);
    }


    public static int getInt(ItemStack item, String key, int defaultValue) {
        NBTTagCompound compound = getNBTCompound(item);
        return compound.hasKey(key) ? compound.getInt(key) : defaultValue;
    }


    public static ItemStack setBoolean(ItemStack item, String key, boolean value) {
        NBTTagCompound compound = getNBTCompound(item);
        compound.setBoolean(key, value);
        return applyNBTCompound(item, compound);
    }


    public static boolean getBoolean(ItemStack item, String key, boolean defaultValue) {
        NBTTagCompound compound = getNBTCompound(item);
        return compound.hasKey(key) ? compound.getBoolean(key) : defaultValue;
    }


    public static ItemStack hideAttributes(ItemStack item) {
        NBTTagCompound compound = getNBTCompound(item);
        compound.setInt("HideFlags", 63); // Hide all flags
        return applyNBTCompound(item, compound);
    }


    public static ItemStack setLore(ItemStack item, List<String> loreLines) {
        NBTTagCompound compound = getNBTCompound(item);

        // Create display tag if it doesn't exist
        if (!compound.hasKey("display")) {
            compound.set("display", new NBTTagCompound());
        }

        NBTTagCompound display = compound.getCompound("display");

        // Create lore tag list
        NBTTagList lore = new NBTTagList();
        for (String line : loreLines) {
            lore.add(new NBTTagString(line));
        }

        display.set("Lore", lore);
        return applyNBTCompound(item, compound);
    }


    public static ItemStack setName(ItemStack item, String name) {
        NBTTagCompound compound = getNBTCompound(item);

        // Create display tag if it doesn't exist
        if (!compound.hasKey("display")) {
            compound.set("display", new NBTTagCompound());
        }

        NBTTagCompound display = compound.getCompound("display");
        display.setString("Name", name);

        return applyNBTCompound(item, compound);
    }


    public static ItemStack setUnbreakable(ItemStack item) {
        NBTTagCompound compound = getNBTCompound(item);
        compound.setBoolean("Unbreakable", true);
        return applyNBTCompound(item, compound);
    }


    public static ItemStack setNestedCompound(ItemStack item, String key, Map<String, Object> nestedData) {
        NBTTagCompound compound = getNBTCompound(item);
        NBTTagCompound nestedCompound = new NBTTagCompound();

        for (Map.Entry<String, Object> entry : nestedData.entrySet()) {
            Object value = entry.getValue();
            String dataKey = entry.getKey();

            if (value instanceof String) {
                nestedCompound.setString(dataKey, (String) value);
            } else if (value instanceof Integer) {
                nestedCompound.setInt(dataKey, (Integer) value);
            } else if (value instanceof Boolean) {
                nestedCompound.setBoolean(dataKey, (Boolean) value);
            } else if (value instanceof Double) {
                nestedCompound.setDouble(dataKey, (Double) value);
            } else if (value instanceof Float) {
                nestedCompound.setFloat(dataKey, (Float) value);
            } else if (value instanceof Long) {
                nestedCompound.setLong(dataKey, (Long) value);
            }
        }

        compound.set(key, nestedCompound);
        return applyNBTCompound(item, compound);
    }


    public static boolean hasTag(ItemStack item, String key) {
        NBTTagCompound compound = getNBTCompound(item);
        return compound.hasKey(key);
    }


    public static ItemStack removeTag(ItemStack item, String key) {
        NBTTagCompound compound = getNBTCompound(item);
        compound.remove(key);
        return applyNBTCompound(item, compound);
    }

    public static String dumpNBT(ItemStack item) {
        NBTTagCompound compound = getNBTCompound(item);
        return prettyPrint(compound);
    }

    public static String prettyPrint(NBTBase nbt) {
        StringBuilder sb = new StringBuilder();
        prettyPrintHelper(nbt, 0, sb);
        return sb.toString();
    }

    private static void prettyPrintHelper(NBTBase nbt, int indent, StringBuilder sb) {
        String indentStr = repeat(" ", indent*2);
        if (nbt instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            sb.append(indentStr).append("{\n");
            for (Object keyObj : compound.c()) {
                String key = (String) keyObj;
                NBTBase value = compound.get(key);
                sb.append(indentStr).append("  ").append(key).append(": ");
                prettyPrintHelper(value, indent + 1, sb);
                sb.append("\n");
            }
            sb.append(indentStr).append("}");
        } else if (nbt instanceof NBTTagList) {
            NBTTagList list = (NBTTagList) nbt;
            if (list.size() == 0) {
                sb.append(indentStr).append("[]");
            } else {
                sb.append(indentStr).append("[\n");
                for (int i = 0; i < list.size(); i++) {
                    NBTBase element = list.get(i);
                    sb.append(indentStr).append("  ").append(i).append(": ");
                    prettyPrintHelper(element, indent + 1, sb);
                    sb.append("\n");
                }
                sb.append(indentStr).append("]");
            }
        } else if (nbt instanceof NBTTagString) {
            // Explicitly handle strings to ensure they display without extra quotes
            String value = ((NBTTagString) nbt).a_(); // Get the string value
            sb.append("\"").append(value).append("\"");
        } else {
            sb.append(nbt.toString());
        }
    }

    private static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}