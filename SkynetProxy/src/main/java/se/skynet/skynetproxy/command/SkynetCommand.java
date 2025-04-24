package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.checkerframework.framework.qual.RequiresQualifier;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class SkynetCommand extends Command implements TabExecutor {
    private final Rank minRank;
    private final SkyProxy plugin;
    public SkynetCommand(String command, Rank minRank, SkyProxy plugin){
        super(command);
        this.minRank = minRank;
        this.plugin = plugin;
    }

    public SkynetCommand(String command, Rank minRank, SkyProxy plugin, List<String> aliases){
        super(command, "", aliases.toArray(new String[0]));
        this.minRank = minRank;
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)){
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

        CustomPlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if(minRank.hasPriorityHigherThan(data.getRank())){
            player.sendMessage(ErrorMessages.noPermission);
            return;
        }
        executeCommand(player, data.getRank(), args);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)){
            return Collections.emptyList();
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        CustomPlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if(minRank.hasPriorityHigherThan(data.getRank())){
            return Collections.emptyList();
        }
        return tabComplete(player, data.getRank(), args);
    }

    public abstract void executeCommand(ProxiedPlayer player, Rank rank, String[] args);
    public abstract Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args);
}
