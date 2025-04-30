package se.skynet.skyblock;

import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.playerdata.PlayerProfile;
import se.skynet.skyblock.playerdata.SkillHelper;
import se.skynet.skyblock.playerdata.SkillType;
import se.skynet.skyblock.playerdata.Stat;
import se.skynet.skyserverbase.util.ScoreboardHelper;

import java.util.*;

public class SkyblockPlayer {

    private Player player;

    private PlayerProfile profile;
    private Map<Stat, Double> stats = new HashMap<>();

    private ArmorSet armor = new ArmorSet();

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

    public double calculateStatMax(Stat stat) {
        double amount = 0;
        switch(stat) {
            case HEALTH:
                amount = 100;
                amount += SkillHelper.calculateHealthBonusFromFarmingLevel(profile.getSkill(SkillType.FARMING).getLevel());
                break;
            case DEFENSE:
                amount += SkillHelper.calculateDefenseBonusFromMiningLevel(profile.getSkill(SkillType.MINING).getLevel());
                break;
            case STRENGTH:
                amount += SkillHelper.calculateStrengthBonusFromCombatLevel(profile.getSkill(SkillType.FORAGING).getLevel());
                break;
            case CRIT_DAMAGE:
                amount = 50;
                break;
            case CRIT_CHANCE:
                amount = 30;
                amount += SkillHelper.calculateCritChanceBonusFromCombatLevel(profile.getSkill(SkillType.COMBAT).getLevel());
                break;
            case SPEED:
                amount = 100;
                break;
            case INTELLIGENCE:
                amount = 200;
                break;
            case DAMAGE:
                return 0;
            default:
                return 0;
        }

        amount += armor.getStat(stat);
        SkyblockItem heldItem = getHeldItem();
        if(heldItem != null) {
            amount += heldItem.getAttribute(stat);
        }

        for (ItemStack content : player.getInventory().getContents()) {
            if(content == player.getInventory().getItemInHand()) continue;
            if(content == null || content.getType() == Material.AIR) continue;
            if(!SkyblockItem.isSkyblockItem(content)) continue;
            SkyblockItemID id = SkyblockItem.getItemID(content);
            if(id == null) continue;
            SkyblockItem item = SkyblockItem.constructSkyblockItem(id.getItemClass(), content);
            if(item == null) continue;
            if(item.getType() == SkyblockItemType.TAILSMAN) {
                amount += item.getAttribute(stat);
            }
        }
        if(ArmorSets.SuperiorDragonArmor.isFullSet(armor)) {
            amount = amount * 1.05;
        }
        return amount;
    }

    public boolean shouldCrit(){
        double critChance = calculateStatMax(Stat.CRIT_CHANCE);
        return new Random().nextDouble() < (critChance / 100);
    }
    public double calculateDamage(boolean critical) {
        double weapon_dmg = getHeldItem().getAttribute(Stat.DAMAGE);
        double strength = calculateStatMax(Stat.STRENGTH);
        double crit_damage = calculateStatMax(Stat.CRIT_DAMAGE);
        double addativeMultiplier = 1 + SkillHelper.calculateDamageMultiplyerFromCombatLevel(profile.getSkill(SkillType.COMBAT).getLevel());
        double multiplicativeMultiplier = 1;
        double bonusModifier = 0;
        if(!critical) {
            crit_damage = 0;
        }

        return ((5+weapon_dmg) * (1 + strength / 100) * (addativeMultiplier) * multiplicativeMultiplier + bonusModifier) * (1 + crit_damage / 100);
    }
    public Double getStat(Stat stat) {
        return stats.get(stat);
    }

    public void setStat(Stat stat, double value) {
        stats.put(stat, value);
    }

    public void addToStat(Stat stat, double value) {
        stats.put(stat, stats.get(stat) + value);
    }

    public SkyblockItem getHeldItem() {
        if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            return null;
        }
        if(SkyblockItem.isSkyblockItem(player.getItemInHand())) {
            return SkyblockItem.constructSkyblockItem(SkyblockItem.getItemID(player.getItemInHand()).getItemClass(), player.getItemInHand());
        } else {
            return null;
        }
    }

    public boolean removeFromStat(Stat stat, double value) {
        double newValue = stats.get(stat) - value;
        if(newValue < 0) {
            return false;
        }
        stats.put(stat, newValue);
        return true;
    }
    public void addToStat(Stat stat, double value, double max) {
        double newValue = stats.get(stat) + value;
        stats.put(stat, Math.min(newValue, max));
    }
    public boolean isInDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void damage(double damage, boolean trueDamage) {
        double defense = calculateStatMax(Stat.DEFENSE);
        double maxHealth = calculateStatMax(Stat.HEALTH);

        double effectiveDamage = trueDamage ? damage : damage * (1 - (defense / (defense + 100)));
        double health = getStat(Stat.HEALTH);
        if(health - effectiveDamage <= 0) {
            handleDeath();
            return;
        }
        addToStat(Stat.HEALTH, -Math.round(effectiveDamage));
        player.setHealth(Math.max(1, Math.min(20, 20 * health / maxHealth)));
    }

    private void handleDeath() {
        Double coins = getProfile().getCoins();
        getProfile().setCoins(coins / 2);
        Location spawn = Skyblock.getInstance().getParentPlugin().getWorldConfigManager().getSpawn();

        player.sendMessage(ChatColor.RED + "You died! You lost " + Util.formatNumberWithCommas(coins / 2) + " coins!");
        player.teleport(spawn);
        setStat(Stat.HEALTH, calculateStatMax(Stat.HEALTH));
        player.playSound(player.getLocation(), "entity.player.hurt", 1, 1);
    }

    public void tick() {
        // Update player gear
        Arrays.stream(player.getInventory().getArmorContents()).filter(item -> item.getType() != Material.AIR).filter(item -> SkyblockItem.isSkyblockItem(item)).forEach(item -> {
            SkyblockItemID id = SkyblockItem.getItemID(item);
            SkyblockItem skyblockItem = SkyblockItem.constructSkyblockItem(id.getItemClass(), item);
            switch(skyblockItem.getType()) {
                case HELMET:
                    armor.setHead(skyblockItem);
                    break;
                case CHESTPLATE:
                    armor.setChest(skyblockItem);
                    break;
                case LEGGINGS:
                    armor.setLegs(skyblockItem);
                    break;
                case BOOTS:
                    armor.setBoots(skyblockItem);
                    break;
                default:
                    break;
            }
        });



        // Update visuals for the player

        // Action bar
        double hp = getStat(Stat.HEALTH);

        int hp_int = (int) hp;
        int mana_int = (int) ((double) getStat(Stat.INTELLIGENCE));

        double max_hp = calculateStatMax(Stat.HEALTH);
        double max_defense = calculateStatMax(Stat.DEFENSE);
        double max_mana = calculateStatMax(Stat.INTELLIGENCE);

        int max_hp_int = (int) max_hp;
        int max_defense_int = (int) max_defense;
        int max_mana_int = (int) max_mana;

        ScoreboardHelper.sendActionBar(player.getPlayer(), ChatColor.RED + "" + hp_int + "/" + max_hp_int + "❤          " +
                ChatColor.GREEN + max_defense_int + "✦           " +
                ChatColor.AQUA + mana_int + "/" + max_mana_int + "✎");

        // Hearts in hotbar
        player.setHealth(Math.max(1, Math.min(20, 20 * hp / max_hp)));

        // Player stat regeneration
        addToStat(Stat.HEALTH, max_hp / 30, max_hp);
        addToStat(Stat.INTELLIGENCE, max_hp / 30, max_mana);

    }
}
