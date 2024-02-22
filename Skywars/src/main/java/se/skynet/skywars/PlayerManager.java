package se.skynet.skywars;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import se.skynet.skyserverbase.packet.PacketUtils;

import java.util.*;

public class PlayerManager implements Listener {

    private final Map<UUID, Integer> kills = new HashMap<>();
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
    public void playerDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity();
        if(!playersAlive.contains(player)){
            event.setCancelled(true);
            return;
        }
        if(gameManger.getGameState() != GameState.INGAME){
            event.setCancelled(true);
        }
        if ((player.getHealth() - event.getFinalDamage()) <= 0){
            event.setCancelled(true);

            Player killer = gameManger.getTagManager().getTaggedPlayer(player);
            if(killer == null){
                gameManger.getPlugin().getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " Died");
            } else {
                gameManger.getPlugin().getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.RED + killer.getName());
                kills.put(killer.getUniqueId(), kills.getOrDefault(killer.getUniqueId(), 0) + 1);
            }

            player.setHealth(20);
            playersAlive.remove(player);
            player.getInventory().clear();
            player.teleport(new Location(player.getWorld(), 0, 100, 0));
            player.setAllowFlight(true);
            player.setFlying(true);
            hidePlayer(player);

            if(playersAlive.size() == 1){
                gameManger.setGameState(GameState.END);
            }
        }
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
            player.setAllowFlight(true);
            player.setFlying(true);
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

    private void hidePlayer(Player player) {
        // construct packet to remove player from tab
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PacketPlayOutPlayerInfo packetPlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        PacketPlayOutEntityDestroy packetDestroy = new PacketPlayOutEntityDestroy(entityPlayer.getId());

        for (Player onlinePlayer : gameManger.getPlugin().getServer().getOnlinePlayers()) {
            if (onlinePlayer == player) {
                continue;
            }
            PacketUtils.sendPacket(onlinePlayer, packetPlayerInfo);
            PacketUtils.sendPacket(onlinePlayer, packetDestroy);
        }

    }

    public Map<UUID, Integer> getKills() {
        return kills;
    }

    public List<Player> getPlayersInGame() {
        return playersInGame;
    }
}

