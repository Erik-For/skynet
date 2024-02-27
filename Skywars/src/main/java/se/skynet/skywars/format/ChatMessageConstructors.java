package se.skynet.skywars.format;

import org.bukkit.ChatColor;
import se.skynet.skywars.Skywars;
import se.skynet.skywars.SkywarsPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatMessageConstructors {

    public static String winMessage(List<SkywarsPlayer> kills, SkywarsPlayer winner, Skywars plugin) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.GOLD).append(ChatFormatUtils.centerMessage("Game over!")).append("\n");
        sb.append(ChatFormatUtils.separator(ChatColor.YELLOW)).append("\n");
        sb.append(ChatColor.GOLD);
        sb.append(ChatFormatUtils.centerMessage(ChatColor.GOLD + "Winner: " + winner.getFormattedName())).append("\n");

        List<SkywarsPlayer> sorted = kills.stream().sorted((o1, o2) -> {
            return o2.getKills().compareTo(o1.getKills());
        }).limit(3).collect(Collectors.toList());

        AtomicInteger i = new AtomicInteger(1);
        sorted.forEach(entry -> {
            String place = i.get() == 1 ? "1st" : i.get() == 2 ? "2nd" : "3rd";
            //Player player = plugin.getServer().getPlayer(entry.getKey());
            String stringBuilder = ChatColor.GOLD +
                    place +
                    " - " +
                    entry.getFormattedName() +
                    ChatColor.GOLD +
                    " - " +
                    ChatColor.GREEN +
                    entry.getKills();

            sb.append(ChatFormatUtils.centerMessage(stringBuilder)).append("\n");
            i.getAndIncrement();
        });
        sb.append("\n");
        sb.append(ChatFormatUtils.separator(ChatColor.YELLOW)).append("\n");

        return sb.toString();
    }

}
