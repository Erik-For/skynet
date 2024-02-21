package se.skynet.skynetproxy.server;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.SkyProxy;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerType type;
    private String name;
    private InetSocketAddress address;

    protected Server(ServerType type, String name, InetSocketAddress address) {
        this.type = type;
        this.name = name;
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public ServerType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<ProxiedPlayer> getPlayers(SkyProxy proxy) {
        return new ArrayList<>(proxy.getProxy().getServers().get(name).getPlayers());
    }
    public ServerInfo getBungeeCordServerObject(SkyProxy proxy) {
        return proxy.getProxy().getServers().get(name);
    }

}
