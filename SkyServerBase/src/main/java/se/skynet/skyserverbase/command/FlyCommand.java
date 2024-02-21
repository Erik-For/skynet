package se.skynet.skyserverbase.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

public class FlyCommand extends Command {

    public FlyCommand(SkyServerBase plugin) {
        super(plugin, System.getenv("SERVER_TYPE").equalsIgnoreCase("LOBBY") ? Rank.MVP : Rank.MODERATOR);

    }

    @Override
    public boolean executeCommand(Player player, CustomPlayerData data, Command command, String label, String[] args) {
        player.setAllowFlight(!player.getAllowFlight());
        player.sendMessage((player.getAllowFlight() ? ChatColor.GREEN : ChatColor.RED) + "Fly mode " + (player.getAllowFlight() ? "enabled" : "disabled"));
        return true;
    }
}
