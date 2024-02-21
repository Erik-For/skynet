package se.skynet.skyserverbase.manager;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import se.skynet.skyserverbase.packet.PacketConstructor;
import se.skynet.skyserverbase.packet.PacketUtils;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

import java.util.Arrays;
import java.util.Collections;

public class RankDisplayManager implements Listener {

    private final SkyServerBase plugin;

    public RankDisplayManager(SkyServerBase plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CustomPlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        Rank rank = data.getRank();
        event.setJoinMessage(rank.getPrefix() + rank.getRankColor() + player.getDisplayName() + ChatColor.GREEN + " [+]");
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        CustomPlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        Rank rank = data.getRank();
        event.setQuitMessage(rank.getPrefix() + rank.getRankColor() + player.getDisplayName() + ChatColor.RED + " [-]");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        CustomPlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        Rank rank = data.getRank();
        event.setFormat(rank.getPrefix() + rank.getRankColor() + player.getDisplayName() + (rank.getPriority() == 0 ? ChatColor.GRAY : ChatColor.WHITE) + ": " + event.getMessage());
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PacketPlayOutScoreboardTeam packet = makeTeamPacketForPlayer(player);
        PacketUtils.sendPacketAll(packet, plugin);

        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if(player == onlinePlayer) continue;
            PacketUtils.sendPacket(player, makeTeamPacketForPlayer(onlinePlayer));
        }
    }

    @EventHandler
    public void quitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        CustomPlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        // construct team packet
        String teamName = 9-data.getRank().getPriority() + player.getUniqueId().toString().split("-")[0];
        PacketPlayOutScoreboardTeam packet = PacketConstructor.removeTeamPacket(
            teamName
        );
        PacketUtils.sendPacketAll(packet, plugin);
    }

    public PacketPlayOutScoreboardTeam makeTeamPacketForPlayer(Player player){
        CustomPlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        // construct team packet
        String teamName = 9-data.getRank().getPriority() + player.getUniqueId().toString().split("-")[0];
        PacketPlayOutScoreboardTeam packet = PacketConstructor.createTeamPacket(
                teamName,
                teamName,
                data.getRank().getPrefix(),
                "",
                ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS,
                Collections.singletonList(player)
        );
        return packet;
    }
}