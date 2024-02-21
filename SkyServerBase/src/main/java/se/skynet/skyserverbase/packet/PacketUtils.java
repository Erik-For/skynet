package se.skynet.skyserverbase.packet;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.SkyServerBase;

import java.lang.reflect.Field;
import java.util.List;

public class PacketUtils {

    public static void setField(Object packet, final String fieldname, final Object value){
        try {
            final Field field = packet.getClass().getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(packet,value);
            field.setAccessible(false);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
    public static void sendPacket(Player player, List<Packet<?>> packets) {
        for (Packet<?> packet : packets) {
            sendPacket(player, packet);
        }
    }

    public static void sendPacketAll(Packet<?> packet, SkyServerBase plugin){
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            sendPacket(onlinePlayer, packet);
        }
    }
    public static void sendPacketAll(List<Packet<?>> packets, SkyServerBase plugin){
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            sendPacket(onlinePlayer, packets);
        }
    }
}
