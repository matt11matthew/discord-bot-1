package me.matthewe.infure.enforcer.discordbot.redislisteners;

import me.matthewe.infure.enforcer.common.packets.discord.ConfirmUnlinkProfileDiscordPacket;
import me.matthewe.infure.enforcer.common.packets.discord.UnlinkProfileDiscordPacket;
import me.matthewe.infure.enforcer.discordbot.InfureDiscordBot;
import net.atherial.api.redis.AtherialPacketListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

/**
 * Created by Matthew E on 11/12/2019 at 12:29 PM for the project InfureEnforcer
 */
public class UnlinkProfileDiscordPacketListener implements AtherialPacketListener<UnlinkProfileDiscordPacket> {
    private InfureDiscordBot discordBot;

    public UnlinkProfileDiscordPacketListener(InfureDiscordBot discordBot) {
        this.discordBot = discordBot;
    }


    @Override
    public void onPacketReceived(String server, UnlinkProfileDiscordPacket packet) {

        String url = null;
        if (packet.getPlayerUuid() == null) {
            String uuid = "f78a4d8dd51b4b3998a3230f2de0c670";
            url = "https://crafatar.com/avatars/" + uuid + "?size=8t=MHF_Steve&overlay";
        } else {
            url = "https://crafatar.com/avatars/" + packet.getPlayerUuid().toString().replaceAll("-", "") + "?size=8t=MHF_Steve&overlay";
        }

        JDA jda = discordBot.getJDA();
        if (jda != null) {
            User user = jda.getUserById(packet.getDiscordId());
            if (user != null) {
                String finalUrl = url;
                user.openPrivateChannel().queue(privateChannel -> {
                    EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setDescription("Now unlinked with **" + packet.getPlayerName() + "**.")
                            .setAuthor(packet.getPlayerName() + " (" + server + ")", "https://infure.cc", finalUrl);
                    privateChannel.sendMessage(embedBuilder.build()).queue();

                });
            }
        }
        ConfirmUnlinkProfileDiscordPacket confirmUnlinkProfileDiscordPacket = new ConfirmUnlinkProfileDiscordPacket(packet.getPlayerUuid(), packet.getDiscordId(), packet.getFullName());
        discordBot.getAtherialRedis().sendPacket(confirmUnlinkProfileDiscordPacket, "all");
    }
}
/*
   JDA jda = discordBot.getJDA();
        if (jda != null) {
            User user = jda.getUserById(packet.getDiscordId());
            if (user != null) {
                String finalUrl = url;
                user.openPrivateChannel().queue(privateChannel -> {
                    EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setDescription("Now linked with **" + packet.getPlayerName()+"**.")
                            .setAuthor(packet.getPlayerName() + " (" + server + ")", "https://infure.cc", finalUrl);
                    privateChannel.sendMessage(embedBuilder.build()).queue();

                });
            }
        }
 */