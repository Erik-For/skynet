package se.skynet.skywars;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PlayerManager implements Listener {

    private Set<Location> spawnLocations = new HashSet<>();
    private HashMap<UUID, Location> usedLocations = new HashMap<>();
    private final List<Player> playersAlive = new ArrayList<>();
    private final List<Player> playersInGame = new ArrayList<>();

    private final GameManger gameManger;
    public PlayerManager(GameManger manger) {
        manger.getPlugin().getServer().getPluginManager().registerEvents(this, manger.getPlugin());
        this.gameManger = manger;
    }

    public void addSpawnLocation(Location location){
        spawnLocations.add(location);

        // this makes people (prob admins) who join the game after not show up on tab
        registerHidePlayerPacketListener();
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(gameManger.getGameState() == GameState.LOBBY){
            if(!spawnLocations.isEmpty()){
                playersAlive.add(player);
                playersInGame.add(player);
                Location location = spawnLocations.iterator().next();
                spawnLocations.remove(location);
                usedLocations.put(player.getUniqueId(), location);
                player.teleport(location);
            } else {
                player.kickPlayer("The game is full");
            }
        } else {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(usedLocations.containsKey(player.getUniqueId())){
            playersAlive.remove(player);
            playersInGame.remove(player);
            spawnLocations.add(usedLocations.get(player.getUniqueId()));
            usedLocations.remove(player.getUniqueId());
        }
    }

    public List<Player> getPlayersAlive() {
        return playersAlive;
    }

    private void registerHidePlayerPacketListener() {
        gameManger.getPlugin().getParentPlugin().getProtocolManager().addPacketListener(new PacketAdapter(gameManger.getPlugin().getParentPlugin(), ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if(!playersAlive.contains(event.getPlayer())){
                    return;
                }
                if(gameManger.getGameState() != GameState.LOBBY) {
                    if(event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
                        boolean cancel = true;
                        UUID target = event.getPacket().getPlayerInfoDataLists().read(0).get(0).getProfile().getUUID();
                        for (Player player : playersInGame) {
                            if (player.getUniqueId() == target) {
                                cancel = false;
                                break;
                            }
                        }
                        event.setCancelled(cancel);
                    } else if(event.getPacketType() == PacketType.Play.Server.NAMED_ENTITY_SPAWN){
                        UUID target = event.getPacket().getUUIDs().read(0);
                        boolean cancel = true;
                        for (Player player : playersInGame) {
                            if (player.getUniqueId() == target) {
                                cancel = false;
                                break;
                            }
                        }
                        event.setCancelled(cancel);
                    }
                }
            }
        });
    }
}
