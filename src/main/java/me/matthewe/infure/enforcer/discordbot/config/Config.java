package me.matthewe.infure.enforcer.discordbot.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 11/6/2019 at 12:38 PM for the project InfureEnforcer
 */
public class Config {
    private Map<Long, GuildConfig> guilds = new ConcurrentHashMap<>();


    public Config(Map<Long, GuildConfig> guilds) {
        this.guilds = guilds;
    }

    public GuildConfig getGuildConfig(long idLong) {
        return guilds.getOrDefault(idLong, null);
    }

    public static class GuildConfig {
        private long staffChatChannel;
        private long chatLogChannel;
        private long linkLogChannel;

        public GuildConfig(long staffChatChannel, long chatLogChannel, long linkLogChannel) {
            this.staffChatChannel = staffChatChannel;
            this.chatLogChannel = chatLogChannel;
            this.linkLogChannel= linkLogChannel;
        }


        public long getLinkLogChannel() {
            return linkLogChannel;
        }

        public long getChatLogChannel() {
            return chatLogChannel;
        }

        public long getStaffChatChannel() {
            return staffChatChannel;
        }
    }

    public Map<Long, GuildConfig> getGuilds() {
        return guilds;
    }
}
