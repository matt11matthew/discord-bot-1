package me.matthewe.infure.enforcer.discordbot.config;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 11/6/2019 at 12:40 PM for the project InfureEnforcer
 */
public class ConfigJsonAdapter implements JsonSerializer<Config>, JsonDeserializer<Config> {

    @Override
    public JsonElement serialize(Config config, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonObject guilds = new JsonObject();

        for (Map.Entry<Long, Config.GuildConfig> longGuildConfigEntry : config.getGuilds().entrySet()) {



            guilds.add(String.valueOf(longGuildConfigEntry.getKey()), ConfigHandler.gsonBuilder.create().toJsonTree(longGuildConfigEntry.getValue()));

        }
        jsonObject.add("guilds", guilds);
        return jsonObject;
    }

    @Override
    public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject guilds = jsonObject.getAsJsonObject("guilds");

        Map<Long, Config.GuildConfig> configMap = new ConcurrentHashMap<>();

        for (Map.Entry<String, JsonElement> stringJsonElementEntry : guilds.entrySet()) {
            String key = stringJsonElementEntry.getKey();
            Config.GuildConfig guildConfig = ConfigHandler.gsonBuilder.create().fromJson(stringJsonElementEntry.getValue(), Config.GuildConfig.class);
            configMap.put(Long.parseLong(key), guildConfig);

        }
        return new Config(configMap);
    }

}
