package me.matthewe.infure.enforcer.discordbot.redislisteners;

import me.matthewe.infure.enforcer.common.packets.discord.CancelLinkingDiscordPacket;
import me.matthewe.infure.enforcer.common.packets.discord.ConfirmCancelLinkingDiscordPacket;
import me.matthewe.infure.enforcer.discordbot.InfureDiscordBot;
import net.atherial.api.redis.AtherialPacketListener;

/**
 * Created by Matthew E on 11/11/2019 at 11:22 AM for the project InfureEnforcer
 */
public class DiscordLinkCancelPacketListener implements AtherialPacketListener<CancelLinkingDiscordPacket> {
    private InfureDiscordBot discordBot;

    public DiscordLinkCancelPacketListener(InfureDiscordBot discordBot) {
        this.discordBot = discordBot;
    }


    @Override
    public void onPacketReceived(String server, CancelLinkingDiscordPacket packet) {
        if (this.discordBot.linkTokens.containsKey(packet.getToken())){
            this.discordBot.linkTokens.remove(packet.getToken());
            discordBot.getAtherialRedis().sendPacket(new ConfirmCancelLinkingDiscordPacket(packet.getPlayerUuid(),packet.getToken()), "all");
        }
    }
}
