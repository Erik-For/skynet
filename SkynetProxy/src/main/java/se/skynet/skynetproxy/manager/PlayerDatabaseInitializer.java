package se.skynet.skynetproxy.manager;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.database.DatabaseMethods;

public class PlayerDatabaseInitializer implements Listener {

    private final SkyProxy proxy;
    public PlayerDatabaseInitializer(SkyProxy proxy) {
        this.proxy = proxy;
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event){
        new DatabaseMethods(proxy.getDatabaseConnectionManager()).createPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }
}
