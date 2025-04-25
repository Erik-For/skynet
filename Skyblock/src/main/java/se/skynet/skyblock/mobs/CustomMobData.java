package se.skynet.skyblock.mobs;

public class CustomMobData {
    private String name;
    private int maxHealth;

    private int health;
    private int level;
    private Mob type;

    public CustomMobData(String name, int maxHealth, int level, Mob type) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.level = level;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLevel() {
        return level;
    }

    public Mob getType() {
        return type;
    }
}
