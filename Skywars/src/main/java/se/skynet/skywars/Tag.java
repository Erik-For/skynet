package se.skynet.skywars;

import java.util.UUID;

public class Tag {

    private final UUID source;
    private final UUID target;
    private final long time;
    private final long expireTime;

    public Tag(UUID source, UUID target, long expireTimeSeconds) {
        this.source = source;
        this.target = target;
        this.time = System.currentTimeMillis();
        this.expireTime = expireTimeSeconds * 1000L;
    }

    public UUID getSource() {
        return source;
    }

    public UUID getTarget() {
        return target;
    }

    public boolean isValid(){
        return System.currentTimeMillis() - time < expireTime;
    }
}