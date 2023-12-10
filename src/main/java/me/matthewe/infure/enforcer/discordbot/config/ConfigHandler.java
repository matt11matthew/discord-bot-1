package me.matthewe.infure.enforcer.discordbot.config;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Matthew E on 11/6/2019 at 12:48 PM for the project InfureEnforcer
 */
public class ConfigHandler {
    public static GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Config.class, new ConfigJsonAdapter())
            .registerTypeAdapter(Settings.Redis.class, new RedisJsonAdapter()
            );


    public <T extends Object> T load(String fileName, T defaultObject, Class<T> clazz) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
//            Map<Long, Config.GuildConfig> guilds = new ConcurrentHashMap<>();
//            guilds.put(595079480754569227L, new Config.GuildConfig(638243112715812864L));
//
//            Config defaultConfig = new Config(guilds);

            try {
                FileUtils.writeStringToFile(file, gsonBuilder.create().toJson(defaultObject,clazz), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        String lines = null;
        try {
            lines = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (lines == null) {
            return null;
        }
        T  config = gsonBuilder.create().fromJson(lines, clazz);
        return config;
    }
}
