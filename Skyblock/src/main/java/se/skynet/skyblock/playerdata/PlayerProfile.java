package se.skynet.skyblock.playerdata;

import java.util.List;
import java.util.UUID;

public class PlayerProfile {
    private Skills skills;

    private Double coins;
    private Double bankCoins;
    private UUID uuid;

    private Profile profile;
    private UUID profileUUID;

    public PlayerProfile(UUID uuid, UUID profileUUID, List<SkillProgression> skills, Double coins, Double bankCoins) {
        this.skills =  new Skills(skills);
        this.profileUUID = profileUUID;
        this.uuid = uuid;
        this.coins = coins;
        this.bankCoins = bankCoins;
    }

    public Double getCoins() {
        return coins;
    }

    public void setCoins(Double coins) {
        this.coins = coins;
    }
    public void addCoins(Double coins) {
        this.coins += coins;
    }

    public Double getBankCoins() {
        return bankCoins;
    }

    public void setBankCoins(Double bankCoins) {
        this.bankCoins = bankCoins;
    }
    public void removeCoins(Double amount) {
        this.coins -= amount;
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

    public UUID getProfileUUID() {
        return profileUUID;
    }
}
