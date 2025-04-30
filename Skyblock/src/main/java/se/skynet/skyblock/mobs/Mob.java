package se.skynet.skyblock.mobs;

import net.minecraft.server.v1_8_R3.EntityZombie;
import org.bukkit.entity.EntityType;

public enum Mob {
    GRAVEYARD_ZOMBIE("Graveyard Zombie", 100, 1, EntityType.ZOMBIE, false, entity -> {
        EntityZombie zombie = (EntityZombie) entity;
        zombie.setBaby(false);
        zombie.setVillager(false);
    }),
    ;

    private final String name;
    private final int maxHealth;
    private final int level;
    private final EntityType entityType;
    private final boolean trueDamage;
    private final SkyblockMob.CustomMobPathFinding pathFinding;

    Mob(String name, int maxHealth, int level, EntityType entityType, boolean trueDamage, SkyblockMob.CustomMobPathFinding pathFinding) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.level = level;
        this.entityType = entityType;
        this.trueDamage = trueDamage;
        this.pathFinding = pathFinding;
    }

    public String getName() {
        return name;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public EntityType getEntityType() {
        return entityType;
    }
    public SkyblockMob.CustomMobPathFinding getPathFinding() {
        return pathFinding;
    }


    public int getLevel() {
        return level;
    }

    public boolean doesTrueDamage() {
        return trueDamage;
    }
}
