package se.skynet.skyblock.mobs;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.misc.HologramHelper;
import se.skynet.skyserverbase.manager.HologramManager;

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

    public static boolean isSkyblockMob(Entity entity) {
        if (entity instanceof LivingEntity && isWrapped(entity)) {
            return true;
        }
        return false;
    }
    public static SkyblockMob getMob(Entity entity) {
        if (entity instanceof LivingEntity && isWrapped(entity)) {
            return new SkyblockMob((LivingEntity) entity);
        }
        return null;
    }
    public SkyblockMob(LivingEntity entity, int health, String name, int level, CustomMobPathFinding pathFinding) {
        this.entity = entity;

        World world = entity.getWorld();
        Location loc = entity.getLocation();

        if (!isWrapped(entity)) {
            //ArmorStand armorStand = HologramHelper.createHologram(loc.add(0, 1.5, 0), customNameFormatted(level, name, health));
            customDataStore.put(entity.getUniqueId(), new CustomMobData(name, health, level, Mob.GRAVEYARD_ZOMBIE));
        }
        entity.setCustomName(customNameFormatted(level, name, health));
        entity.setCustomNameVisible(true);

        /* TODO make this work instead of customName because it is visible father back */
        /*
        int hologram = HologramManager.createHologram(entity.getLocation().add(0, 4, 0), customNameFormatted(level, name, health));
        ArmorStand hologramEntity = (ArmorStand) entity.getWorld().getEntities().stream().filter(e -> e.getEntityId() == hologram).findFirst().orElse(null);
        if (hologramEntity != null) {
            hologramEntity.setVisible(true);
            hologramEntity.setSmall(false);
            hologramEntity.setBasePlate(true);
            hologramEntity.setGravity(true);
            hologramEntity.setMarker(false);
            entity.setPassenger(hologramEntity);
        }
         */


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

    public Mob getType() {
        return getCustomMobData().getType();
    }

    public void setHealth(int health) {
        if(health <= 0) {
            entity.damage(1000);
            customDataStore.remove(entity.getUniqueId());
            return;
        }
        getCustomMobData().setHealth(health);
        //getCustomMobData().getArmorStand().setCustomName(customNameFormatted(getCustomMobData().getLevel(), getCustomMobData().getName(), health));
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