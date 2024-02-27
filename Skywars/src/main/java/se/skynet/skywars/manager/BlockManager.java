package se.skynet.skywars.manager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import se.skynet.skywars.Game;
import se.skynet.skywars.GameState;

public class BlockManager implements Listener {

    private final Game game;

    public BlockManager(Game game) {
        this.game = game;
        game.getPlugin().getServer().getPluginManager().registerEvents(this, game.getPlugin());
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        if(!(game.getGameState() == GameState.IN_GAME) || (game.getGameState() == GameState.CAGE_DESTRUCTION)) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(!(game.getGameState() == GameState.IN_GAME) || (game.getGameState() == GameState.CAGE_DESTRUCTION)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!(game.getGameState() == GameState.IN_GAME) || (game.getGameState() == GameState.CAGE_DESTRUCTION)) {
            event.setCancelled(true);
        }
    }

}
