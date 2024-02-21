package se.skynet.skynetproxy.playerdata;

import se.skynet.skynetproxy.Rank;

public class CustomPlayerData {

    private final Rank rank;

    public CustomPlayerData(Rank rank) {
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

}
