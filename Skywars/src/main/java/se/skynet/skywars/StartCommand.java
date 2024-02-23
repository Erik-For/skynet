package se.skynet.skywars;

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
        if(plugin.getGameManager().getGameState() != GameState.LOBBY){
            player.sendMessage("The game is not in the lobby state");
            return true;
        }
        if(plugin.getGameManager().getPlayerManager().getPlayersInGame().size() < 2){
            player.sendMessage("There are not enough players to start the game");
            return true;
        }
        plugin.getGameManager().setGameState(GameState.STARTING);
        return true;
    }
}
