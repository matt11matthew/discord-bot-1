package me.matthewe.infure.enforcer.discordbot.redislisteners;

import me.matthewe.infure.enforcer.common.packets.chat.punishment.PunishmentLogPacket;
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
public class PunishmentLogPacketListener implements AtherialPacketListener<PunishmentLogPacket> {
    private InfureDiscordBot discordBot;

    public PunishmentLogPacketListener(InfureDiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void onPacketReceived(String fromServer, PunishmentLogPacket packet) {


        String url = null;
        if (packet.getPlayerUuid() == null) {
            String uuid = "f78a4d8dd51b4b3998a3230f2de0c670";
            url = "https://crafatar.com/avatars/" + uuid + "?size=8t=MHF_Steve&overlay";
        } else {
            url = "https://crafatar.com/avatars/" + packet.getPlayerUuid().toString().replaceAll("-", "") + "?size=8t=MHF_Steve&overlay";
        }

        EmbedBuilder embedBuilder = null;
        switch (packet.getPacketType()) {
            case WARN:
                embedBuilder = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setDescription("**" + packet.getKickerName() + "** has warned **"+packet.getPlayerName()+"**.")
                        .setTimestamp(Instant.ofEpochMilli(packet.getTimeStamp()))
                        .setAuthor(packet.getPlayerName() + " (" + fromServer + ")", "https://infure.cc", url);
                if (packet.getReason() != null && !packet.getReason().equalsIgnoreCase("none")) {
                    embedBuilder.addField("Reason", packet.getReason(), false).addBlankField(false);
                }
                break;
            case KICK:
                embedBuilder = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setDescription("**" + packet.getKickerName() + "** has kicked **"+packet.getPlayerName()+"**.")
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
