package se.skynet.skyblock.items;

import org.bukkit.event.block.Action;

public enum AbilityAction {
    RIGHT_CLICK("RIGHT CLICK"),
    LEFT_CLICK("LEFT CLICK"),
    SHIFT_RIGHT_CLICK("SNEAK RIGHT CLICK"),
    SHIFT_LEFT_CLICK("SNEAK LEFT CLICK"),;

    private final String action;
    AbilityAction(String action) {
        this.action = action;
    }

    public String getActionText() {
        return action;
    }

    public static AbilityAction fromBukkitAction(Action action, boolean isSneaking) {
        switch (action) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                return isSneaking ? SHIFT_RIGHT_CLICK : RIGHT_CLICK;
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                return isSneaking ? SHIFT_LEFT_CLICK : LEFT_CLICK;
            default:
                break;
        }
        return null;
    }
}
