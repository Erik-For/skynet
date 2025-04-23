package se.skynet.skyblock.playerdata;


import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Skills {

    private HashMap<SkillType, SkillProgression> skills = new HashMap<>();

    private Skills() {
        for (SkillType skill : SkillType.values()) {
            this.skills.put(skill, new SkillProgression(skill, 0, 1));
        }
    }

    public static Skills create() {
        return new Skills();
    }

    public void addExperience(SkillType skill, int amount) {
        SkillProgression progression = skills.get(skill);
        progression.addExperience(amount);
    }

    public Skills(List<SkillProgression> skills) {
        for (SkillType skill : SkillType.values()) {
            this.skills.put(skill, new SkillProgression(skill, 0, 1));
        }

        skills.forEach(skill -> {
            this.skills.get(skill.getSkill()).setExperience(skill.getExperience());
            this.skills.get(skill.getSkill()).setLevel(skill.getLevel());
        });
    }

    public List<SkillProgression> getSkills() {
        return skills.values().stream().collect(Collectors.toList());
    }

    public SkillProgression getSkill(SkillType skill) {
        return skills.get(skill);
    }
}
