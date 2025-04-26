package se.skynet.skyserverbase.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.SkyServerBase;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class SignGUIManger {

    private final SkyServerBase plugin;
    private final ProtocolManager protocolManager;
    private final Map<UUID, Consumer<String[]>> signCallbacks = new HashMap<>();

    public SignGUIManger(SkyServerBase plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();

        // Register packet listener for sign updates
        protocolManager.addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        Player player = event.getPlayer();
                        UUID uuid = player.getUniqueId();

                        if (signCallbacks.containsKey(uuid)) {
                            // Get the edited text from the packet
                            PacketContainer packet = event.getPacket();
                            String[] lines = new String[4];

                            // Read the lines (implementation depends on server version)
                            if (packet.getChatComponentArrays().size() > 0) {
                                // 1.8.3 and newer
                                WrappedChatComponent[] components = packet.getChatComponentArrays().read(0);
                                for (int i = 0; i < components.length; i++) {
                                    lines[i] = components[i].getJson();
                                    // Strip JSON formatting
                                    lines[i] = lines[i].replaceAll("\"", "")
                                            .replaceAll("\\{text:", "")
                                            .replaceAll("\\}", "");
                                }
                            } else {
                                // 1.8.0 to 1.8.2
                                lines = packet.getStringArrays().read(0);
                            }

                            // Call the callback with the edited lines
                            Consumer<String[]> callback = signCallbacks.remove(uuid);
                            String[] finalLines = lines;
                            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(finalLines));

                            // Send a block change to revert the sign
                            BlockPosition position = packet.getBlockPositionModifier().read(0);
                            Location location = new Location(player.getWorld(), position.getX(), position.getY(), position.getZ());
                            player.sendBlockChange(location, location.getBlock().getType(), location.getBlock().getData());
                        }
                    }
                }
        );
    }

    /**
     * Opens a sign editor for a player with pre-filled text.
     *
     * @param player   The player to open the sign editor for
     * @param lines    The initial text on the sign (max 4 lines)
     * @param callback A consumer that receives the edited text when the player submits
     */
    public void open(Player player, String[] lines, Consumer<String[]> callback) {
        // Choose a position for the fake sign (below the player, out of sight)
        BlockPosition position = new BlockPosition(0, 0, 0);

        try {
            // Store the callback
            signCallbacks.put(player.getUniqueId(), callback);

            // Create and send a block change packet (to set a sign)
            PacketContainer blockChangePacket = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            blockChangePacket.getBlockPositionModifier().write(0, position);
            blockChangePacket.getBlockData().write(0, WrappedBlockData.createData(Material.WALL_SIGN));
            protocolManager.sendServerPacket(player, blockChangePacket);

            // Create and send a sign update packet to set the text
            PacketContainer signUpdatePacket = protocolManager.createPacket(PacketType.Play.Server.UPDATE_SIGN);
            signUpdatePacket.getBlockPositionModifier().write(0, position);

            // 1.8 uses arrays of IChatBaseComponent for sign text
            WrappedChatComponent[] components = new WrappedChatComponent[4];
            for (int i = 0; i < 4; i++) {
                String text = (i < lines.length && lines[i] != null) ? lines[i] : "";
                components[i] = WrappedChatComponent.fromText(text);
            }
            signUpdatePacket.getChatComponentArrays().write(0, components);

            protocolManager.sendServerPacket(player, signUpdatePacket);

            // Create and send a sign editor packet to open the GUI
            PacketContainer openSignPacket = protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
            openSignPacket.getBlockPositionModifier().write(0, position);
            protocolManager.sendServerPacket(player, openSignPacket);

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

