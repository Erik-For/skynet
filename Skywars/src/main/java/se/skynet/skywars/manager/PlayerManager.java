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
import se.skynet.skywars.SkywarsPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerManager implements Listener {

    private final HashMap<UUID, SkywarsPlayer> playersInGame = new HashMap<>();
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
        playersInGame.put(player.getUniqueId(), new SkywarsPlayer(player, game));
    }

    @EventHandler
    public void onLeaveEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (game.getGameState() == GameState.WAITING) {
            playersInGame.remove(player.getUniqueId());
            return;
        }
        CustomPlayerData data = game.getPlugin().getParentPlugin().getPlayerDataManager().getPlayerData(player.getUniqueId());
        game.getPlugin().getServer().broadcastMessage(data.getRank().getPrefix() + player.getName() + ChatColor.YELLOW + " left the game");
    }

    public List<SkywarsPlayer> getPlayersAlive() {
        return playersInGame.values().stream().filter(SkywarsPlayer::isAlive).collect(Collectors.toList());
    }

    public List<SkywarsPlayer> getPlayersInGame() {
        return new ArrayList<>(playersInGame.values());
    }

    public SkywarsPlayer getPlayer(UUID uuid) {
        return playersInGame.get(uuid);
    }
}
