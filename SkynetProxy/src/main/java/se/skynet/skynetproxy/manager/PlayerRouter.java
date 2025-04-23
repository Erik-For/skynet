package se.skynet.skynetproxy.manager;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.server.Server;
import se.skynet.skynetproxy.server.ServerType;

import java.util.List;
import java.util.Random;

public class PlayerRouter implements Listener {

    private final SkyProxy proxy;
    public PlayerRouter(SkyProxy proxy) {
        this.proxy = proxy;
    }

    @EventHandler
    public void onPlayerJoin(ServerConnectEvent event) {

        if(event.getReason() != ServerConnectEvent.Reason.JOIN_PROXY) {
            return;
        }
        System.out.println("Player " + event.getPlayer().getName() + " is joining the proxy");
        List<Server> servers = proxy.getServerManager().getServers(ServerType.LOBBY);
        System.out.println("Available lobby servers: " + servers.size());
        if (servers.isEmpty()) {
            event.getPlayer().disconnect(new ComponentBuilder("No lobby servers available").create());
            event.setCancelled(true);
            return;
        }
        Random rand = new Random();
        ServerInfo randomLobby = servers.get(rand.nextInt(servers.size())).getBungeeCordServerObject(proxy);
        event.setTarget(randomLobby);
    }
}
