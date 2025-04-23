package se.skynet.skyblock.mobs;

import org.bukkit.entity.EntityType;

public enum Mob {
    GRAVEYARD_ZOMBIE("Graveyard Zombie", 100, 1, EntityType.ZOMBIE, entity -> {}),
    ;

    private final String name;
    private final int maxHealth;
    private final int level;
    private final EntityType entityType;
    private final SkyblockMob.CustomMobPathFinding pathFinding;

    Mob(String name, int maxHealth, int level, EntityType entityType, SkyblockMob.CustomMobPathFinding pathFinding) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.level = level;
        this.entityType = entityType;
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
}
