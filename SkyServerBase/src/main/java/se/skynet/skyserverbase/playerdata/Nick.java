package se.skynet.skyserverbase.playerdata;

import se.skynet.skyserverbase.Rank;

public class Nick {

    private final String nickname;
    private final Rank nickRank;

    public Nick(String nickname, Rank nickRank){
        this.nickname = nickname;
        this.nickRank = nickRank;
    }

    public String getNickname() {
        return nickname;
    }

    public Rank getNickRank() {
        return nickRank;
    }
}
