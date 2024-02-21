package se.skynet.skyserverbase.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

public abstract class Command implements CommandExecutor {

    private final SkyServerBase plugin;
    private final Rank minRank;
    public Command(SkyServerBase plugin, Rank minRank) {
        this.plugin = plugin;
        this.minRank = minRank;
    }
    protected abstract boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings);

    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player player = (Player) commandSender;
        CustomPlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (playerData.getRank().hasPriorityHigherThanOrEqual(minRank)) {
            return executeCommand(player, playerData,this, s, strings);
        }
        player.sendMessage("§cYou do not have permission to use this command.");
        return false;
    }


    public SkyServerBase getPlugin() {
        return plugin;
    }
    public Rank getMinRank() {
        return minRank;
    }

}
