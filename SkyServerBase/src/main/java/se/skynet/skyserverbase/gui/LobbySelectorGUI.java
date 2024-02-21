package se.skynet.skyserverbase.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyserverbase.SkyServerBase;

import java.util.Arrays;
import java.util.HashMap;

public class LobbySelectorGUI implements GUI {

    private static final HashMap<Integer, String> slotServerMap = new HashMap<>();
    private static Inventory inv;
    private static Long lastUpdate = 0L;
    private final SkyServerBase plugin;

    public LobbySelectorGUI(SkyServerBase plugin) {
        this.plugin = plugin;

        if (System.currentTimeMillis() - lastUpdate < 1000 * 5) {
            return;
        }
        lastUpdate = System.currentTimeMillis();
        inv = plugin.getServer().createInventory(this, 9 * 4, "Lobby Selector");
        plugin.getBungeeProxyApi().getServers().whenComplete((servers, throwable) -> {
            if (throwable != null) { throwable.printStackTrace(); return; }
            final int[] slot = {0};
            for (String server : servers) {
                if (server.split("-")[0].equalsIgnoreCase("lobby")) {
                    plugin.getBungeeProxyApi().getServerPlayerCount(server).whenComplete((count, throwable1) -> {
                        if (throwable1 != null) { throwable1.printStackTrace(); return; }
                        boolean isCurrentServer = server.equalsIgnoreCase(plugin.getServerName());
                        slotServerMap.put(slot[0], server);

                        ItemStack item = new ItemStack(isCurrentServer ? Material.DIAMOND : Material.NETHER_STAR, 1);
                        ItemUtils.setName(item, ChatColor.GREEN + server);
                        ItemUtils.setLore(item,
                                Arrays.asList(
                                        ChatColor.GRAY +
                                                (isCurrentServer ? "You are currently on this server" : "Click to join"),
                                        ChatColor.GRAY + "Players: " + ChatColor.WHITE + count + ChatColor.GRAY + "/20"
                                )
                        );
                        inv.setItem(slot[0], item);
                        slot[0]++;
                    });
                }
            }
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if(event.getCurrentItem().getType().equals(Material.DIAMOND)) {
            ((Player) event.getWhoClicked()).sendMessage(ChatColor.RED + "You are already connected to this server");
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (slotServerMap.containsKey(event.getSlot())) {
            try {
                player.sendMessage(ChatColor.GREEN + "Connecting to " + slotServerMap.get(event.getSlot()));
                plugin.getBungeeProxyApi().movePlayer(player.getName(), slotServerMap.get(event.getSlot()));
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "An error occurred while trying to connect to the server, the server might have been shut down");
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

}
