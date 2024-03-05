package se.skynet.skywars.manager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        event.setCancelled(
                !(game.getGameState() == GameState.IN_GAME ||
                        game.getGameState() == GameState.CAGE_DESTRUCTION ||
                        game.getGameState() == GameState.END
                )
        );
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(
                !(game.getGameState() == GameState.IN_GAME ||
                        game.getGameState() == GameState.CAGE_DESTRUCTION ||
                        game.getGameState() == GameState.END
                )
        );
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(
                !(game.getGameState() == GameState.IN_GAME ||
                        game.getGameState() == GameState.CAGE_DESTRUCTION ||
                        game.getGameState() == GameState.END
                )
        );
    }

}
