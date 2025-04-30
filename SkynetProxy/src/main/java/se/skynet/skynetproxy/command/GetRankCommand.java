package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.database.DatabaseMethods;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

import java.util.Collections;
import java.util.UUID;

public class GetRankCommand extends SkynetCommand {


    private final SkyProxy plugin;
    public GetRankCommand(SkyProxy plugin){
        super("getrank", Rank.MODERATOR, plugin);
        this.plugin = plugin;
    }

    @Override
    public void executeCommand(ProxiedPlayer player, Rank rank, String[] args) {
        if(args.length < 1){
            player.sendMessage(new ComponentBuilder("Usage: /getrank <player>").create());
            return;

        }
        String targetPlayerName = args[0];
        DatabaseMethods databaseMethods = new DatabaseMethods(plugin.getDatabaseConnectionManager());
        UUID uuid = databaseMethods.getUUID(targetPlayerName);
        if(uuid == null){
            player.sendMessage(new ComponentBuilder("Player " + targetPlayerName + " has never joined the server.").create());
            return;
        }
        CustomPlayerData playerRank = databaseMethods.getPlayerRank(uuid);
        player.sendMessage(new ComponentBuilder("Player " + targetPlayerName + " has rank " + playerRank.getRank().getRankColor() + playerRank.getRank().getDisplayName()).create());

    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        if(args.length == 0){
            return plugin.getProxy().getPlayers().stream().map(ProxiedPlayer::getName)::iterator;
        } else if(args.length == 1) {
            return plugin.getProxy().getPlayers().stream().filter((proxiedPlayer -> {
                boolean a = proxiedPlayer.getName().toLowerCase().startsWith(args[0].toLowerCase());
                return a;
            })).map(ProxiedPlayer::getName)::iterator;
        }
        return Collections.emptyList();
    }
}
