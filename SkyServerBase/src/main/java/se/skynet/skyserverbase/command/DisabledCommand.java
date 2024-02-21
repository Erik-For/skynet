package se.skynet.skyserverbase.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

public class DisabledCommand extends Command {

    public DisabledCommand(SkyServerBase plugin) {
        super(plugin, Rank.ADMIN);
    }

    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        player.sendMessage(ChatColor.RED + "This command is disabled.");
        return true;
    }
}
