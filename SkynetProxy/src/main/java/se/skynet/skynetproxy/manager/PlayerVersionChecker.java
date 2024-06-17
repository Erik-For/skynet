package se.skynet.skynetproxy.manager;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import se.skynet.skynetproxy.SkyProxy;


public class PlayerVersionChecker implements Listener {

    private final SkyProxy core;

    public PlayerVersionChecker(SkyProxy core) {
        this.core = core;
    }


    @EventHandler
    public void preLogin(PreLoginEvent event){
        if(event.getConnection().getVersion() != Integer.valueOf(System.getenv("PROTOCOL_VERSION"))){
            event.setCancelled(true);
            event.setCancelReason(TextComponent.fromLegacyText("Wrong game version"));
        }
    }

}