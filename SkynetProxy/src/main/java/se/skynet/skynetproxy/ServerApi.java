package se.skynet.skynetproxy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.config.ServerInfo;
import org.apache.commons.lang3.EnumUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import se.skynet.skynetproxy.server.Server;
import se.skynet.skynetproxy.server.ServerType;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServerApi extends Thread {

    private final SkyProxy proxy;
    private final Jedis jedisSubscriber;
    private final Jedis jedisPublisher;

    public ServerApi(SkyProxy proxy) {
        this.proxy = proxy;
        this.jedisSubscriber = new Jedis(System.getenv("REDIS_HOST"), 6379);
        this.jedisPublisher = new Jedis(System.getenv("REDIS_HOST"), 6379);
        this.start();
    }

    @Override
    public void run() {
        jedisSubscriber.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                JsonObject json = JsonParser.parseString(message).getAsJsonObject();
                String responseChannel = json.get("responseChannel").getAsString();
                String id = json.get("id").getAsString();
                String type = json.get("type").getAsString();

                switch (type) {
                    case "GET_SERVERS": {
                        JsonObject response = new JsonObject();
                        response.addProperty("type", "GET_SERVERS_RESPONSE");
                        response.addProperty("id", id);
                        response.addProperty("servers", proxy.getProxy().getServers().values().stream().map(serverInfo -> serverInfo.getName()).collect(Collectors.joining(",")));
                        jedisPublisher.publish(responseChannel, response.toString());
                        break;
                    }
                    case "MOVE_PLAYER":{
                        String playerName = json.get("playerName").getAsString();
                        String serverName = json.get("serverName").getAsString();
                        proxy.getProxy().getPlayer(playerName).connect(proxy.getProxy().getServerInfo(serverName));
                        break;
                    }
                    case "GET_SERVER_PLAYER_COUNT": {
                        JsonObject response = new JsonObject();
                        response.addProperty("type", "GET_SERVER_PLAYER_COUNT_RESPONSE");
                        response.addProperty("id", id);
                        response.addProperty("playerCount", proxy.getProxy().getServerInfo(json.get("serverName").getAsString()).getPlayers().size());
                        jedisPublisher.publish(responseChannel, response.toString());
                        break;
                    }
                    case "REGISTER_SERVER": {
                        String serverType = json.get("serverType").getAsString();
                        String ip = json.get("ip").getAsString();
                        if(!EnumUtils.isValidEnum(ServerType.class, serverType)){
                            System.out.println("Invalid server type: " + serverType);
                            return;
                        }
                        String serverName = serverType + "-" + UUID.randomUUID().toString().split("-")[0];
                        proxy.getServerManager().addServer(serverName, ServerType.valueOf(serverType), ip);
                        JsonObject response = new JsonObject();

                        response.addProperty("type", "REGISTER_SERVER_RESPONSE");
                        response.addProperty("id", id);
                        response.addProperty("serverName", serverName);
                        System.out.println("Registered server: " + serverName);
                        jedisPublisher.publish(responseChannel, response.toString());
                        break;
                    }
                    case "UNREGISTER_SERVER": {
                        String serverName = json.get("serverName").getAsString();
                        proxy.getServerManager().removeServer(serverName);
                        proxy.getProxy().getServers().remove(serverName);
                        System.out.println("Removed server: " + serverName);
                        break;
                    }
                    case "SEND_ALL_TO_SERVER_TYPE": {
                        String serverType = json.get("serverType").getAsString();
                        String serverName = json.get("serverName").getAsString();
                        if(!EnumUtils.isValidEnum(ServerType.class, serverType)){
                            System.out.println("Invalid server type: " + serverType);
                            return;
                        }
                        List<Server> servers = proxy.getServerManager().getServers(ServerType.valueOf(serverType));
                        proxy.getProxy().getServers().get(serverName).getPlayers().forEach(player -> {
                            Random rand = new Random();
                            ServerInfo randomServer = servers.get(rand.nextInt(servers.size())).getBungeeCordServerObject(proxy);
                            player.connect(randomServer);
                        });
                        break;
                    }
                }
            }
        }, "BUNGEE_API");
    }
}
