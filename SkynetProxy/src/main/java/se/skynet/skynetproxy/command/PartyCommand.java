package se.skynet.skynetproxy.command;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.debug.PlayerJsonSerializer;
import se.skynet.skynetproxy.debug.PlayerListJsonSerializer;
import se.skynet.skynetproxy.party.Party;
import se.skynet.skynetproxy.party.PartyChatFormatting;
import se.skynet.skynetproxy.party.PartyInvite;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class PartyCommand extends SkynetCommand {
    private final SkyProxy plugin;

    private final String usage = ChatColor.RED + "Usage: /party <invite|join|list|leave>";

    public PartyCommand(SkyProxy plugin) {
        super("party", Rank.DEFAULT, plugin, Collections.singletonList("p"));
        this.plugin = plugin;
    }

    @Override
    public void executeCommand(ProxiedPlayer player, Rank rank, String[] args) {
        CustomPlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());


        if (args.length == 0) {
            player.sendMessage(usage);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                handleJoinCommand(player, args);
                break;
            case "invite":
                handleInviteCommand(player, playerData, args);
                break;
            case "list":
                handleListCommand(player);                player.sendMessage(ChatColor.RED + "Usage: /party <invite|join|list|leave>");

                break;
            case "leave":
                handleLeaveCommand(player, playerData);
                break;
            case "warp":
                handleWarpCommand(player);
                break;
            case "disband":
                handleDisbandCommand(player, playerData);
                break;
            case "kick":
                handleKickCommand(player, playerData, args);
                break;
            case "debug":
                handleDebugCommand(player, rank, args);
                break;
            default:
                player.sendMessage(usage);
                break;
        }
    }

    private void handleJoinCommand(ProxiedPlayer player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /party join <player>");
            return;
        }
        if(plugin.getPartyManager().isInParty(player)){
            player.sendMessage(ChatColor.RED + "You are already in a party");
            return;
        }
        UUID inviteId = UUID.fromString(args[1]);
        Optional<PartyInvite> invite = plugin.getPartyManager().getInvite(inviteId);

        if (invite.isEmpty()) {
            PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatNotInvited());
            return;
        }

        Optional<Party> party = invite.get().acceptInvite(player);

        if (party.isEmpty()) {
            PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatNotInvited());
            return;
        }

        ProxiedPlayer owner = party.get().getLeader();
        CustomPlayerData ownerData = plugin.getPlayerDataManager().getPlayerData(owner.getUniqueId());
        CustomPlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

        PartyChatFormatting.sendMessage(party.get().getPlayers(), PartyChatFormatting.formatJoinMessageToCurrent(player, playerData));
        PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatJoinMessageToJoiner(owner, ownerData));
        party.get().addPlayer(player);
    }

    private void handleInviteCommand(ProxiedPlayer player, CustomPlayerData playerData, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /party invite <player>");
            return;
        }

        ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]);

        if (target == null) {
            PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatNoPlayerFoundMessageToInviter());
            return;
        }
        CustomPlayerData targetPlayerData = plugin.getPlayerDataManager().getPlayerData(target.getUniqueId());

        Optional<Party> party = plugin.getPartyManager().getParty(player);
        Party partyInstance = party.orElseGet(() -> plugin.getPartyManager().createParty(player));

        PartyInvite partyInvite = new PartyInvite(partyInstance, target, player, plugin);
        PartyChatFormatting.sendMessage(target, PartyChatFormatting.formatInvitedMessageToInvited(target, targetPlayerData, partyInvite.getId()));
        PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatInvitedMessageToParty(target, targetPlayerData, player, playerData));
        plugin.getPartyManager().addInvite(partyInvite);
    }

    private void handleListCommand(ProxiedPlayer player) {
        Optional<Party> party = plugin.getPartyManager().getParty(player);

        if (party.isEmpty()) {
            player.sendMessage(ChatColor.RED + "You are not in a party");
            return;
        }

        List<Pair<ProxiedPlayer, CustomPlayerData>> members = party.get().getMembers().stream()
                .map(member -> new ImmutablePair<>(member, plugin.getPlayerDataManager().getPlayerData(member.getUniqueId())))
                .collect(Collectors.toList());

        ProxiedPlayer leader = party.get().getLeader();
        CustomPlayerData leaderData = plugin.getPlayerDataManager().getPlayerData(leader.getUniqueId());

        PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatPartyListMessage(members, leader, leaderData));
    }

    private void handleLeaveCommand(ProxiedPlayer player, CustomPlayerData playerData) {
        Optional<Party> party = plugin.getPartyManager().getParty(player);

        if (party.isEmpty()) {
            player.sendMessage(ChatColor.RED + "You are not in a party");
            return;
        }

        if (party.get().getLeader().equals(player)) {
            PartyChatFormatting.sendMessage(party.get().getPlayers(), PartyChatFormatting.formatPartyDisbandMessageToParty(player, playerData));
            party.get().disband();
        } else {
            party.get().removePlayer(player);
        }
    }

    private void handleWarpCommand(ProxiedPlayer player) {
        Optional<Party> party = plugin.getPartyManager().getParty(player);

        if (party.isEmpty()) {
            player.sendMessage(ChatColor.RED + "You are not in a party");
            return;
        }

        if (!party.get().getLeader().equals(player)) {
            player.sendMessage(ChatColor.RED + "You are not the leader of the party");
            return;
        }

        Server server = player.getServer();
        ServerInfo info = server.getInfo();
        party.get().getMembers().stream().filter(member -> !member.equals(player)).forEach(member -> member.connect(info));
    }

    private void handleDisbandCommand(ProxiedPlayer player, CustomPlayerData playerData) {
        Optional<Party> party = plugin.getPartyManager().getParty(player);

        if (party.isEmpty()) {
            player.sendMessage(ChatColor.RED + "You are not in a party");
            return;
        }

        if (!party.get().getLeader().equals(player)) {
            player.sendMessage(ChatColor.RED + "You are not the leader of the party");
            return;
        }

        PartyChatFormatting.sendMessage(party.get().getPlayers(), PartyChatFormatting.formatPartyDisbandMessageToParty(player, playerData));
        party.get().disband();
    }

    private void handleKickCommand(ProxiedPlayer player, CustomPlayerData playerData, String[] args) {
        Optional<Party> party = plugin.getPartyManager().getParty(player);

        if (party.isEmpty()) {
            player.sendMessage(ChatColor.RED + "You are not in a party");
            return;
        }

        if (!party.get().getLeader().equals(player)) {
            player.sendMessage(ChatColor.RED + "You are not the leader of the party");
            return;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /party kick <player>");
            return;
        }

        ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found");
            return;
        }

        if (!party.get().getPlayers().contains(target)) {
            player.sendMessage(ChatColor.RED + "Player is not in the party");
            return;
        }

        party.get().removePlayer(target);
        CustomPlayerData targetData = plugin.getPlayerDataManager().getPlayerData(target.getUniqueId());
        PartyChatFormatting.sendMessage(party.get().getPlayers(), PartyChatFormatting.formatKickMessageToRemaining(target, targetData));
        PartyChatFormatting.sendMessage(target, PartyChatFormatting.formatKickMessageToKicked(player, playerData));
    }

    private void handleDebugCommand(ProxiedPlayer player, Rank rank, String[] args) {
        Type merchantListType = new TypeToken<List<ProxiedPlayer>>() {}.getType();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ProxiedPlayer.class, new PlayerJsonSerializer())
                .registerTypeAdapter(merchantListType, new PlayerListJsonSerializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        if (!rank.hasPriorityHigherThanOrEqual(Rank.MANAGEMENT)) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /party debug <global | party>");
            return;
        }

        if (args[1].equalsIgnoreCase("global")) {
            String json = gson.toJson(plugin.getPartyManager());
            System.out.println("\n" + json);
            player.sendMessage(ChatColor.AQUA + "Check logs");
        } else if (args[1].equalsIgnoreCase("party")) {
            Optional<Party> party = plugin.getPartyManager().getParty(player);

            if (party.isEmpty()) {
                player.sendMessage(ChatColor.RED + "You are not in a party");
                return;
            }

            String json = gson.toJson(party.get(), Party.class);
            System.out.println("\n" + json);
            player.sendMessage(ChatColor.AQUA + "Check logs");
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /party debug <global | party>");
        }
    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        return Collections.emptyList();
    }
}