package se.skynet.skynetproxy.manager;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import se.skynet.skynetproxy.SkyProxy;

public class BanManager implements Listener {

    private final SkyProxy plugin;

    public BanManager(SkyProxy plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void serverJoin(PostLoginEvent event){
        if(new se.skynet.skynetproxy.database.DatabaseMethods(plugin.getDatabaseConnectionManager()).isBanned(event.getPlayer().getUniqueId())){
            event.getPlayer().disconnect("You are banned from the server!");
        }
    }

}
