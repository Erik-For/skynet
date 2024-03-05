package se.skynet.skynetproxy.party;

import com.google.gson.annotations.Expose;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Party {
    @Expose(serialize = true, deserialize = false)

    private final String name;
    private final PartyManger partyManger;
    @Expose(serialize = true, deserialize = false)

    private ProxiedPlayer leader;
    @Expose(serialize = true, deserialize = false)

    private final List<ProxiedPlayer> players = new ArrayList<>();
    public Party(ProxiedPlayer leader, PartyManger partyManger) {
        this.name = leader.getName();
        this.leader = leader;
        this.partyManger = partyManger;
        players.add(leader);
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


    protected PartyManger getPartyManager() {
        return partyManger;
    }

    public void removePlayer(ProxiedPlayer player) {
        if(player == leader) {
            disband();
        }

        players.remove(player);
        partyManger.unRegisterPlayerFromMaps(player);
    }

    public void disband() {
        partyManger.disbandParty(this);
    }

    public void addPlayer(ProxiedPlayer player) {
        players.add(player);
        partyManger.registerPlayerToMaps(player, this);
    }
}
