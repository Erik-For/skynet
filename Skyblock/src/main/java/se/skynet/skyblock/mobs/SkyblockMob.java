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
    private static final HashMap<UUID, CustomMobData> customDataStore = new HashMap<>();
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
        if (!isWrapped(entity)) {
            customDataStore.put(entity.getUniqueId(), new CustomMobData(name, health, level, Mob.GRAVEYARD_ZOMBIE));
        }
        entity.setCustomName(customNameFormatted(level, name, health));
        //entity.setCustomNameVisible(true);

        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity instanceof EntityCreature) {
            pathFinding.setPathFinding((EntityCreature) nmsEntity);
        }
    }
    public SkyblockMob(LivingEntity entity) {
        this.entity = entity;
        if (!isWrapped(entity)) return;
    }

    private CustomMobData getCustomMobData() {
        return customDataStore.get(entity.getUniqueId());
    }

    public Integer getHealth() {
        return getCustomMobData().getHealth();
    }

    public void setHealth(int health) {
        if(health <= 0) {
            entity.damage(1000);
            customDataStore.remove(entity.getUniqueId());
            return;
        }
        getCustomMobData().setHealth(health);
        entity.setCustomName(customNameFormatted(getCustomMobData().getLevel(), getCustomMobData().getName(), health));
    }
    public LivingEntity getHandle() {
        return entity;
    }

    private String customNameFormatted(int level, String name, int maxHealth) {
        return ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Lv" + level + ChatColor.DARK_GRAY + "] " + ChatColor.RED + name + " " + ChatColor.GREEN + maxHealth + "/" + maxHealth + ChatColor.RED + " ❤";
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

    public static boolean isWrapped(Entity entity) {
        return customDataStore.containsKey(entity.getUniqueId());
    }

}