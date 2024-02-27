package se.skynet.skywars.manager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;
import se.skynet.skywars.Game;
import se.skynet.skywars.GameState;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager implements Listener {

    private final List<Player> playersInGame = new ArrayList<>();
    private final List<Player> playersAlive = new ArrayList<>();

    private final Game game;

    public PlayerManager(Game game) {
        game.getPlugin().getServer().getPluginManager().registerEvents(this, game.getPlugin());
        this.game = game;
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(game.getGameState() != GameState.WAITING) {
            event.getPlayer().setGameMode(org.bukkit.GameMode.SPECTATOR);
            return;
        }
        playersInGame.add(player);
        playersAlive.add(player);
    }

    @EventHandler
    public void onLeaveEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        playersInGame.remove(player);
        playersAlive.remove(player);
        CustomPlayerData data = game.getPlugin().getParentPlugin().getPlayerDataManager().getPlayerData(player.getUniqueId());
        game.getPlugin().getServer().broadcastMessage(data.getRank().getPrefix() + player.getName() + ChatColor.YELLOW + " left the game");
    }

    public List<Player> getPlayersAlive() {
        return playersAlive;
    }

    public List<Player> getPlayersInGame() {
        return playersInGame;
    }
}
