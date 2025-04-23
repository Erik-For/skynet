package se.skynet.skyblock.mobs;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import se.skynet.skyblock.Skyblock;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SkyblockMob {

    interface CustomMobPathFinding {
        void setPathFinding(EntityCreature entity);
    }
    private static final HashMap<UUID, NBTTagCompound> customDataStore = new HashMap<>();
    private LivingEntity entity;

    // pass a lambda to the constructor

    public static SkyblockMob spawnMob(Mob mob, Location loc) {
        Entity entity = mob.getEntityType().getEntityClass().cast(loc.getWorld().spawnEntity(loc, mob.getEntityType()));
        if (entity instanceof LivingEntity) {
            return new SkyblockMob((LivingEntity) entity, mob.getMaxHealth(), mob.getName(), mob.getLevel(), mob.getPathFinding());
        }
        return null;
    }
    public SkyblockMob(LivingEntity entity, int health, String name, int level, CustomMobPathFinding pathFinding) {
        this.entity = entity;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!entity.isValid() || entity.isDead()) return;
                setData(entity, "wrapped", true);
                setData(entity, "maxhp", health);
                setData(entity, "hp", health);
                setData(entity, "name", name);
                setData(entity, "lvl", level);
                ensurePersistence(entity);
                entity.setCustomNameVisible(true);
                entity.setCustomName(getCustomNameFormatted());

                net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
                if (nmsEntity instanceof EntityCreature) {
                    pathFinding.setPathFinding((EntityCreature) nmsEntity);
                }
            }
        }.runTask(Skyblock.getPlugin(Skyblock.class));
    }
    public SkyblockMob(LivingEntity entity) {
        this.entity = entity;
        if (!isWrapped(entity)) return;
    }

    public LivingEntity getHandle() {
        return entity;
    }

    public String getCustomNameFormatted() {
        return ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Lv" + getLevel() + ChatColor.DARK_GRAY + "] " + ChatColor.RED + getName() + " " + ChatColor.GREEN + getHealth() + "/" + getMaxHealth() + ChatColor.RED + " ❤";
    }

    public String getName() {
        Object data = getData(entity, "name");
        return data != null ? (String) data : "Unnamed";
    }

    public int getMaxHealth() {
        Object data = getData(entity, "maxhp");
        return data != null ? (int) data : 20;
    }

    public int getHealth() {
        Object data = getData(entity, "hp");
        return data != null ? (int) data : getMaxHealth();
    }

    public int getLevel() {
        Object data = getData(entity, "lvl");
        return data != null ? (int) data : 1;
    }

    public static boolean isWrapped(Entity entity) {
        Object data = getData(entity, "wrapped");
        return data != null && (boolean) data;
    }

    public void setHealth(int health) {
        if(health <= 0) {
            entity.damage(entity.getMaxHealth() * 10);
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!entity.isValid() || entity.isDead()) return;
                setData(entity, "hp", health);
                entity.setCustomName(getCustomNameFormatted());
            }
        }.runTask(Skyblock.getPlugin(Skyblock.class));
    }

    public void setMaxHealth(int health) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!entity.isValid() || entity.isDead()) return;
                setData(entity, "maxhp", health);
                entity.setCustomName(getCustomNameFormatted());
            }
        }.runTask(Skyblock.getPlugin(Skyblock.class));
    }

    public static void clearGoals(EntityCreature entity) {
        try {
            Field goalB = PathfinderGoalSelector.class.getDeclaredField("b");
            Field goalC = PathfinderGoalSelector.class.getDeclaredField("c");
            goalB.setAccessible(true);
            goalC.setAccessible(true);

            ((List<?>) goalB.get(entity.goalSelector)).clear();
            ((List<?>) goalC.get(entity.goalSelector)).clear();
            ((List<?>) goalB.get(entity.targetSelector)).clear();
            ((List<?>) goalC.get(entity.targetSelector)).clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void ensurePersistence(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound entityTag = new NBTTagCompound();
        nmsEntity.e(entityTag);
        entityTag.setBoolean("PersistenceRequired", true);
        nmsEntity.f(entityTag);
        nmsEntity.world.entityJoinedWorld(nmsEntity, false);
    }

    private void setData(Entity entity, String key, Object value) {
        NBTTagCompound customData = customDataStore.computeIfAbsent(entity.getUniqueId(), k -> new NBTTagCompound());
        String tagKey = "c_" + key;
        if (value instanceof String) {
            customData.setString(tagKey, (String) value);
        } else if (value instanceof Integer) {
            customData.setInt(tagKey, (Integer) value);
        } else if (value instanceof Boolean) {
            customData.setBoolean(tagKey, (Boolean) value);
        } else if (value instanceof Double) {
            customData.setDouble(tagKey, (Double) value);
        } else if (value instanceof Float) {
            customData.setFloat(tagKey, (Float) value);
        } else {
            customData.setString(tagKey, value.toString());
        }
        customDataStore.put(entity.getUniqueId(), customData);
    }

    public static boolean hasData(Entity entity, String key) {
        NBTTagCompound customData = customDataStore.get(entity.getUniqueId());
        return customData != null && customData.hasKey("c_" + key);
    }

    private static Object getData(Entity entity, String key) {
        NBTTagCompound customData = customDataStore.get(entity.getUniqueId());
        String tagKey = "c_" + key;
        if (customData == null || !customData.hasKey(tagKey)) return null;
        if (customData.hasKeyOfType(tagKey, 1)) {
            byte value = customData.getByte(tagKey);
            return (value == 0 || value == 1) ? value == 1 : value;
        } else if (customData.hasKeyOfType(tagKey, 3)) {
            return customData.getInt(tagKey);
        } else if (customData.hasKeyOfType(tagKey, 6)) {
            return customData.getDouble(tagKey);
        } else if (customData.hasKeyOfType(tagKey, 5)) {
            return customData.getFloat(tagKey);
        } else if (customData.hasKeyOfType(tagKey, 8)) {
            return customData.getString(tagKey);
        } else {
            return customData.get(tagKey).toString();
        }
    }

    private static void removeData(Entity entity, String key) {
        NBTTagCompound customData = customDataStore.get(entity.getUniqueId());
        if (customData != null) {
            customData.remove("c_" + key);
            if (customData.isEmpty()) {
                customDataStore.remove(entity.getUniqueId());
            } else {
                customDataStore.put(entity.getUniqueId(), customData);
            }
        }
    }
}