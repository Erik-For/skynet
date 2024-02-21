package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.database.DatabaseMethods;

import java.util.Collections;
import java.util.UUID;

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
        UUID targetUUID;
        if (targetPlayer == null || !targetPlayer.isConnected()){
            targetUUID = new DatabaseMethods((proxy.getDatabaseConnectionManager())).getUUID(username);
            if(targetUUID == null){
                player.sendMessage(ErrorMessages.noPlayer);
                return;
            }
        } else {
            targetUUID = targetPlayer.getUniqueId();
        }
        Rank tagetPlayerRank = proxy.getPlayerDataManager().getPlayerData(targetPlayer.getUniqueId()).getRank();
        if(tagetPlayerRank.hasPriorityHigherThan(rank)){
            player.sendMessage(ErrorMessages.userHasHigherRank);
            return;
        }
        new DatabaseMethods(proxy.getDatabaseConnectionManager()).setBanned(targetPlayer.getUniqueId(), true);
        targetPlayer.disconnect(new ComponentBuilder("You have been banned from the server!").color(ChatColor.RED).create());
    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        // tabcomplete player names of online players
        if(args.length == 0){
            return proxy.getProxy().getPlayers().stream().map(ProxiedPlayer::getName)::iterator;
        } else if(args.length == 1) {
            return proxy.getProxy().getPlayers().stream().filter((proxiedPlayer -> {
                boolean a = proxiedPlayer.getName().toLowerCase().startsWith(args[0].toLowerCase());
                boolean b = proxy.getPlayerDataManager().getPlayerData(proxiedPlayer.getUniqueId()).getRank().hasPriorityHigherThanOrEqual(rank);
                return a && !b;
            })).map(ProxiedPlayer::getName)::iterator;
        }

        return Collections.emptyList();
    }
}
