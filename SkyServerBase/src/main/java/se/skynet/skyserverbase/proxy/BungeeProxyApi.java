package se.skynet.skyserverbase.proxy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.v1_8_R3.Tuple;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.Nick;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class BungeeProxyApi extends Thread {

    private final String responseChannel;
    //private final SkyServerBase plugin;
    private final Jedis redisPublisher;
    private final Jedis redisSubscriber;

    public BungeeProxyApi(SkyServerBase plugin) {
        //this.plugin = plugin;
        this.redisPublisher = new Jedis(System.getenv("REDIS_HOST"), 6379);
        this.redisSubscriber = new Jedis(System.getenv("REDIS_HOST"), 6379);
        this.responseChannel = UUID.randomUUID().toString().split("-")[0];
        this.start();
    }

    private final ConcurrentHashMap<String, CompletableFuture<List<String>>> serverListResponseFuters = new ConcurrentHashMap<>();


    public final void sendMessage(String message){
        redisPublisher.publish("MESSAGE_PLAYER_DEBUG", message);
    }
    private Tuple<JsonObject, String> makeJson(String type){
        JsonObject json = new JsonObject();
        String id = UUID.randomUUID().toString().split("-")[0];
        json.addProperty("responseChannel", responseChannel);
        json.addProperty("id", id);
        json.addProperty("type", type);
        return new Tuple<>(json, id);
    }

    public CompletableFuture<List<String>> getServers(){
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        Tuple<JsonObject, String> json = makeJson("GET_SERVERS");

        serverListResponseFuters.put(json.b(), future);

        redisPublisher.publish("BUNGEE_API", json.a().toString());

        return future;
    }

    public void movePlayer(String playerName, String serverName){
        Tuple<JsonObject, String> tuple = makeJson("MOVE_PLAYER");
        JsonObject json = tuple.a();
        json.addProperty("playerName", playerName);
        json.addProperty("serverName", serverName);
        redisPublisher.publish("BUNGEE_API", json.toString());
    }

    private final ConcurrentHashMap<String, CompletableFuture<Integer>> serverPlayerCountResponseFuters = new ConcurrentHashMap<>();

    public CompletableFuture<Integer> getServerPlayerCount(String serverName){
        CompletableFuture<Integer> future = new CompletableFuture<>();
        Tuple<JsonObject, String> tuple = makeJson("GET_SERVER_PLAYER_COUNT");

        JsonObject json = tuple.a();

        json.addProperty("serverName", serverName);

        serverPlayerCountResponseFuters.put(tuple.b(), future);
        redisPublisher.publish("BUNGEE_API", json.toString());

        return future;
    }

    private String getServerIp(){
        try {
            return Inet4Address.getLocalHost().toString().split("/")[1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
    private final ConcurrentHashMap<String, CompletableFuture<String>> serverNameResponseFuters = new ConcurrentHashMap<>();

    // register server function that returns the server name as a completable future
    public CompletableFuture<String> registerServer(){
        CompletableFuture<String> future = new CompletableFuture<>();
        Tuple<JsonObject, String> tuple = makeJson("REGISTER_SERVER");
        JsonObject json = tuple.a();

        json.addProperty("serverType", System.getenv("SERVER_TYPE"));
        json.addProperty("ip", getServerIp());

        serverNameResponseFuters.put(tuple.b(), future);
        redisPublisher.publish("BUNGEE_API", json.toString());

        return future;
    }

    public void unregisterServer(String serverName){
        Tuple<JsonObject, String> tuple = makeJson("UNREGISTER_SERVER");
        JsonObject json = tuple.a();
        json.addProperty("serverName", serverName);
        redisPublisher.publish("BUNGEE_API", json.toString());
    }

    public void sendAllToServerType(String serverType, String serverName){
        Tuple<JsonObject, String> tuple = makeJson("SEND_ALL_TO_SERVER_TYPE");
        JsonObject json = tuple.a();
        json.addProperty("serverName", serverName);
        json.addProperty("serverType", serverType);
        redisPublisher.publish("BUNGEE_API", json.toString());
    }

    @Override
    public void run() {
        redisSubscriber.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                JsonObject json = new JsonParser().parse(message).getAsJsonObject();
                String type = json.get("type").getAsString();
                String id = json.get("id").getAsString();

                switch (type) {
                    case "GET_SERVERS_RESPONSE":
                        List<String> servers = Arrays.asList(json.get("servers").getAsString().split(","));
                        serverListResponseFuters.get(id).complete(servers);
                        break;
                    case "GET_SERVER_PLAYER_COUNT_RESPONSE":
                        int playerCount = json.get("playerCount").getAsInt();
                        serverPlayerCountResponseFuters.get(id).complete(playerCount);
                        break;
                    case "REGISTER_SERVER_RESPONSE":
                        String serverName = json.get("serverName").getAsString();
                        serverNameResponseFuters.get(id).complete(serverName);
                        break;
                }
            }
        }, responseChannel);
    }
}
