package se.skynet.skynetproxy.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.party.Party;
import se.skynet.skynetproxy.party.PartyInvite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PartyManger implements Listener {

    private final SkyProxy plugin;
    private final List<Party> partys = new ArrayList<>();
    private final HashMap<UUID, Party> partyMap = new HashMap<>();
    private final HashMap<UUID, PartyInvite> partyInvites = new HashMap<>();

    public PartyManger(SkyProxy plugin) {
        this.plugin = plugin;
    }

    public Party createParty(ProxiedPlayer leader) {
        Party party = new Party(leader, plugin);
        partys.add(party);
        partyMap.put(leader.getUniqueId(), party);
        return party;
    }

    public void disbandParty(ProxiedPlayer leader) {
        Party party = partyMap.get(leader.getUniqueId());
        partys.remove(party);
        partyMap.remove(leader.getUniqueId());
    }

    public Party getParty(ProxiedPlayer player) {
        return partyMap.get(player.getUniqueId());
    }

    public void removePlayerFromParty(ProxiedPlayer player) {
        Party party = partyMap.get(player.getUniqueId());
        party.removePlayer(player);
        partyMap.remove(player.getUniqueId());
    }

    public void addPlayerToParty(ProxiedPlayer player, Party party) {
        partyMap.put(player.getUniqueId(), party);
        party.addPlayer(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(!partyMap.containsKey(player.getUniqueId())) return;
        Party party = partyMap.get(player.getUniqueId());
        party.removePlayer(player);
        partyMap.remove(player.getUniqueId());
    }

    public void removeInvite(UUID uniqueId) {
        partyInvites.remove(uniqueId);
    }

    public void addInvite(PartyInvite partyInvite) {
        partyInvites.put(partyInvite.getId(), partyInvite);
        partyInvite.getParty().invitePlayer(partyInvite);
    }

    public boolean isInParty(ProxiedPlayer target) {
        return partyMap.containsKey(target.getUniqueId());
    }

    public PartyInvite getInvite(UUID inviteId) {
        return partyInvites.get(inviteId);
    }
}
