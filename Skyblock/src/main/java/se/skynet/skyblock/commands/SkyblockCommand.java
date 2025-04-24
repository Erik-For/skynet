package se.skynet.skyblock.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.guis.menu.MainSkyblockMenu;
import se.skynet.skyblock.items.SkyblockItem;
import se.skynet.skyblock.items.SkyblockItemType;
import se.skynet.skyblock.playerdata.SkillProgression;
import se.skynet.skyblock.playerdata.SkillType;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.command.Command;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;
import se.skynet.skyserverbase.util.NBTHelper;

import java.util.List;

public class SkyblockCommand extends Command {
    private final Skyblock plugin;
    public SkyblockCommand(Skyblock plugin) {
        super(plugin.getParentPlugin(), Rank.ADMIN);
        this.plugin = plugin;
    }

    @Override
    protected boolean executeCommand(Player player, CustomPlayerData customPlayerData, Command command, String s, String[] args) {
        if(args.length == 0) {
            player.sendMessage("§cPlease use /skyblock help for a list of commands");
            return true;
        }
        SkyblockPlayer skyblockPlayer = plugin.getPlayerManager().getSkyblockPlayer(player);

        switch(args[0]) {
            case "menu":
                // open the main skyblock menu
                MainSkyblockMenu mainSkyblockMenu = new MainSkyblockMenu(plugin, skyblockPlayer);
                player.openInventory(mainSkyblockMenu.getInventory());
                break;
            case "skill":
                // set skill level and experience

                if(args.length < 2) {
                    player.sendMessage("§cPlease use /skyblock skill <skill | list> <level> <experience>");
                    return true;
                }
                if(args[1].equalsIgnoreCase("list")) {
                    skyblockPlayer
                            .getProfile()
                            .getSkillsAsList()
                            .forEach(skill -> player.sendMessage("§a" + skill.getSkill().getName() + " §7- §aLevel: " + skill.getLevel() + " §7- §aExperience: " + skill.getExperience()));
                    return true;
                } else if(args[1].equalsIgnoreCase("xp_boost")) {
                    for(SkillType skillType : SkillType.values()) {
                        SkillProgression skill = skyblockPlayer.getProfile().getSkill(skillType);
                        // random xp boost between 10 000 and 1 000 000
                        int xpBoost = (int) (Math.random() * (1000000 - 10000 + 1)) + 10000;
                        skill.addExperience(xpBoost);
                    }
                    return true;
                }
                if(args.length < 3) {
                    player.sendMessage("§cPlease use /skyblock skill <skill | list> <level> <experience>");
                    return true;
                }
                SkillType skillType;
                try {
                    skillType = SkillType.valueOf(args[1]);
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§cPlease use a valid skill name");
                    return true;
                }
                int level;
                int experience;
                try {
                    level = Integer.parseInt(args[2]);
                    experience = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cPlease use valid numbers for level and experience");
                    return true;
                }
                SkillProgression skill = skyblockPlayer.getProfile().getSkill(skillType);
                skill.addExperience(experience);
                skill.setLevel(level);

                player.sendMessage("§aSet skill " + skillType.getName() + " to level " + level + " and experience " + experience);
                break;

            case "dev":
                // toggles dev mode
                boolean inDevMode = skyblockPlayer.isInDevMode();
                skyblockPlayer.setDevMode(!inDevMode);
                if(!inDevMode) {
                    player.sendMessage(ChatColor.GREEN +"You are now in dev mode");
                } else {
                    player.sendMessage(ChatColor.RED + "You are now out of dev mode");
                }
                break;
            case "coin":
                // set / add / remove
                if(args.length < 3) {
                    player.sendMessage("§cPlease use /skyblock coin <add | remove> <amount>");
                    return true;
                }
                String action = args[1];
                float amount;
                try {
                    amount = Float.parseFloat(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cPlease use a valid number for amount");
                    return true;
                }
                if(action.equalsIgnoreCase("add")) {
                    skyblockPlayer.getProfile().addCoins(amount);
                    player.sendMessage("§aAdded " + amount + " coins to your account");
                } else if(action.equalsIgnoreCase("remove")) {
                    skyblockPlayer.getProfile().removeCoins(amount);
                    player.sendMessage("§aRemoved " + amount + " coins from your account");
                } else if(action.equalsIgnoreCase("set")) {
                    skyblockPlayer.getProfile().setCoins(amount);
                    player.sendMessage("§aSet your coins to " + amount);

                } else {
                    player.sendMessage("§cPlease use a valid action (set| add | remove)");
                }
                break;
            case "spawn":
                // teleport to spawn
                player.sendMessage("§cThis command is not implemented yet");
                break;
            case "nbt":
                // Dump nbt data of item in hand pretty print
                ItemStack itemInHand = player.getInventory().getItemInHand();
                if(itemInHand == null || itemInHand.getType() == null || itemInHand.getType() == Material.AIR) {
                    player.sendMessage("§cYou are not holding an item");
                    return true;
                }

                String nbtData = NBTHelper.dumpNBT(itemInHand);
                player.sendMessage("§aNBT Data: ");
                String[] lines = nbtData.split("\n");
                for(String line : lines) {
                    player.sendMessage("§7" + line.replace("§", "&"));
                }
                break;
            default:
                player.sendMessage("§cUnknown command. Please use /skyblock help for a list of commands");
        }

        return true;
    }

    @Override
    protected List<String> tabComplete(Player player, CustomPlayerData customPlayerData, Command command, String s, String[] strings) {
        return null;
    }
}
