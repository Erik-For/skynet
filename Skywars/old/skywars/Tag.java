package se.skynet.skywars;

import org.bukkit.entity.Player;

import java.math.BigInteger;

public class Tag {

    private BigInteger tagTime;
    private Player tagger;

    public Tag(Player tagger) {
        this.tagTime = BigInteger.valueOf(System.currentTimeMillis());
        this.tagger = tagger;
    }

    public Player getTagger() {
        return tagger;
    }

    public boolean isVaid() {
        return tagTime.add(BigInteger.valueOf(10000)).compareTo(BigInteger.valueOf(System.currentTimeMillis())) > 0;
    }
}
