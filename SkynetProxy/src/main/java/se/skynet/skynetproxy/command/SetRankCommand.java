package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.database.DatabaseMethods;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class SetRankCommand extends SkynetCommand{

    private final SkyProxy plugin;
    public SetRankCommand(SkyProxy plugin) {
        super("setrank", Rank.MANAGEMENT, plugin);
        this.plugin = plugin;
    }
    @Override
    public void executeCommand(ProxiedPlayer player, Rank rank, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /setrank <player> <rank>");
            return;
        }

        String targetPlayerName = args[0];
        String targetRankName = args[1];

        // Check if the target player is online
        ProxiedPlayer targetPlayer = plugin.getProxy().getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage("Player " + targetPlayerName + " is not online.");
            return;
        }

        // Check if the rank exists
        Rank targetRank = Rank.fromString(targetRankName);
        if (targetRank == null) {
            player.sendMessage("Rank " + targetRankName + " does not exist.");
            return;
        }

        // Set the rank for the target player
        new DatabaseMethods(plugin.getDatabaseConnectionManager()).setRank(targetPlayer.getUniqueId(), targetRank);
        player.sendMessage("Set rank of " + targetPlayerName + " to " + targetRank.getDisplayName());
    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        if (args.length == 1) {
            return plugin.getProxy().getPlayers().stream()
                    .filter(p -> p.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    .map(ProxiedPlayer::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return Arrays.stream(Rank.values())
                    .map(Rank::name)
                    .filter(r -> r.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
