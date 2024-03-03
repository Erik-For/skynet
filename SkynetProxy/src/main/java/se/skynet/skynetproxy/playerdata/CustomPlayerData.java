package se.skynet.skynetproxy.playerdata;

import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.party.Party;

public class CustomPlayerData {

    private final Rank rank;
    private Party party;

    public CustomPlayerData(Rank rank) {
        this.rank = rank;
        this.party = null;
    }

    public Rank getRank() {
        return rank;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

}
