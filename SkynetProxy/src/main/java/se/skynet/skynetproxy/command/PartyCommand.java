package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.party.Party;
import se.skynet.skynetproxy.party.PartyChatFormatting;
import se.skynet.skynetproxy.party.PartyInvite;

import java.util.Arrays;
import java.util.Collections;
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
        if(args.length == 0){
            player.sendMessage("Format err");
            return;
        }
        if (args[0].equals("join") ) {
            if (args.length < 2) {
                player.sendMessage("Usage: /party join <player>");
                return;
            }
            UUID inviteId = UUID.fromString(args[1]);

            PartyInvite invite = plugin.getPartyManager().getInvite(inviteId);
            if(invite == null){
                PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatNotInvited());
                return;
            }
            Party party = invite.acceptInvite(player);
            if(party == null){
                PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatNotInvited());
                return;
            }

            plugin.getPartyManager().addPlayerToParty(player, party);

        } else if (args[0].equalsIgnoreCase("invite")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /party invite <player>");
                return;
            }
            ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]);
            if (target == null) {
                PartyChatFormatting.sendMessage(player, PartyChatFormatting.formatNoPlayerFoundMessageToInviter());
                return;
            }
            Party party;
            if (plugin.getPartyManager().isInParty(player)) {
                party = plugin.getPartyManager().getParty(target);
                return;
            } else {
                party = plugin.getPartyManager().createParty(player);
            }
            party.invitePlayer(player, target);
        }
    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        return null;
    }
}
