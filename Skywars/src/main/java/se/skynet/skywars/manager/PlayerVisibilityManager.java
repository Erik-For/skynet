package se.skynet.skywars.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.packet.PacketUtils;
import se.skynet.skywars.Skywars;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerVisibilityManager {

    private final Skywars plugin;
    private final Set<UUID> hiddenPlayers = new HashSet<>();

    public PlayerVisibilityManager(Skywars plugin) {
        this.plugin = plugin;
        registerListeners();
    }


    public void hidePlayer(Player player) {
        if(hiddenPlayers.contains(player.getUniqueId())) return;
        hiddenPlayers.add(player.getUniqueId());

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        PacketPlayOutEntityDestroy packet2 = new PacketPlayOutEntityDestroy(entityPlayer.getId());

        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if(onlinePlayer.equals(player)) continue;
            PacketUtils.sendPacketAll(packet, plugin.getParentPlugin());
            PacketUtils.sendPacketAll(packet2, plugin.getParentPlugin());
        }
    }

    public void showPlayer(Player player) {
        if(!hiddenPlayers.contains(player.getUniqueId())) return;
        hiddenPlayers.remove(player.getUniqueId());

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        PacketPlayOutNamedEntitySpawn packet2 = new PacketPlayOutNamedEntitySpawn(entityPlayer);

        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if(onlinePlayer.equals(player)) continue;
            PacketUtils.sendPacketAll(packet, plugin.getParentPlugin());
            PacketUtils.sendPacketAll(packet2, plugin.getParentPlugin());
        }
    }

    public boolean isShown(Player player) {
        return hiddenPlayers.contains(player.getUniqueId());
    }

    // this prevents packets related to hidden players from being sent to players clients
    // exception is made for the hidden player itself
    private void registerListeners() {
        ProtocolManager protocolManager = plugin.getParentPlugin().getProtocolManager();
        protocolManager.addPacketListener(
                new PacketAdapter(plugin.getParentPlugin(), PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        PacketType packetType = event.getPacketType();
                        if (packetType.equals(PacketType.Play.Server.PLAYER_INFO)) {
                            UUID target = event.getPacket().getPlayerInfoDataLists().read(0).get(0).getProfile().getUUID();
                            if (hiddenPlayers.contains(target) && event.getPlayer().getUniqueId() != target) {
                                event.setCancelled(true);
                            }
                        } else if (packetType.equals(PacketType.Play.Server.NAMED_ENTITY_SPAWN)) {
                            UUID target = event.getPacket().getUUIDs().read(0);
                            if (hiddenPlayers.contains(target) && event.getPlayer().getUniqueId() != target) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
        );
    }

}
