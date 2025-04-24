package se.skynet.skyserverbase.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

import java.util.List;

public abstract class Command implements CommandExecutor, TabCompleter {

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

    protected abstract List<String> tabComplete(Player player, CustomPlayerData playerData, Command command, String s, String[] strings);
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return null;
        }
        Player player = (Player) commandSender;
        CustomPlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (playerData.getRank().hasPriorityHigherThanOrEqual(minRank)) {
            return tabComplete(player, playerData, this, s, strings);
        }
        return null;
    }

    public SkyServerBase getPlugin() {
        return plugin;
    }
    public Rank getMinRank() {
        return minRank;
    }

    public static void registerCommand(PluginCommand paperCommand, Command command) {
        paperCommand.setTabCompleter(command);
        paperCommand.setExecutor(command);
    }
}
