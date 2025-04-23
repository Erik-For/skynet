package se.skynet.skyblock.playerdata;

public enum SkillType {
    COMBAT("Combat", "combat", 60),
    MINING("Mining", "mining", 60),
    FORAGING("Foraging", "foraging", 50),
    FARMING("Farming", "farming", 50),
    FISHING("Fishing", "fishing", 50);

    private final String name;
    private final String id;
    private final int maxLevel;

    SkillType(String name, String id, int maxLevel) {
        this.name = name;
        this.id = id;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
