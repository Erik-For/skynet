package se.skynet.skyserverbase.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.npc.NPC;
import se.skynet.skyserverbase.npc.NPCClickEvent;
import se.skynet.skyserverbase.packet.PacketUtils;

import java.util.ArrayList;
import java.util.List;

public class NpcManager implements Listener {

    protected static List<NPC> npcs = new ArrayList<>();
    private final SkyServerBase plugin;
//5.5 62 10.5 120 0
    public NpcManager(SkyServerBase plugin) {
        this.plugin = plugin;

        plugin.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                int entityId = packet.getIntegers().read(0);
                EnumWrappers.EntityUseAction action = packet.getEntityUseActions().read(0);

                for (NPC npc : npcs) {
                    if(npc.getId() == entityId){
                        NPCClickEvent.Action npcAction = action == EnumWrappers.EntityUseAction.INTERACT ? NPCClickEvent.Action.RIGHT : NPCClickEvent.Action.LEFT;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                npc.onInteract(new NPCClickEvent(player, npc, npcAction));
                            }
                        }.runTask(plugin);
                    }
                }
            }
        });
    }

    public static void addNpc(NPC npc) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            List<Packet<?>> packets = npc.getPackets();
            PacketUtils.sendPacket(onlinePlayer, packets);
            new BukkitRunnable() {
                @Override
                public void run() {
                    PacketUtils.sendPacket(onlinePlayer, npc.getRemoveTab());
                }
            }.runTaskLater(SkyServerBase.getPlugin(SkyServerBase.class), 20);
        }
        npcs.add(npc);
    }

    /*
    public void removeNpc(NPC npc) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            PacketUtils.sendPacket(onlinePlayer, npc.removePacket());
        }
        npcs.remove(npc);
    }
     */

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        for (NPC npc : npcs) {
            List<Packet<?>> packets = npc.getPackets();
            PacketUtils.sendPacket(player, packets);
            new BukkitRunnable() {
                @Override
                public void run() {
                    PacketUtils.sendPacket(player, npc.getRemoveTab());
                }
            }.runTaskLater(plugin, 20);
        }
    }

}
