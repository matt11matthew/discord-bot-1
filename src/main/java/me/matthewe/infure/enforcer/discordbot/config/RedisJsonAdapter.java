package me.matthewe.infure.enforcer.discordbot.config;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Matthew E on 11/6/2019 at 12:40 PM for the project InfureEnforcer
 */
public class RedisJsonAdapter implements JsonSerializer<Settings.Redis>, JsonDeserializer<Settings.Redis> {

    @Override
    public JsonElement serialize(Settings.Redis redis, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("host", redis.getHost());
        jsonObject.addProperty("port", redis.getPort());
        jsonObject.addProperty("password", redis.getPassword());
        if (redis.hasAuth()) {
            jsonObject.addProperty("username", redis.getUsername());

        }
        return jsonObject;
    }

    @Override
    public Settings.Redis deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String host = jsonObject.get("host").getAsString();
        int port = jsonObject.get("port").getAsInt();
        String password = jsonObject.get("password").getAsString();
        String username = null;
        if (jsonObject.has("username")) {
            username = jsonObject.get("username").getAsString();
        }

        return new Settings.Redis(host,port,password,username);
    }

}
