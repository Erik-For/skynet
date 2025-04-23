package se.skynet.skyblock.playerdata;

import java.util.List;
import java.util.UUID;

public class PlayerProfile {
    private Skills skills;

    private Float coins;
    private UUID uuid;

    private Profile profile;

    public PlayerProfile(UUID uuid, List<SkillProgression> skills) {
        this.skills =  new Skills(skills);
        this.uuid = uuid;
    }

    public Float getCoins() {
        return coins;
    }

    public void setCoins(Float coins) {
        this.coins = coins;
    }

    public void addCoins(Float coins) {
        this.coins += coins;
    }

    public List<SkillProgression> getSkillsAsList() {
        return skills.getSkills();
    }

    public UUID getUuid() {
        return uuid;
    }

    public SkillProgression getSkill(SkillType skill) {
        return skills.getSkill(skill);
    }

    public float getSkillAvrage() {
        int totalLevel = 0;
        for (SkillProgression skill : skills.getSkills()) {
            totalLevel += skill.getLevel();
        }
        return (float) totalLevel / skills.getSkills().size();
    }
}
