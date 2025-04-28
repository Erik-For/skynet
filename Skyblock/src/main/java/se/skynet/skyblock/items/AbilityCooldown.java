package se.skynet.skyblock.items;

public class AbilityCooldown {
    private long lastUsedTime;

    private Ability ability;

    public AbilityCooldown(Ability ability) {
        this.ability = ability;
        this.lastUsedTime = 0;
    }

    public boolean isOnCooldown() {
        return (System.currentTimeMillis() - lastUsedTime) < ability.getCooldown() * 1000L;
    }

    public boolean useAbility() {
        if (isOnCooldown()) {
            return false;
        }
        lastUsedTime = System.currentTimeMillis();
        return true;
    }
    public int getSecondsLeft() {
        long currentTime = System.currentTimeMillis();
        long timeLeft = (lastUsedTime + ability.getCooldown() * 1000L) - currentTime;
        return (int) Math.max(0, timeLeft / 1000);
    }
}
