package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.database.DatabaseMethods;

public class ProxyBan extends SkynetCommand {

    private final SkyProxy proxy;
    public BaseComponent[] formatError = new ComponentBuilder("/proxyban <Player>").color(ChatColor.RED).create();

    public ProxyBan(SkyProxy proxy){
        super("proxyban", Rank.MODERATOR, proxy);
        this.proxy = proxy;
    }

    @Override
    public void executeCommand(ProxiedPlayer player, Rank rank, String[] args) {
        if(args.length<1) {
            player.sendMessage(formatError);
            return;
        }
        String username = args[0];
        ProxiedPlayer targetPlayer = proxy.getProxy().getPlayer(username);
        if (targetPlayer == null || !targetPlayer.isConnected()){
            player.sendMessage(ErrorMessages.noPlayer);
            return;
        }
        Rank tagetPlayerRank = proxy.getPlayerDataManager().getPlayerData(targetPlayer.getUniqueId()).getRank();
        if(tagetPlayerRank.hasPriorityHigherThan(rank)){
            player.sendMessage(ErrorMessages.userHasHigherRank);
            return;
        }
        new DatabaseMethods(proxy.getDatabaseConnectionManager()).setBanned(targetPlayer.getUniqueId(), true);
        targetPlayer.disconnect(new ComponentBuilder("You have been banned from the server!").color(ChatColor.RED).create());
    }
}
