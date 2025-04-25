package se.skynet.skyblock;

import org.bukkit.entity.Player;
import se.skynet.skyblock.playerdata.PlayerProfile;
import se.skynet.skyblock.playerdata.SkillHelper;
import se.skynet.skyblock.playerdata.SkillType;
import se.skynet.skyblock.playerdata.Stat;

import java.util.HashMap;
import java.util.Map;

public class SkyblockPlayer {

    private Player player;

    private PlayerProfile profile;
    private Map<Stat, Integer> stats = new HashMap<>();

    private boolean devMode = false;

    public SkyblockPlayer(Player player, PlayerProfile profile) {
        this.player = player;
        this.profile = profile;
        for(Stat stat : Stat.values()) {
            stats.put(stat, calculateStatMax(stat));
        }
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public Integer calculateStatMax(Stat stat) {
        int amount = 0;
        switch(stat) {
            case HEALTH:
                amount = 100;
                amount += SkillHelper.calculateHealthBonusFromFarmingLevel(profile.getSkill(SkillType.FARMING).getLevel());
                return amount;
            case DEFENSE:
                amount += SkillHelper.calculateDefenseBonusFromMiningLevel(profile.getSkill(SkillType.MINING).getLevel());
                return amount;
            case STRENGTH:
                return 0;
            case CRIT_DAMAGE:
                return 50;
            case CRIT_CHANCE:
                return 30;
            case SPEED:
                return 100;
            case INTELLIGENCE:
                return 100;
            default:
                return 0;
        }
    }

    public Integer getStat(Stat stat) {
        return stats.get(stat);
    }

    public boolean isInDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
