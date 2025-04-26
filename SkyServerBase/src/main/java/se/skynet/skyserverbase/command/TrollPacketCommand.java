package se.skynet.skyserverbase.command;

import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.packet.PacketConstructor;
import se.skynet.skyserverbase.packet.PacketUtils;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TrollPacketCommand extends Command {

    private final SkyServerBase plugin;
    public TrollPacketCommand(SkyServerBase plugin) {
        super(plugin, Rank.MANAGEMENT);
        this.plugin = plugin;
    }

    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            player.sendMessage("Usage: /trollpacket <player> <value>");
            player.sendMessage("0: Show welcome to demo screen.\n" +
                    "101: Tell movement controls.\n" +
                    "102: Tell jump control.\n" +
                    "103: Tell inventory control.\n" +
                    "104: Tell that the demo is over and print a message about how to take a screenshot.");
            return true;
        }

        Player target = plugin.getServer().getPlayer(strings[0]);
        if (target == null) {
            player.sendMessage("Player not found.");
            return true;
        }

        int value;
        try {
            value = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid value. Must be an integer.");
            return true;
        }
        if(value == 0 || value == 101 || value == 102 || value == 103 || value == 104) {
            player.sendMessage("Sending troll packet to " + target.getName() + " with value: " + value);
            PacketPlayOutGameStateChange gameEventPacket = PacketConstructor.createGameEventPacket(5, value);
            PacketUtils.sendPacket(target, gameEventPacket);
        } else {
            player.sendMessage("Invalid value. Must be 0, 101, 102, 103, or 104.");
            return true;
        }
        // Example of sending a troll packet
        // PacketUtils.sendPacket(target, new PacketPlayOutChat(new ChatComponentText("You have been trolled!"), (byte) 1));

        player.sendMessage("Troll packet sent to " + target.getName());
        return true;
    }

    @Override
    protected List<String> tabComplete(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> p.getName().toLowerCase().startsWith(strings[0].toLowerCase()))
                    .map(Player::getName)
                    .collect(Collectors.toList());
        } else if (strings.length == 2) {
            return Arrays.asList("0", "101", "102", "103", "104");
        }
        return new ArrayList<>();
    }
}
