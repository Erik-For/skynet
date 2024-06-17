package se.skynet.skynetproxy.party;

import com.google.gson.annotations.Expose;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Party {
    @Expose(serialize = true, deserialize = false)
    private final String name;
    private final PartyManager partyManager;
    @Expose(serialize = true, deserialize = false)
    private ProxiedPlayer leader;
    @Expose(serialize = true, deserialize = false)
    private final List<ProxiedPlayer> players = new ArrayList<>();

    public Party(ProxiedPlayer leader, PartyManager partyManager) {
        this.name = leader.getName();
        this.leader = leader;
        this.partyManager = partyManager;
        players.add(leader);
    }

    public ProxiedPlayer getLeader() {
        return leader;
    }

    public List<ProxiedPlayer> getPlayers() {
        return players;
    }

    public List<ProxiedPlayer> getMembers() {
        return players.stream()
                .filter(player -> !player.equals(leader))
                .collect(Collectors.toList());
    }

    protected PartyManager getPartyManager() {
        return partyManager;
    }

    public void removePlayer(ProxiedPlayer player) {
        if (player == leader) {
            disband();
        }
        players.remove(player);
        partyManager.unregisterPlayerFromMaps(player);
    }

    public void disband() {
        partyManager.disbandParty(this);
    }

    public void addPlayer(ProxiedPlayer player) {
        players.add(player);
        partyManager.registerPlayerToMaps(player, this);
    }
}