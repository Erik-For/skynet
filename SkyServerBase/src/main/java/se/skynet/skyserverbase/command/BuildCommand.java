package se.skynet.skyserverbase.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

import java.util.List;

public class BuildCommand extends Command {

    public BuildCommand(SkyServerBase plugin) {
        super(plugin, Rank.ADMIN);
    }

    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        if(getPlugin().getPlayerDataManager().getPlayerData(player.getUniqueId()).getPermissonAttachment().getPermissions().containsKey("skynet.build")){
            playerData.getPermissonAttachment().unsetPermission("skynet.build");
            player.sendMessage(ChatColor.RED + "Build mode disabled");
        } else {
            playerData.getPermissonAttachment().setPermission("skynet.build", true);
            player.sendMessage(ChatColor.GREEN + "Build mode enabled");
        }
        return false;
    }

    @Override
    protected List<String> tabComplete(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        return null;
    }
}
