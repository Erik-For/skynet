package se.skynet.skynetproxy.command;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.debug.PasteBin;
import se.skynet.skynetproxy.debug.PlayerJsonSerializer;
import se.skynet.skynetproxy.debug.PlayerListJsonSerializer;
import se.skynet.skynetproxy.party.Party;
import se.skynet.skynetproxy.party.PartyChatFormatting;
import se.skynet.skynetproxy.party.PartyInvite;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PartyCommand extends SkynetCommand {


    private final SkyProxy plugin;

    public PartyCommand(SkyProxy plugin) {
        super("party", Rank.DEFAULT, plugin, Collections.singletonList("p"));
        this.plugin = plugin;
    }

    /*

    /party invite <player> this create a party and invites a player

     */
    @Override
    public void executeCommand(ProxiedPlayer player, Rank rank, String[] args) {
        CustomPlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage("Format err");
            return;
        }
        if (args[0].equals("join")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /party join <player>");
                return;
            }
            UUID inviteId = UUID.fromString(args[1]);
            System.out.println("Invite id: " + args[1]);
            System.out.println("Invite id: " + inviteId.toString());
            PartyInvite invite = plugin.getPartyManager().getInvite(inviteId);
            if (invite == null) {
                PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatNotInvited());
                System.out.println("Invite is null");
                return;
            }
            Party party = invite.acceptInvite(player);
            if (party == null) {
                PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatNotInvited());
                System.out.println("Party is null");
                return;
            }
            ProxiedPlayer owner = party.getLeader();
            CustomPlayerData ownerData = plugin.getPlayerDataManager().getPlayerData(owner.getUniqueId());

            PartyChatFormatting.sendMessage(party.getPlayers(), PartyChatFormatting.formatJoinMessageToCurrent(player, playerData));
            PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatJoinMessageToJoiner(owner, ownerData));
            party.addPlayer(player);
        } else if (args[0].equalsIgnoreCase("invite")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /party invite <player>");
                return;
            }
            ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]);
            CustomPlayerData targetPlayerData = plugin.getPlayerDataManager().getPlayerData(target.getUniqueId());
            if (target == null) {
                PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatNoPlayerFoundMessageToInviter());
                return;
            }
            Party party;
            if (plugin.getPartyManager().isInParty(player)) {
                party = plugin.getPartyManager().getParty(player);
            } else {
                party = plugin.getPartyManager().createParty(player);
            }
            PartyInvite partyInvite = new PartyInvite(party, target, player, plugin);
            PartyChatFormatting.sendMessage(target, PartyChatFormatting.formatInvitedMessageToInvited(target, targetPlayerData, partyInvite.getId()));
            PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatInvitedMessageToParty(target, targetPlayerData, player, playerData));
            plugin.getPartyManager().addInvite(partyInvite);
        } else if (args[0].equalsIgnoreCase("list")) {
            if (!plugin.getPartyManager().isInParty(player)) {
                player.sendMessage("You are not in a party");
                return;
            }
            Party party = plugin.getPartyManager().getParty(player);
            List<Pair<ProxiedPlayer, CustomPlayerData>> members = new ArrayList<>();
            for (ProxiedPlayer member : party.getMembers()) {
                CustomPlayerData data = plugin.getPlayerDataManager().getPlayerData(member.getUniqueId());
                members.add(new ImmutablePair<>(member, data));
            }
            PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatPartyListMessage(members, party.getLeader(), plugin.getPlayerDataManager().getPlayerData(party.getLeader().getUniqueId())));
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (!plugin.getPartyManager().isInParty(player)) {
                player.sendMessage("You are not in a party");
                return;
            }
            Party party = plugin.getPartyManager().getParty(player);
            if (party.getLeader().equals(player)) {
                PartyChatFormatting.sendMessage(party.getPlayers(), PartyChatFormatting.formatPartyDisbandMessageToParty(player, playerData));
                party.disband();
            } else {
                party.removePlayer(player);
            }
        } else if(args[0].equalsIgnoreCase("warp")) {
            if (!plugin.getPartyManager().isInParty(player)) {
                player.sendMessage("You are not in a party");
                return;
            }
            Party party = plugin.getPartyManager().getParty(player);
            if (!party.getLeader().equals(player)) {
                player.sendMessage("You are not the leader of the party");
                return;
            }
            Server server = player.getServer();
            ServerInfo info = server.getInfo();
            party.getMembers().stream().filter(member -> !member.equals(player)).forEach(member -> member.connect(info));
        } else if(args[0].equalsIgnoreCase("disband")) {
            if (!plugin.getPartyManager().isInParty(player)) {
                player.sendMessage("You are not in a party");
                return;
            }
            Party party = plugin.getPartyManager().getParty(player);
            if (!party.getLeader().equals(player)) {
                player.sendMessage("You are not the leader of the party");
                return;
            }
            PartyChatFormatting.sendMessage(party.getPlayers(), PartyChatFormatting.formatPartyDisbandMessageToParty(player, playerData));
            party.disband();
        } else if(args[0].equalsIgnoreCase("kick")) {
            if (!plugin.getPartyManager().isInParty(player)) {
                player.sendMessage("You are not in a party");
                return;
            }
            Party party = plugin.getPartyManager().getParty(player);
            if (!party.getLeader().equals(player)) {
                player.sendMessage("You are not the leader of the party");
                return;
            }
            if (args.length < 2) {
                player.sendMessage("Usage: /party kick <player>");
                return;
            }
            ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]);
            if (target == null) {
                player.sendMessage("Player not found");
                return;
            }
            if (!party.getPlayers().contains(target)) {
                player.sendMessage("Player is not in the party");
                return;
            }
            party.removePlayer(target);
            CustomPlayerData targetData = plugin.getPlayerDataManager().getPlayerData(target.getUniqueId());
            PartyChatFormatting.sendMessage(party.getPlayers(), PartyChatFormatting.formatKickMessageToRemaining(target, targetData));
            PartyChatFormatting.sendMessage(target, PartyChatFormatting.formatKickMessageToKicked(player, playerData));
        } else if(args[0].equalsIgnoreCase("debug")){
            Type merchantListType = new TypeToken<List<ProxiedPlayer>>() {}.getType();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(ProxiedPlayer.class, new PlayerJsonSerializer())
                    .registerTypeAdapter(merchantListType, new PlayerListJsonSerializer())
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            if(!rank.hasPriorityHigherThanOrEqual(Rank.MANAGEMENT)){
                player.sendMessage("You do not have permission to use this command");
                return;
            }
            if(args.length < 2){
                player.sendMessage("Usage: /party debug <global | party>");
                return;
            }
            if(args[1].equalsIgnoreCase("global")){
                String json = gson.toJson(plugin.getPartyManager());
                System.out.println("\n" + json);
                player.sendMessage("Check logs");
            } else if(args[1].equalsIgnoreCase("party")){
                if(!plugin.getPartyManager().isInParty(player)){
                    player.sendMessage("You are not in a party");
                    return;
                }
                Party party = plugin.getPartyManager().getParty(player);
                String json = gson.toJson(party, Party.class);
                System.out.println("\n" + json);
                player.sendMessage("Check logs");
            } else {
                player.sendMessage("Usage: /party debug <global | party>");
            }

        } else {
            player.sendMessage("Usage: /party <invite|join|list|leave>");
        }
    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        return Collections.emptyList();
    }
}
