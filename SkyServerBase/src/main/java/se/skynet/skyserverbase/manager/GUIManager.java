package se.skynet.skyserverbase.manager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.reflections.Reflections;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.gui.GUI;

import java.util.HashMap;

public class GUIManager implements Listener {

    private final SkyServerBase plugin;

    private static HashMap<String, Class<? extends GUI>> guis = new HashMap<>();

    public GUIManager(SkyServerBase plugin) {
        this.plugin = plugin;
        Reflections reflections = new Reflections("se.skynet.skyserverbase.gui");
        for (Class<? extends GUI> listenerClass : reflections.getSubTypesOf(GUI.class)) {
            try {
                guis.put(listenerClass.getSimpleName(), listenerClass);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("FATAL: Failed to register listener " + listenerClass.getSimpleName());
            }
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof GUI) {
            GUI gui = (GUI) holder;
            gui.onClick(event);
        }
    }

    public static Class<? extends GUI> getGUI(String name) {
        return guis.get(name);
    }
}
