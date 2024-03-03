package se.skynet.skynetproxy.party;

import jdk.internal.net.http.common.Pair;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

import java.util.List;
import java.util.UUID;

public class PartyChatFormatting {

    public static void sendMessage(List<ProxiedPlayer> players, BaseComponent[] message) {
        for (ProxiedPlayer player : players) {
            player.sendMessage(message);
        }
    }

    public static void sendMessage(ProxiedPlayer player, BaseComponent[] message) {
        player.sendMessage(message);
    }
    public static BaseComponent[] formatChatMessage(ProxiedPlayer player, CustomPlayerData playerData, String message) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("Party").color(ChatColor.BLUE).create());
        componentBuilder.append(new ComponentBuilder(" > ").color(ChatColor.DARK_GRAY).create());
        componentBuilder.append(new ComponentBuilder(player.getName()).color(playerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(": ").append(message).color(
                playerData.getRank().hasPriorityHigherThan(Rank.DEFAULT) ? ChatColor.WHITE : ChatColor.GRAY).create());

        return componentBuilder.create();
    }
    public static BaseComponent[] formatJoinMessageToCurrent(ProxiedPlayer player, CustomPlayerData playerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder(player.getName()).color(playerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" has joined the party!").color(ChatColor.YELLOW).create());

        return componentBuilder.create();
    }
    public static BaseComponent[] formatLeaveMessageToRemaining(ProxiedPlayer player, CustomPlayerData playerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder(player.getName()).color(playerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" has left the party!").color(ChatColor.YELLOW).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatJoinMessageToJoiner(ProxiedPlayer owner, CustomPlayerData ownerPlayerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("You have joined ").color(ChatColor.YELLOW).create());
        componentBuilder.append(new ComponentBuilder(owner.getName()).color(ownerPlayerData.getRank().getRankColor()).append("'s").create());
        componentBuilder.append(new ComponentBuilder(" party!").color(ChatColor.YELLOW).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatLeaveMessageToLeaver(ProxiedPlayer owner, CustomPlayerData ownerPlayerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("You left the party ").color(ChatColor.YELLOW).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatKickMessageToKicked(ProxiedPlayer owner, CustomPlayerData ownerPlayerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("You have been removed from the party by ").color(ChatColor.YELLOW).create());
        componentBuilder.append(new ComponentBuilder(ownerPlayerData.getRank().getPrefix()).append(" ").append(owner.getName()).color(ownerPlayerData.getRank().getRankColor()).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatKickMessageToRemaining(ProxiedPlayer player, CustomPlayerData playerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder(playerData.getRank().getPrefix()).append(" ").append(player.getName()).color(playerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" has been removed from the party!").color(ChatColor.YELLOW).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatInvitedMessageToParty(ProxiedPlayer invited, CustomPlayerData invitedPlayerData, ProxiedPlayer inviter, CustomPlayerData inviterPlayerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder(inviterPlayerData.getRank().getPrefix()).append(" ").append(inviter.getName()).color(inviterPlayerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" invited  ").color(ChatColor.YELLOW).create());
        componentBuilder.append(new ComponentBuilder(invitedPlayerData.getRank().getPrefix()).append(" ").append(invited.getName()).color(invitedPlayerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" to the party! They have ").color(ChatColor.YELLOW).create());
        componentBuilder.append(new ComponentBuilder("60").color(ChatColor.RED).create());
        componentBuilder.append(new ComponentBuilder(" seconds to accept!").color(ChatColor.YELLOW).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatInvitedMessageToInvited(ProxiedPlayer inviter, CustomPlayerData inviterPlayerData, UUID partyId) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder(inviterPlayerData.getRank().getPrefix()).append(" ").append(inviter.getName()).color(inviterPlayerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" has invited you to their party!").color(ChatColor.YELLOW).create());
        componentBuilder.append(new ComponentBuilder("\n").create());
        componentBuilder.append(new ComponentBuilder("You have ").color(ChatColor.YELLOW).create());
        componentBuilder.append(new ComponentBuilder("60").color(ChatColor.RED).create());
        componentBuilder.append(new ComponentBuilder(" seconds to accept! ").color(ChatColor.YELLOW).create());
        componentBuilder.append(new ComponentBuilder("Click here to join!").color(ChatColor.GOLD).underlined(true).event(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + partyId.toString())).create());
        System.out.println(partyId);
        return componentBuilder.create();
    }

//    public static BaseComponent[] formatAlreadyInPartyMessageToInviter(ProxiedPlayer invited , CustomPlayerData invitedPlayerData) {
//        ComponentBuilder componentBuilder = new ComponentBuilder();
//        componentBuilder.append(new ComponentBuilder(invitedPlayerData.getRank().getPrefix()).append(" ").append(invited.getName()).color(invitedPlayerData.getRank().getRankColor()).create());
//        componentBuilder.append(new ComponentBuilder(" is already in a party!").color(ChatColor.RED).create());
//
//        return componentBuilder.create();
//    }

    public static BaseComponent[] formatPartyListMessage(List<Pair<ProxiedPlayer, CustomPlayerData>> players, ProxiedPlayer partyLeader, CustomPlayerData partyLeaderData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("Party Leader: ").color(ChatColor.YELLOW).create());
        componentBuilder.append(new ComponentBuilder(partyLeaderData.getRank().getPrefix()).append(" ").append(partyLeader.getName()).color(partyLeaderData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder("\n").create());
        componentBuilder.append(new ComponentBuilder("Party Members: ").color(ChatColor.YELLOW).create());
        for (Pair<ProxiedPlayer, CustomPlayerData> player : players) {
            if(player.first.equals(partyLeader)) continue;
            componentBuilder.append(new ComponentBuilder("\n").append(player.second.getRank().getPrefix()).append(" ").append(player.first.getName()).color(player.second.getRank().getRankColor()).create());
        }
        return componentBuilder.create();
    }

    public static BaseComponent[] formatPartyDisbandMessageToParty(ProxiedPlayer partyLeader, CustomPlayerData partyLeaderData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("The party has been disbanded by ").color(ChatColor.YELLOW).create());
        componentBuilder.append(new ComponentBuilder(partyLeaderData.getRank().getPrefix()).append(" ").append(partyLeader.getName()).color(partyLeaderData.getRank().getRankColor()).create());
        return componentBuilder.create();
    }

    public static BaseComponent[] formatNotAllowed() {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("You are not allowed to do that!").color(ChatColor.RED).create());
        return componentBuilder.create();
    }

    public static BaseComponent[] formatNoPlayerFoundMessageToInviter() {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("Couldn't find a player with that name!").color(ChatColor.RED).create());
        return componentBuilder.create();
    }

    public static BaseComponent[] formatPlayerNotInParty() {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("You are not in a party!").color(ChatColor.RED).create());
        return componentBuilder.create();
    }

    public static BaseComponent[] formatPlayerNotInYourParty() {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("That player is not in your party!").color(ChatColor.RED).create());
        return componentBuilder.create();
    }

    public static BaseComponent[] formatInviteTimeout(ProxiedPlayer inviter, CustomPlayerData inviterPlayerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("Your party invite from ").color(ChatColor.RED).create());
        componentBuilder.append(new ComponentBuilder(inviterPlayerData.getRank().getPrefix()).append(" ").append(inviter.getName()).color(inviterPlayerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" has expired!").color(ChatColor.RED).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatInviteExpirePartyDisband(ProxiedPlayer inviter, CustomPlayerData inviterPlayerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("Your party invite from ").color(ChatColor.RED).create());
        componentBuilder.append(new ComponentBuilder(inviterPlayerData.getRank().getPrefix()).append(" ").append(inviter.getName()).color(inviterPlayerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" has expired due to the party being disbanded").color(ChatColor.RED).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatInviterLeft(ProxiedPlayer inviter, CustomPlayerData inviterPlayerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("Your party invite from ").color(ChatColor.RED).create());
        componentBuilder.append(new ComponentBuilder(inviterPlayerData.getRank().getPrefix()).append(" ").append(inviter.getName()).color(inviterPlayerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" has expired due to the inviter leaving the party").color(ChatColor.RED).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatAlreadyInParty() {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("You are already in a party! Leave it to join another one.").color(ChatColor.RED).create());
        return componentBuilder.create();
    }

    public static BaseComponent[] formatNewLeaderMessageToParty(ProxiedPlayer leader, CustomPlayerData leaderPlayerData) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder(leader.getName()).color(leaderPlayerData.getRank().getRankColor()).create());
        componentBuilder.append(new ComponentBuilder(" is now the party leader!").color(ChatColor.YELLOW).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatNewLeaderMessageToNewLeader() {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("You are now the party leader!").color(ChatColor.YELLOW).create());

        return componentBuilder.create();
    }

    public static BaseComponent[] formatNotInvited() {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(new ComponentBuilder("You are not invited to this party!").color(ChatColor.RED).create());
        return componentBuilder.create();
    }
}
