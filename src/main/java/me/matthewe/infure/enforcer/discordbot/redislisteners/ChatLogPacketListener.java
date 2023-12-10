package me.matthewe.infure.enforcer.discordbot.redislisteners;

import me.matthewe.infure.enforcer.common.packets.chat.ChatLogPacket;
import me.matthewe.infure.enforcer.discordbot.InfureDiscordBot;
import me.matthewe.infure.enforcer.discordbot.config.Config;
import net.atherial.api.redis.AtherialPacketListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;

/**
 * Created by Matthew E on 11/11/2019 at 11:22 AM for the project InfureEnforcer
 */
public class ChatLogPacketListener implements AtherialPacketListener<ChatLogPacket> {
    private InfureDiscordBot discordBot;

    public ChatLogPacketListener(InfureDiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void onPacketReceived(String fromServer, ChatLogPacket packet) {


        String url = null;
        if (packet.getPlayerUuid() == null) {
            String uuid = "f78a4d8dd51b4b3998a3230f2de0c670";
            url = "https://crafatar.com/avatars/" + uuid + "?size=8t=MHF_Steve&overlay";
        } else {
            url = "https://crafatar.com/avatars/" + packet.getPlayerUuid().toString().replaceAll("-", "") + "?size=8t=MHF_Steve&overlay";
        }

        EmbedBuilder embedBuilder = null;
        switch (packet.getPacketType()) {
            case CLEAR_CHAT:
                embedBuilder = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setDescription("**" + packet.getPlayerName() + "** has cleared the chat.")
                        .setTimestamp(Instant.ofEpochMilli(packet.getTimeStamp()))
                        .setAuthor(packet.getPlayerName() + " (" + fromServer + ")", "https://infure.cc", url);
                if (packet.getReason() != null && !packet.getReason().equalsIgnoreCase("none")) {
                    embedBuilder.addField("Reason", packet.getReason(), false).addBlankField(false);
                }                break;
            case SET_DELAY:
                embedBuilder = new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setDescription("**" + packet.getPlayerName() + "** has set the chat delay to **" + packet.getTimeString() + "**.")
                        .setTimestamp(Instant.ofEpochMilli(packet.getTimeStamp()))
                        .setAuthor(packet.getPlayerName() + " (" + fromServer + ")", "https://infure.cc", url).addBlankField(false);
                break;
            case MUTE_CHAT:
                embedBuilder = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setDescription("**" + packet.getPlayerName() + "** has muted the chat.")
                        .setTimestamp(Instant.ofEpochMilli(packet.getTimeStamp()))
                        .setAuthor(packet.getPlayerName() + " (" + fromServer + ")", "https://infure.cc", url);
                if (packet.getReason() != null && !packet.getReason().equalsIgnoreCase("none")) {
                    embedBuilder.addField("Reason", packet.getReason(), false).addBlankField(false);
                }
                break;
            case UNMUTE_CHAT:
                embedBuilder = new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setDescription("**" + packet.getPlayerName() + "** has unmuted the chat.")
                        .setTimestamp(Instant.ofEpochMilli(packet.getTimeStamp()))
                        .setAuthor(packet.getPlayerName() + " (" + fromServer + ")", "https://infure.cc", url);
                if (packet.getReason() != null && !packet.getReason().equalsIgnoreCase("none")) {
                    embedBuilder.addField("Reason", packet.getReason(), false).addBlankField(false);
                }
                break;
            default:
                break;
        }
        for (Guild guild : discordBot.getGuilds()) {
            Config.GuildConfig guildConfig = discordBot.getConfig().getGuildConfig(guild.getIdLong());
            TextChannel textChannel = discordBot.getTextChannel(guildConfig.getChatLogChannel());
            if (textChannel != null) {
                textChannel.sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}
