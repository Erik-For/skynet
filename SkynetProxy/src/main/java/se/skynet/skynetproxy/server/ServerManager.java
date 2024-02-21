package se.skynet.skynetproxy.server;

import net.md_5.bungee.api.config.ServerInfo;
import se.skynet.skynetproxy.SkyProxy;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ServerManager {

    private final SkyProxy proxy;
    private final HashMap<String, Server> ipServerMap = new HashMap<>();
    private final HashMap<String, Server> nameServerMap = new HashMap<>();

    public ServerManager(SkyProxy proxy) {
        this.proxy = proxy;
    }

    public void addServer(String name, ServerType type, String ip) {
        Server server = new Server(type, name, new InetSocketAddress(ip, 25565));
        nameServerMap.put(server.getName(), server);
        ipServerMap.put(ip, server);
        ServerInfo serverInfo = proxy.getProxy().constructServerInfo(server.getName(), server.getAddress(), "", false);
        proxy.getProxy().getServers().put(server.getName(), serverInfo);
    }
    public void removeServer(String ip){
        nameServerMap.remove(ip);
    }

    public List<Server> getServers(ServerType type) {
        return nameServerMap.values().stream().filter(server -> server.getType() == type).collect(Collectors.toList());
    }
    public Server getServer(String name) {
        return nameServerMap.get(name);
    }
}
