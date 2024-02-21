package se.skynet.skyserverbase.packet;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketConstructor {

    public static PacketPlayOutScoreboardTeam createTeamPacket(String teamName, String displayName, String prefix, String suffix, ScoreboardTeamBase.EnumNameTagVisibility nameTagVisibility, List<Player> players) {
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
            List<String> playerNames = new ArrayList<>();
            for(Player player : players){
                playerNames.add(player.getName());
            }

            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            PacketUtils.setField(packet, "a", teamName);
            PacketUtils.setField(packet, "b", displayName);
            PacketUtils.setField(packet, "c", prefix);
            PacketUtils.setField(packet, "d", suffix);
            PacketUtils.setField(packet, "e", nameTagVisibility.e);
            PacketUtils.setField(packet, "f", 0);
            PacketUtils.setField(packet, "g", playerNames);

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


}
