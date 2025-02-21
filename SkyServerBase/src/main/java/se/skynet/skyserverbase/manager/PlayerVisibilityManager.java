package se.skynet.skyserverbase.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.packet.PacketConstructor;
import se.skynet.skyserverbase.packet.PacketUtils;
import se.skynet.skyserverbase.playerdata.Nick;
import se.skynet.skyserverbase.playerdata.PlayerDataManager;

import java.util.*;

public class PlayerVisibilityManager implements Listener {

    private final SkyServerBase plugin;
    private PlayerDataManager dataManager;
    public PlayerVisibilityManager(SkyServerBase plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getPlayerDataManager();
    }

    public void hidePlayer(Player player) {
        if(dataManager.getPlayerData(player.getUniqueId()).isHidden()) return;
        Nick nick = dataManager.getPlayerData(player.getUniqueId()).getNick();
        List<Packet<?>> packets = new ArrayList<>();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        packets.add(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
        packets.add(new PacketPlayOutEntityDestroy(entityPlayer.getId()));


        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if(onlinePlayer.equals(player)) continue;

            PacketUtils.sendPacket(onlinePlayer, packets);
        }
        dataManager.getPlayerData(player.getUniqueId()).setHidden(true);
    }

    public void showPlayer(Player player) {
        if(!dataManager.getPlayerData(player.getUniqueId()).isHidden()) return;
        dataManager.getPlayerData(player.getUniqueId()).setHidden(false);
        Nick nick = dataManager.getPlayerData(player.getUniqueId()).getNick();
        List<Packet<?>> packets = new ArrayList<>();

        if(nick == null) {
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            packets.add(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
            packets.add(new PacketPlayOutNamedEntitySpawn(entityPlayer));
        } else {
            packets.add(PacketConstructor.addPlayerPacket(player.getUniqueId(), nick.getNickname(), nick.getTexture(), nick.getSignature()));
            packets.add(PacketConstructor.spawnPlayerPacket(player));
        }

        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if(onlinePlayer.equals(player)) continue;

            PacketUtils.sendPacket(onlinePlayer, packets);
        }
    }
    public boolean isPlayerHidden(Player player) {
        return dataManager.getPlayerData(player.getUniqueId()).isHidden();
    }

    private void registerListeners() {
        ProtocolManager protocolManager = plugin.getProtocolManager();
        Set<Player> hiddenPlayers = new HashSet<>();
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            if(dataManager.getPlayerData(player.getUniqueId()).isHidden()) {
                hiddenPlayers.add(player);
            }
        }
        protocolManager.addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
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
