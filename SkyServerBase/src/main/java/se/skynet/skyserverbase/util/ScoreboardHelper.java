package se.skynet.skyserverbase.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.ScoreboardAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardHelper {

    private static final Map<UUID, ProtocolScoreboard> scoreboards = new ConcurrentHashMap<>();

    public static void setScoreboard(Player player, List<String> lines) {
        createScoreboard(player, lines);
    }

    public static void removeScoreboard(Player player) {
        ProtocolScoreboard scoreboard = scoreboards.remove(player.getUniqueId());
        if (scoreboard != null) {
            scoreboard.remove();
        }
    }

    public static void updateLine(Player player, int index, String newText) {
        ProtocolScoreboard scoreboard = scoreboards.get(player.getUniqueId());
        if (scoreboard != null) {
            scoreboard.updateLine(index, newText);
        }
    }

    private static void createScoreboard(Player player, List<String> lines) {
        removeScoreboard(player);

        List<String> scoreboardLines = lines.subList(1, lines.size());
        String title = lines.get(0);
        ProtocolScoreboard scoreboard = new ProtocolScoreboard(player, title);

        for (int i = 0; i < scoreboardLines.size(); i++) {
            scoreboard.addLine(scoreboardLines.size() - i - 1, scoreboardLines.get(i));
        }

        scoreboards.put(player.getUniqueId(), scoreboard);
    }

    private static class ProtocolScoreboard {
        private final Player player;
        private final String objectiveName;
        private final ProtocolManager protocolManager;
        private final Map<Integer, String> lines = new HashMap<>();

        public ProtocolScoreboard(Player player, String title) {
            this.player = player;
            this.objectiveName = "sb_" + player.getName().substring(0, Math.min(player.getName().length(), 10));
            this.protocolManager = ProtocolLibrary.getProtocolManager();

            // Create objective
            PacketContainer objectivePacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
            objectivePacket.getStrings().write(0, objectiveName);
            objectivePacket.getStrings().write(1, title);
            objectivePacket.getIntegers().write(0, 0); // 0 = CREATE

            // Display objective
            PacketContainer displayPacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE);
            displayPacket.getIntegers().write(0, 1); // 1 = SIDEBAR
            displayPacket.getStrings().write(0, objectiveName);

            // Send packets
            sendPacket(objectivePacket);
            sendPacket(displayPacket);
        }

        public void addLine(int score, String text) {
            // Create score
            PacketContainer scorePacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_SCORE);
            scorePacket.getStrings().write(0, text);
            scorePacket.getStrings().write(1, objectiveName);
            scorePacket.getIntegers().write(0, score);
            scorePacket.getScoreboardActions().write(0, ScoreboardAction.CHANGE);

            sendPacket(scorePacket);
            lines.put(score, text);
        }

        public void updateLine(int index, String newText) {
            for (Map.Entry<Integer, String> entry : lines.entrySet()) {
                if (entry.getKey() == lines.size() - index - 1) {
                    // Remove old score
                    PacketContainer removePacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_SCORE);
                    removePacket.getStrings().write(0, entry.getValue());
                    removePacket.getStrings().write(1, objectiveName);
                    removePacket.getScoreboardActions().write(0, ScoreboardAction.REMOVE);

                    sendPacket(removePacket);

                    lines.remove(entry.getKey());
                    addLine(entry.getKey(), newText);
                    break;
                }
            }
        }

        public void remove() {
            // Remove all scores
            for (Map.Entry<Integer, String> entry : lines.entrySet()) {
                PacketContainer removeScorePacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_SCORE);
                removeScorePacket.getStrings().write(0, entry.getValue());
                removeScorePacket.getStrings().write(1, objectiveName);
                removeScorePacket.getScoreboardActions().write(0, ScoreboardAction.REMOVE);

                sendPacket(removeScorePacket);
            }

            // Remove objective
            PacketContainer removeObjectivePacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
            removeObjectivePacket.getStrings().write(0, objectiveName);
            removeObjectivePacket.getIntegers().write(0, 1); // 1 = REMOVE

            sendPacket(removeObjectivePacket);
        }

        private void sendPacket(PacketContainer packet) {
            try {
                protocolManager.sendServerPacket(player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ScoreboardBuilder {
        private final List<String> lines = new java.util.ArrayList<>();

        public ScoreboardBuilder(String title) {
            lines.add(title);
        }

        public ScoreboardBuilder addLine(String text) {
            lines.add(text);
            return this;
        }

        public List<String> build() {
            return new java.util.ArrayList<>(lines);
        }
    }

    // Send an actionbar message to a specific player
    public static void sendActionBar(Player player, String text) {
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendActionBarToAll(String text) {
        for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
            sendActionBar(player, text);
        }
    }
}