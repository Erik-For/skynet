package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

public abstract class SkynetCommand extends Command {
    private final Rank minRank;
    private final SkyProxy plugin;
    public SkynetCommand(String command, Rank minRank, SkyProxy plugin){
        super(command);
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
        if(minRank.hasPriorityHigherThanOrEqual(data.getRank())){
            player.sendMessage(ErrorMessages.noPermission);
            return;
        }
        executeCommand(player, data.getRank(), args);
    }

    public abstract void executeCommand(ProxiedPlayer player, Rank rank, String[] args);
}
