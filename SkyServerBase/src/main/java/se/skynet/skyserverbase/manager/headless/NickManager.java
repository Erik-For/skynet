package se.skynet.skyserverbase.manager.headless;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import org.bukkit.event.Listener;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.Nick;
import se.skynet.skyserverbase.playerdata.PlayerDataManager;

import java.util.stream.Collectors;

public class NickManager implements Listener {

    private final SkyServerBase plugin;
    public NickManager(SkyServerBase plugin){
        this.plugin = plugin;
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();

        plugin.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.PLAYER_INFO) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        event.getPacket().getPlayerInfoDataLists().write(0, event.getPacket().getPlayerInfoDataLists()
                                .read(0)
                                .stream()
                                .map(playerInfoData -> {
                                    if(playerDataManager.getPlayerData(playerInfoData.getProfile().getUUID()) == null || playerDataManager.getPlayerData(playerInfoData.getProfile().getUUID()).getNick() == null){
                                        System.out.println("PlayerData or Nick is null" + playerInfoData.getProfile().getName());
                                        return playerInfoData;
                                    }
                                    System.out.println("PlayerData or Nick is not null" + playerInfoData.getProfile().getName());
                                    Nick nick = playerDataManager.getPlayerData(playerInfoData.getProfile().getUUID()).getNick();
                                    // modify using reflection
                                    WrappedGameProfile gameProfile = new WrappedGameProfile(playerInfoData.getProfile().getUUID(), nick.getNickname());
                                    gameProfile.getProperties().put("textures", new WrappedSignedProperty("textures", nick.getTexture(), nick.getSignature()));
                                    return new PlayerInfoData(gameProfile, playerInfoData.getLatency(), playerInfoData.getGameMode(), playerInfoData.getDisplayName());
                                }).
                                collect(Collectors.toList())
                        );
                    }
                }
        );
    }



}
