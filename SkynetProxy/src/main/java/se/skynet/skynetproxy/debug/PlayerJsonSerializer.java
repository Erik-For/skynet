package se.skynet.skynetproxy.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Type;

public class PlayerJsonSerializer implements JsonSerializer<ProxiedPlayer> {

    @Override
    public JsonElement serialize(ProxiedPlayer proxiedPlayer, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", proxiedPlayer.getUniqueId().toString());
        jsonObject.addProperty("name", proxiedPlayer.getName());
        return jsonObject;
    }
}
