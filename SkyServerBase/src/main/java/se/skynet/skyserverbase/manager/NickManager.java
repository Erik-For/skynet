package se.skynet.skyserverbase.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.mojang.authlib.properties.Property;
import org.bukkit.event.Listener;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.Nick;
import se.skynet.skyserverbase.playerdata.PlayerDataManager;
import se.skynet.skyserverbase.proxy.BungeeProxyApi;

import java.util.HashMap;
import java.util.UUID;
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
                                    if(playerDataManager.getPlayerData(playerInfoData.getProfile().getUUID()) == null || playerDataManager.getPlayerData(playerInfoData.getProfile().getUUID()).getRank() == null){
                                        return playerInfoData;
                                    }
                                    Nick nick = playerDataManager.getPlayerData(playerInfoData.getProfile().getUUID()).getNick();
                                    // modify using reflection
                                    WrappedGameProfile gameProfile = new WrappedGameProfile(playerInfoData.getProfile().getUUID(), nick.getNickname());
                                    gameProfile.getProperties().put("textures", new WrappedSignedProperty("textures", "eyJ0aW1lc3RhbXAiOjE1ODcyNDE1OTEyNzYsInByb2ZpbGVJZCI6IjRkNzA0ODZmNTA5MjRkMzM4NmJiZmM5YzEyYmFiNGFlIiwicHJvZmlsZU5hbWUiOiJzaXJGYWJpb3pzY2hlIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82ODY3MThkODVlMjViMDA2ZjJjOGYxNjBmNjE5YjIzYzhmZDZhZTc1ZGRmMWMwNjMwOGVjMGY1MzlkOTMxNzAzIn19fQ==", "qSJBND+tmy4O/+m/N21yHl6kWbuUIsjgugLgELLhpoqc55cd6DAGl4seGV9qE7tFCrNU1MgiSIeGE/mgqdzcGLyVsXsywJxiJzaSLra63I2EdsITvA9gMZVvcMlkPOmYOCF37d0hfOUW5eIxL9sq52B4yPws4k5Mfcd4PPD3NbAoA8exH9bRqH+hx7+pbhjCdkxIxwFEHfsp7t/DGzhbUJFW3ulEUHJHddXGE1JuqYxGsk9UhmQu7sA4bLOQuHisZj0CYXlsDXIopVVSEN7nnajEvCE4e2yoW1kHUfOsADQGkD0kBBw5A+VHz15dKFaLjmyGz0GrKTPNlXcrGPiHbCVU+WxXGAljfIXLtUiKPJksAmQTlIt6bGnrZ/oDWbp7WXizo4qogD5TTHz2ZBQ+wPf1h7BTjv1tVWjVhjEpbj2AXveUHL6CVYEv0Eb4GCXRpJO83z5sGGhyVDRnRZYLIUBapJvpDCpGYkQAiW+go04s4R5/RKpHJ9kxnhILXpxY/3NBz8rPy7NNvVBAQJOiXVX9IJbZkoxQwSjDO+VdD0cEb/Ov5vHtTkzhQuVTOUwP0DgdluPB6jq6Ui3nMSi1PbBESGCwiU6xMXk0E96saxF5NAET+n06yU7Si2Jju5mQX8cphG8jIm4pLxYaYulOqyHLOYAEjZdSd3f26FAWtVc="));
                                    return new PlayerInfoData(gameProfile, playerInfoData.getLatency(), playerInfoData.getGameMode(), playerInfoData.getDisplayName());
                                }).
                                collect(Collectors.toList())
                        );
                    }
                }
        );
    }



}
