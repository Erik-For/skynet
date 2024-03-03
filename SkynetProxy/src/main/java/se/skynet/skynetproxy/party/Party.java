package se.skynet.skynetproxy.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Party {

    private ProxiedPlayer leader;
    private SkyProxy plugin;
    private final List<ProxiedPlayer> players = new ArrayList<>();
    private final List<PartyInvite> invites = new ArrayList<>();

    public Party(ProxiedPlayer leader, SkyProxy plugin) {
        this.leader = leader;
        this.plugin = plugin;
        players.add(leader);
    }

    public void disband(){
        PartyChatFormatting.sendMessage(players, PartyChatFormatting.formatPartyDisbandMessageToParty(leader, plugin.getPlayerDataManager().getPlayerData(leader.getUniqueId())));
        for (ProxiedPlayer player : players) {
            CustomPlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
            playerData.setParty(null);
            plugin.getPartyManager().removePlayerFromParty(player);
        }
        players.clear();
    }
    public ProxiedPlayer getLeader() {
        return leader;
    }

    public List<ProxiedPlayer> getPlayers() {
        return players;
    }

    public List<ProxiedPlayer> getMembers() {
        return players.stream().filter(proxiedPlayer -> !proxiedPlayer.equals(leader)).collect(Collectors.toList());
    }

    protected void invitePlayer(PartyInvite invite){
        invites.add(invite);
        CustomPlayerData invitedData = plugin.getPlayerDataManager().getPlayerData(invite.getInvited().getUniqueId());
        CustomPlayerData inviterData = plugin.getPlayerDataManager().getPlayerData(invite.getInvited().getUniqueId());

        PartyChatFormatting.sendMessage(players, PartyChatFormatting.formatInvitedMessageToParty(invite.getInvited(), invitedData, invite.getInviter(),inviterData));
        PartyChatFormatting.sendMessage(invite.getInvited(), PartyChatFormatting.formatInvitedMessageToInvited(invite.getInviter(), inviterData, invite.getId()));
    }
    protected void addPlayer(ProxiedPlayer player){
        CustomPlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        playerData.setParty(this);

        PartyChatFormatting.sendMessage(players, PartyChatFormatting.formatJoinMessageToCurrent(player, playerData));
        PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatJoinMessageToJoiner(leader, playerData));

        players.add(player);
    }

    protected void removePlayer(ProxiedPlayer player){
        players.remove(player);

        for (PartyInvite invite : invites) {
            if(invite.getInviter().equals(player)){
                invite.revokeInviterLeft(player);
            }
        }
        CustomPlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        playerData.setParty(null);

        PartyChatFormatting.sendMessage(players, PartyChatFormatting.formatLeaveMessageToRemaining(player, playerData));
        PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatLeaveMessageToLeaver(player, playerData));

        if(leader.equals(player)){
            leader = players.get(0);
            PartyChatFormatting.sendMessage(this.getMembers(), PartyChatFormatting.formatNewLeaderMessageToParty(leader, playerData));
            PartyChatFormatting.sendMessage(leader, PartyChatFormatting.formatNewLeaderMessageToNewLeader());
        }
    }

}
