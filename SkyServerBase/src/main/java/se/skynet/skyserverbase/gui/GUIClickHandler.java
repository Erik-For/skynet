package se.skynet.skyserverbase.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;

public class GUIClickHandler {

    private final HashMap<Integer, ClickAction> clickActions = new HashMap<>();

    public void setClickAction(int slot, ClickAction action) {
        clickActions.put(slot, action);
    }

    public void handleClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if (clickActions.containsKey(slot)) {
            ClickAction action = clickActions.get(slot);
            action.onClick(event);
        }
    }

    public void clearClickActions() {
        clickActions.clear();
    }

    public void removeClickAction(int slot) {
        clickActions.remove(slot);
    }
}
