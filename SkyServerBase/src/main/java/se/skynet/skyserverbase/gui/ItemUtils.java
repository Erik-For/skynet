package se.skynet.skyserverbase.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class ItemUtils {

    public static void setName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    public static void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static ItemStack getPlayerHead(String name) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(name);
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getPlayerHeadFromTexture(String texture) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        setSkullTexture(skull, texture);
        return skull;
    }

    public static ItemStack setSkullTexture(ItemStack skull, String texture) {
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);

        return skull;
    }
    public static ItemStack getItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        setName(item, name);
        setLore(item, lore);
        return item;
    }
}
