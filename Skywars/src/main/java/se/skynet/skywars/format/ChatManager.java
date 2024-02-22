package se.skynet.skywars.format;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import se.skynet.skywars.Skywars;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ChatManager {

    public static String constructWinMessage(Map<UUID, Integer> kills, List<Player> players, Player winner, Skywars plugin) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.GOLD).append("Game over!").append("\n");
        sb.append(separator()).append("\n");
        sb.append(centerMessage(ChatColor.GOLD + "Winner: " + plugin.getParentPlugin().getPlayerDataManager().getPlayerData(winner.getUniqueId()).getRank().getRankColor() + winner.getName())).append("\n");

        Stream<Map.Entry<UUID, Integer>> sorted = kills.entrySet().stream().sorted((o1, o2) -> o2.getValue());

        AtomicInteger i = new AtomicInteger(1);
        sorted.forEach(entry -> {
            String place = i.get() == 1 ? "1st" : i.get() == 2 ? "2nd" : "3rd";
            Player player = plugin.getServer().getPlayer(entry.getKey());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append(ChatColor.GOLD)
                    .append(place)
                    .append(" - ")
                    .append(plugin.getParentPlugin().getPlayerDataManager().getPlayerData(player.getUniqueId()).getRank().getRankColor())
                    .append(player.getName()).append(ChatColor.GOLD)
                    .append(" - ")
                    .append(ChatColor.GREEN)
                    .append(entry.getValue());

            sb.append(centerMessage(stringBuilder.toString())).append("\n");
            i.getAndIncrement();
        });
        sb.append("\n");
        sb.append(separator()).append("\n");

        return sb.toString();
    }

    private final static int CENTER_PX = 154;

    public static String centerMessage(String message){
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return (sb.toString() + message);
    }

    public static String separator(){
        return ChatColor.AQUA + "----------------------------------------";
    }
}
