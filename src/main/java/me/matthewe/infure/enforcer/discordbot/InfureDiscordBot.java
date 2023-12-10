package me.matthewe.infure.enforcer.discordbot;

import me.matthewe.infure.enforcer.common.packets.chat.ChatLogPacket;
import me.matthewe.infure.enforcer.common.packets.chat.punishment.PunishmentLogPacket;
import me.matthewe.infure.enforcer.common.packets.discord.CancelLinkingDiscordPacket;
import me.matthewe.infure.enforcer.common.packets.discord.GenerateDiscordTokenPacket;
import me.matthewe.infure.enforcer.common.packets.discord.IngameConfirmLinkDiscordPacket;
import me.matthewe.infure.enforcer.common.packets.discord.UnlinkProfileDiscordPacket;
import me.matthewe.infure.enforcer.common.packets.staffchat.StaffChatPacket;
import me.matthewe.infure.enforcer.discordbot.bot.DiscordBot;
import me.matthewe.infure.enforcer.discordbot.config.Config;
import me.matthewe.infure.enforcer.discordbot.config.ConfigHandler;
import me.matthewe.infure.enforcer.discordbot.config.Settings;
import me.matthewe.infure.enforcer.discordbot.listeners.DiscordLinkListener;
import me.matthewe.infure.enforcer.discordbot.redislisteners.*;
import net.atherial.api.redis.AtherialRedis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Matthew E on 11/6/2019 at 12:29 PM for the project InfureEnforcer
 */
public class InfureDiscordBot extends DiscordBot<InfureDiscordBot> {
    public Map<String, UUID> linkTokens;
    public Config config;
    public Settings settings;
    protected AtherialRedis atherialRedis;

    public InfureDiscordBot() {
        super("InfureDiscordBot");
        Map<Long, Config.GuildConfig> guilds = new ConcurrentHashMap<>();
        guilds.put(595079480754569227L, new Config.GuildConfig(638243112715812864L, 643482800938287114L, 643586225533354027L));

        Config defaultConfig = new Config(guilds);

        this.linkTokens = new ConcurrentHashMap<>();

        Settings.Redis defaultRedis = new Settings.Redis("localhost", 6379, "", null);

        this.config = new ConfigHandler().load("config.json", defaultConfig, Config.class);
        this.settings = new ConfigHandler().load("settings.json",
                new Settings("NONE", defaultRedis), Settings.class);

        this.setToken(settings.token);


    }

    public AtherialRedis getAtherialRedis() {
        return atherialRedis;
    }

    @Override
    public void onStart() {
        this.atherialRedis = new AtherialRedis(settings.redis.host, settings.redis.port, settings.redis.password);
        this.atherialRedis.connect("DiscordBot");

        System.out.println("Connected.");

        atherialRedis.registerListener(ChatLogPacket.class, new ChatLogPacketListener(this));
        atherialRedis.registerListener(PunishmentLogPacket.class, new PunishmentLogPacketListener(this));
        atherialRedis.registerListener(GenerateDiscordTokenPacket.class, new DiscordLinkPacketListener(this));
        atherialRedis.registerListener(CancelLinkingDiscordPacket.class, new DiscordLinkCancelPacketListener(this));
        atherialRedis.registerListener(IngameConfirmLinkDiscordPacket.class, new DiscordLinkIngameConfirmPacketListener(this));
        atherialRedis.registerListener(UnlinkProfileDiscordPacket.class, new UnlinkProfileDiscordPacketListener(this));

        atherialRedis.registerListener(StaffChatPacket.class, (s, packet) -> {
            for (Map.Entry<Long, Config.GuildConfig> longGuildConfigEntry : config.getGuilds().entrySet()) {
                Guild guild = jda.getGuildById(longGuildConfigEntry.getKey());
                Config.GuildConfig guildConfig = longGuildConfigEntry.getValue();
                if (guild != null) {
                    TextChannel textChannelById = guild.getTextChannelById(guildConfig.getStaffChatChannel());
                    if (textChannelById != null) {
                        String url = "https://crafatar.com/avatars/" + packet.getSender().toString().replaceAll("-", "") + "?size=13t=MHF_Steve&overlay";
                        MessageEmbed build = new EmbedBuilder()
                                .setTimestamp(Instant.from(ZonedDateTime.now()))
                                .setAuthor(packet.getSenderUsername() + " (" + packet.getFromServer() + ")", "https://infure.cc", url)
                                .setColor(Color.GREEN)
                                .setDescription(packet.getRawMessage()).build();
                        textChannelById.sendMessage(build).queue();
                    }

                }
            }
        });
    }

    @Override
    public void registerListeners() {
        registerListener(new DiscordLinkListener(this));
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && config.getGuilds().containsKey(event.getGuild().getIdLong())) {
            Config.GuildConfig guildConfig = config.getGuilds().get(event.getGuild().getIdLong());
            if (event.getChannel().getIdLong() == guildConfig.getStaffChatChannel()) {
                event.getMessage().delete().queue();
            }
        }
    }

    @Override
    public void onStop() {

    }

    public Config getConfig() {
        return config;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public Class<InfureDiscordBot> getDiscordBotClass() {
        return InfureDiscordBot.class;
    }

    @Override
    public void onConsoleMessage(String line) {

    }

    public TextChannel getTextChannel(long id) {
        for (Guild guild : getGuilds()) {
            TextChannel textChannelById = guild.getTextChannelById(id);
            if (textChannelById != null) {
                return textChannelById;
            }
        }
        return null;
    }

    public java.util.List<Guild> getGuilds() {
        return config.getGuilds().keySet().stream().map(aLong -> jda.getGuildById(aLong)).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
