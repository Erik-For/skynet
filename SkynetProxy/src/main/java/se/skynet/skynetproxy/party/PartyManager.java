package se.skynet.skynetproxy.party;

import com.google.gson.annotations.Expose;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class PartyManager implements Listener {
    private final SkyProxy plugin;
    @Expose(serialize = true, deserialize = false)
    private final HashMap<UUID, Party> partyMap = new HashMap<>();
    @Expose(serialize = true, deserialize = false)
    private final HashMap<UUID, PartyInvite> partyInvites = new HashMap<>();

    public PartyManager(SkyProxy plugin) {
        this.plugin = plugin;
    }

    public Party createParty(ProxiedPlayer leader) {
        Party party = new Party(leader, this);
        partyMap.put(leader.getUniqueId(), party);
        return party;
    }

    protected void disbandParty(Party party) {
        party.getPlayers().forEach(player -> partyMap.remove(player.getUniqueId()));
        partyMap.values().remove(party);
    }

    public Optional<Party> getParty(ProxiedPlayer player) {
        return Optional.ofNullable(partyMap.get(player.getUniqueId()));
    }

    public boolean isInParty(ProxiedPlayer target) {
        return partyMap.containsKey(target.getUniqueId());
    }

    public void removeInvite(UUID uniqueId) {
        partyInvites.remove(uniqueId);
    }

    public void addInvite(PartyInvite partyInvite) {
        partyInvites.put(partyInvite.getId(), partyInvite);
    }

    public Optional<PartyInvite> getInvite(UUID inviteId) {
        return Optional.ofNullable(partyInvites.get(inviteId));
    }

    protected void registerPlayerToMaps(ProxiedPlayer player, Party party) {
        partyMap.put(player.getUniqueId(), party);
    }

    protected void unregisterPlayerFromMaps(ProxiedPlayer player) {
        partyMap.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        CustomPlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        getParty(player).ifPresent(party -> {
            if (player == party.getLeader()) {
                PartyChatFormatting.sendMessage(party.getMembers(), PartyChatFormatting.formatPartyDisbandMessageToParty(player, playerData));
                party.disband();
            } else {
                party.removePlayer(player);
                PartyChatFormatting.sendMessage(party.getPlayers(), PartyChatFormatting.formatLeaveMessageToRemaining(player, playerData));
            }
        });
    }
}