package me.matthewe.infure.enforcer.discordbot.redislisteners;

import me.matthewe.infure.enforcer.common.packets.discord.GenerateDiscordTokenPacket;
import me.matthewe.infure.enforcer.discordbot.InfureDiscordBot;
import net.atherial.api.redis.AtherialPacketListener;

/**
 * Created by Matthew E on 11/11/2019 at 11:22 AM for the project InfureEnforcer
 */
public class DiscordLinkPacketListener implements AtherialPacketListener<GenerateDiscordTokenPacket> {
    private InfureDiscordBot discordBot;

    public DiscordLinkPacketListener(InfureDiscordBot discordBot) {
        this.discordBot = discordBot;
    }


    @Override
    public void onPacketReceived(String server, GenerateDiscordTokenPacket packet) {
        if (this.discordBot.linkTokens.containsKey(packet.getToken())){
            return;
        }
        this.discordBot.linkTokens.put(packet.getToken(), packet.getPlayerUuid());
    }
}
