package se.skynet.skynetproxy.debug;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Type;
import java.util.List;

public class PlayerListJsonSerializer implements JsonSerializer<List<ProxiedPlayer>> {


    @Override
    public JsonElement serialize(List<ProxiedPlayer> proxiedPlayerList, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonMerchant = new JsonArray();
        PlayerJsonSerializer playerJsonSerializer = new PlayerJsonSerializer();

        for (ProxiedPlayer proxiedPlayer : proxiedPlayerList) {
            jsonMerchant.add(playerJsonSerializer.serialize(proxiedPlayer, type, jsonSerializationContext));
        }
        return jsonMerchant;
    }
}
