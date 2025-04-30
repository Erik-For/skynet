package se.skynet.skyblock.playerdata;

import java.util.HashMap;
import java.util.Map;

public class SkillProgression {
    private float experience;
    private int level;

    private SkillType skill;

    public SkillProgression(SkillType skill, float experience, int level) {
        this.skill = skill;
        this.experience = experience;
        this.level = level;
    }

    public void addExperience(float amount) {
        this.experience += amount;
        // round experience to 1 decimal place

        this.experience = Math.round(this.experience * 10.0f) / 10.0f;

        while (this.experience >= calculateExperienceToReachLEvel(level + 1)) {
            this.experience -= calculateExperienceToReachLEvel(level + 1);
            level++;
        }
    }

    public int getLevel() {
        return level;
    }

    public SkillType getSkill() {
        return skill;
    }

    public float getExperience() {
        return experience;
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getProgress() {
        return Math.min(this.experience / calculateExperienceToReachLEvel(level + 1), 1);
    }

    public String getShorthandExperienceRoof(int level) {
        float experience = calculateExperienceToReachLEvel(level);
        StringBuilder sb = new StringBuilder();
        if (experience >= 1000000) {
            sb.append((int) (experience / 1000000)).append("M");
        } else if (experience >= 1000) {
            sb.append((int) (experience / 1000)).append("K");
        } else {
            sb.append((int) experience);
        }
        return sb.toString();
    }
    public static float calculateExperienceToReachLEvel(int level) {
        Map<Integer, Integer> levelToExperience = new HashMap<>();
        levelToExperience.put(1,	50);
        levelToExperience.put(2,	125);
        levelToExperience.put(3,	200);
        levelToExperience.put(4,	300);
        levelToExperience.put(5,	500);
        levelToExperience.put(6,	750);
        levelToExperience.put(7,	1000);
        levelToExperience.put(8,	1500);
        levelToExperience.put(9,	2000);
        levelToExperience.put(10,	3500);
        levelToExperience.put(11,	5000);
        levelToExperience.put(12,	7500);
        levelToExperience.put(13,	10000);
        levelToExperience.put(14,	15000);
        levelToExperience.put(15,	20000);
        levelToExperience.put(16,	30000);
        levelToExperience.put(17,	50000);
        levelToExperience.put(18,	75000);
        levelToExperience.put(19,	100000);
        levelToExperience.put(20,	200000);
        levelToExperience.put(21,	300000);
        levelToExperience.put(22,	400000);
        levelToExperience.put(23,	500000);
        levelToExperience.put(24,	600000);
        levelToExperience.put(25,	700000);
        levelToExperience.put(26,	800000);
        levelToExperience.put(27,	900000);
        levelToExperience.put(28,	1000000);
        levelToExperience.put(29,	1100000);
        levelToExperience.put(30,	1200000);
        levelToExperience.put(31,	1300000);
        levelToExperience.put(32,	1400000);
        levelToExperience.put(33,	1500000);
        levelToExperience.put(34,	1600000);
        levelToExperience.put(35,	1700000);
        levelToExperience.put(36,	1800000);
        levelToExperience.put(37,	1900000);
        levelToExperience.put(38,	2000000);
        levelToExperience.put(39,	2100000);
        levelToExperience.put(40,	2200000);
        levelToExperience.put(41,	2300000);
        levelToExperience.put(42,	2400000);
        levelToExperience.put(43,	2500000);
        levelToExperience.put(44,	2600000);
        levelToExperience.put(45,	2750000);
        levelToExperience.put(46,	2900000);
        levelToExperience.put(47,	3100000);
        levelToExperience.put(48,	3400000);
        levelToExperience.put(49,	3700000);
        levelToExperience.put(50,	4000000);
        levelToExperience.put(51,	4300000);
        levelToExperience.put(52,	4600000);
        levelToExperience.put(53,	4900000);
        levelToExperience.put(54,	5200000);
        levelToExperience.put(55,	5500000);
        levelToExperience.put(56,	5800000);
        levelToExperience.put(57,	6100000);
        levelToExperience.put(58,	6400000);
        levelToExperience.put(59,	6700000);
        levelToExperience.put(60,	7000000);

        return levelToExperience.getOrDefault(level, 0);
    }
}
