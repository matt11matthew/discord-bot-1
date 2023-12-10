package me.matthewe.infure.enforcer.discordbot.redislisteners;

import me.matthewe.infure.enforcer.common.packets.discord.IngameConfirmLinkDiscordPacket;
import me.matthewe.infure.enforcer.discordbot.InfureDiscordBot;
import net.atherial.api.redis.AtherialPacketListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

/**
 * Created by Matthew E on 11/11/2019 at 11:22 AM for the project InfureEnforcer
 */
public class DiscordLinkIngameConfirmPacketListener implements AtherialPacketListener<IngameConfirmLinkDiscordPacket> {
    private InfureDiscordBot discordBot;

    public DiscordLinkIngameConfirmPacketListener(InfureDiscordBot discordBot) {
        this.discordBot = discordBot;
    }


    @Override
    public void onPacketReceived(String server, IngameConfirmLinkDiscordPacket packet) {
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
                            .setDescription("Now linked with **" + packet.getPlayerName()+"**.")
                            .setAuthor(packet.getPlayerName() + " (" + server + ")", "https://infure.cc", finalUrl);
                    privateChannel.sendMessage(embedBuilder.build()).queue();

                });
            }
        }
    }
}
