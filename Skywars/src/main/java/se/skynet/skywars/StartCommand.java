package se.skynet.skywars;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.command.Command;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

public class StartCommand extends Command {

    private final Skywars plugin;
    public StartCommand(Skywars plugin) {
        super(plugin.getParentPlugin(), Rank.MODERATOR);
        this.plugin = plugin;
    }


    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        // Start the game
        if(plugin.getGame().getGameState() != GameState.WAITING){
            player.sendMessage(ChatColor.RED + "The game is not in the lobby state");
            return true;
        }
        if(plugin.getGame().getPlayerManager().getPlayersInGame().size() < 2){
            player.sendMessage(ChatColor.RED + "There are not enough players to start the game");
            return true;
        }
        plugin.getGame().setGameState(GameState.STARTING);
        return true;
    }


    @Override
    protected List<String> tabComplete(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        return Arrays.asList();
    }
}
