package se.skynet.skyserverbase.playerdata;

import se.skynet.skyserverbase.Rank;

public class Nick {

    private final String nickname;
    private final Rank nickRank;
    private final String signature;
    private final String texture;

    public Nick(String nickname, Rank nickRank, String signature, String texture){
        this.nickname = nickname;
        this.nickRank = nickRank;
        this.signature = signature;
        this.texture = texture;
    }

    public String getNickname() {
        return nickname;
    }

    public Rank getNickRank() {
        return nickRank;
    }

    public String getSignature() {
        return signature;
    }

    public String getTexture() {
        return texture;
    }

    public boolean hasSkin(){
        return signature != null && texture != null;
    }
}
