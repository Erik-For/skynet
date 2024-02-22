package se.skynet.skywars.format;

import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.tuple.Triple;
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

        // make a triple with the top 3 killers
        AtomicInteger i = new AtomicInteger(1);

        Stream<Map.Entry<UUID, Integer>> sorted = kills.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        winner.sendMessage(String.valueOf(sorted.count()));


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
