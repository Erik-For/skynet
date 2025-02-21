package se.skynet.skyserverbase.command;

import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

public class HideCommand extends Command {

    public HideCommand(SkyServerBase plugin) {
        super(plugin, Rank.MODERATOR);
    }
    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        // Hide the player
        if(this.getPlugin().getPlayerVisibilityManager().isPlayerHidden(player)){
            this.getPlugin().getPlayerVisibilityManager().showPlayer(player);
            player.sendMessage("§aYou are now visible to other players.");
        } else {
            this.getPlugin().getPlayerVisibilityManager().hidePlayer(player);
            player.sendMessage("§cYou are now hidden from other players.");
        }
        return true;
    }
}
