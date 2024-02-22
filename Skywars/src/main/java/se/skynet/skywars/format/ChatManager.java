package se.skynet.skywars.format;

import com.google.gson.GsonBuilder;
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
        sb.append(ChatColor.GOLD).append("Winner: ").append(ChatColor.GREEN).append(winner.getName()).append("\n");

        // sort kills map by value
        // if there are more than 1 player, add 2nd place
        // if there are more than 2 player add 3rd place

        Stream<Map.Entry<UUID, Integer>> sorted = kills.entrySet().stream().sorted((o1, o2) -> o2.getValue());
        System.out.println("sorted: " + new GsonBuilder().setPrettyPrinting().create().toJson(sorted));
        // format: n:th place - (rankcolors) playername - kills
        AtomicInteger i = new AtomicInteger(1);
        sorted.forEach(entry -> {
            String place = i.get() == 1 ? "1st" : i.get() == 2 ? "2nd" : "3rd";
            Player player = plugin.getServer().getPlayer(entry.getKey());
            sb
                    .append(ChatColor.GOLD)
                    .append(place)
                    .append(" - ")
                    .append(plugin.getParentPlugin().getPlayerDataManager().getPlayerData(player.getUniqueId()).getRank().getRankColor())
                    .append(player.getName()).append(ChatColor.GOLD)
                    .append(" - ")
                    .append(ChatColor.GREEN)
                    .append(entry.getValue())
                    .append("\n");
            i.getAndIncrement();
        });

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
}
