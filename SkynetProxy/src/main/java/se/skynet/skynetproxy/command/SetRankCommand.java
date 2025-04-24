package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.database.DatabaseMethods;

import java.util.*;
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

        DatabaseMethods databaseMethods = new DatabaseMethods(plugin.getDatabaseConnectionManager());
        // Check if the target player is online
        ProxiedPlayer targetPlayer = plugin.getProxy().getPlayer(targetPlayerName);
        // Check if the rank exists
        Rank targetRank = Rank.fromString(targetRankName);
        if (targetRank == null) {
            player.sendMessage("Rank " + targetRankName + " does not exist.");
            return;
        }

        UUID targetUUID;
        if (targetPlayer == null) {
            UUID uuid = databaseMethods.getUUID(targetPlayerName);
            if(uuid == null) {
                player.sendMessage("Player " + targetPlayerName + " is not online and has never joined the server.");
                return;
            }
            targetUUID = uuid;
        } else {
            targetUUID = targetPlayer.getUniqueId();
            targetPlayer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Your rank has been changed to " + targetRank.getRankColor() + targetRankName + ChatColor.GREEN + "" + ChatColor.BOLD + " relog to see the changes.");
        }

        // Set the rank for the target player
        databaseMethods.setRank(targetUUID, targetRank);
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You have set " + targetPlayerName + "'s rank to " + targetRank.getRankColor() + targetRankName);
    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        List<String> players = plugin.getProxy().getPlayers().stream()
                .map(ProxiedPlayer::getName)
                .collect(Collectors.toList());
        List<String> playersFiltered = players.stream()
                .filter(p -> p != player.getName()).collect(Collectors.toList());

        if(args.length == 0) {
            return playersFiltered;
        } else if (args.length == 1) {
            return playersFiltered.stream().filter(p -> p.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        } else if (args.length == 2) {
            return Arrays.stream(Rank.values())
                    .map(Rank::name)
                    .filter(r -> r.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
