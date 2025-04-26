package se.skynet.skyblock.playerdata;

public class SkillHelper {

    public static double calculateHealthBonusFromFarmingLevel(int farmingLevel) {
        // level 1-14 gives 2, 15-19 gives 3, 20-25 gives 4, 26-60 gives 5
        int[] healthBonusTeirs = new int[]{14, 5, 6, 35};
        double[] healthBonus = new double[]{2, 3, 4, 5};

        return calculateSkillBonus(farmingLevel, healthBonusTeirs, healthBonus);
    }

    public static double calculateCritChanceBonusFromCombatLevel(int combatLevel) {
        // level 1-14 gives 2, 15-19 gives 3, 20-25 gives 4, 26-60 gives 5
        int[] critChanceBonusTeirs = new int[]{60};
        double[] critChanceBonus = new double[]{0.5};

        return calculateSkillBonus(combatLevel, critChanceBonusTeirs, critChanceBonus);
    }

    public static double calculateDefenseBonusFromMiningLevel(int miningLevel) {
        // level 1-14 gives 2, 15-19 gives 3, 20-25 gives 4, 26-60 gives 5
        int[] defenceBonusTeirs = new int[]{14, 46};
        double[] defenceBonus = new double[]{1, 2};

        return calculateSkillBonus(miningLevel, defenceBonusTeirs, defenceBonus);
    }
    private static double calculateSkillBonus(int skillLevel, int[] skillBonusTeirs, double[] skillBonus) {
        double bonus = 0;
        int usedSkillLevels = 0;
        for (int i = 0; i < skillBonusTeirs.length; i++) {
            int skillLevels = skillBonusTeirs[i];
            int effectiveSkillLevel = skillLevel - usedSkillLevels;
            if (effectiveSkillLevel / skillLevels >= 1) {
                bonus += skillBonus[i] * skillLevels;
                usedSkillLevels += skillLevels;
            } else {
                bonus += (effectiveSkillLevel % skillLevels) * skillBonus[i];
                return bonus;
            }
        }
        return bonus;
    }
}
