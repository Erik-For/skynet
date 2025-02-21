package se.skynet.skyserverbase.packet;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;

import java.util.*;

public class PacketConstructor {

    public static PacketPlayOutScoreboardTeam createTeamPacket(String teamName, String displayName, String prefix, String suffix, ScoreboardTeamBase.EnumNameTagVisibility nameTagVisibility, List<String> players) {
        try {
            if(prefix.length() > 16){
                System.out.println("Prefix is too long, shortening it");
                prefix = prefix.substring(0, 16);
            }
            if(suffix.length() > 16){
                System.out.println("Suffix is too long, shortening it");
                suffix = suffix.substring(0, 16);
            }
            // turn players list into list of strings which are from player.getName()
            /*
            List<String> playerNames = new ArrayList<>();
            for(Player player : players){
                playerNames.add(player.getName());
            }
             */

            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            PacketUtils.setField(packet, "a", teamName);
            PacketUtils.setField(packet, "b", displayName);
            PacketUtils.setField(packet, "c", prefix);
            PacketUtils.setField(packet, "d", suffix);
            PacketUtils.setField(packet, "e", nameTagVisibility.e);
            PacketUtils.setField(packet, "f", 0);
            PacketUtils.setField(packet, "g", players);

            return packet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static PacketPlayOutScoreboardTeam removeTeamPacket(String teamName) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        PacketUtils.setField(packet, "a", teamName);
        PacketUtils.setField(packet, "h", 1);
        return packet;
    }


    public static List<Packet<?>> renickPlayer(Player player, int priority, String nickName, Rank rank, String textures, String signature) {
        List<Packet<?>> packets = new ArrayList<>();
        String oldTeamName = 9-priority + player.getUniqueId().toString().split("-")[0];
        packets.add(removeTeamPacket(oldTeamName));
        packets.add(removePlayerPacket(player.getUniqueId()));
        packets.add(addPlayerPacket(player.getUniqueId(), nickName, textures, signature));
        packets.add(destroyEntityPacket(player.getEntityId()));
        packets.add(spawnPlayerPacket(player));
        String teamName = 9-rank.getPriority() + player.getUniqueId().toString().split("-")[0];
        packets.add(createTeamPacket(teamName, teamName, rank.getPrefix(), "", ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS, Collections.singletonList(nickName)));
        return packets;
    }

    public static PacketPlayOutPlayerInfo removePlayerPacket(UUID uuid) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketUtils.setField(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        PacketUtils.setField(packet, "b", Collections.singletonList(packet.new PlayerInfoData(new GameProfile(uuid, "test"), 0, WorldSettings.EnumGamemode.SURVIVAL, null)));
        return packet;
    }

    public static PacketPlayOutPlayerInfo addPlayerPacket(UUID uuid, String name, String texture, String signature) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketUtils.setField(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        GameProfile profile = new GameProfile(uuid, name);
        // modify texture
        profile.getProperties().put("textures", new Property("textures", texture, signature));
        PacketUtils.setField(packet, "b", Collections.singletonList(packet.new PlayerInfoData(profile, 0, WorldSettings.EnumGamemode.SURVIVAL, null)));


        return packet;
    }

    public static PacketPlayOutEntityDestroy destroyEntityPacket(int entityId) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy();
        PacketUtils.setField(packet, "a", new int[]{entityId});
        return packet;
    }

    public static PacketPlayOutNamedEntitySpawn spawnPlayerPacket(Player player) {
        EntityPlayer eplayer = ((CraftPlayer) player).getHandle();
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(eplayer);
        /*
        PacketUtils.setField(packet, "a", player.getEntityId());
        PacketUtils.setField(packet, "b", player.getUniqueId());
        PacketUtils.setField(packet, "c", MathHelper.floor(player.getLocation().getX() * 32.0D));
        PacketUtils.setField(packet, "d", MathHelper.floor(player.getLocation().getY() * 32.0D));
        PacketUtils.setField(packet, "e", MathHelper.floor(player.getLocation().getZ() * 32.0D));
        PacketUtils.setField(packet, "f", (byte) ((int) (player.getLocation().getYaw() * 256.0F / 360.0F)));
        PacketUtils.setField(packet, "g", (byte) ((int) (player.getLocation().getPitch() * 256.0F / 360.0F)));
        PacketUtils.setField(packet, "h", 0);
         */

        return packet;

    }

    public static List<Packet<?>> fixPlayer(Player player) {
        List<Packet<?>> packets = new ArrayList<>();
        EntityPlayer eplayer = ((CraftPlayer) player).getHandle();

        PacketPlayOutRespawn packet = new PacketPlayOutRespawn(
                eplayer.dimension,
                eplayer.getWorld().getDifficulty(),
                eplayer.getWorld().worldData.getType(),
                eplayer.playerInteractManager.getGameMode()
        );
        packets.add(packet);

        PacketPlayOutPosition positionPacket = new PacketPlayOutPosition(
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                player.getLocation().getYaw(),
                player.getLocation().getPitch(),
                new HashSet<>()
        );
        packets.add(positionPacket);

        return packets;
    }
}
