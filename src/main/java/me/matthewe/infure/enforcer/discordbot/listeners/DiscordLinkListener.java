package me.matthewe.infure.enforcer.discordbot.listeners;

import me.matthewe.infure.enforcer.common.packets.discord.ConfirmLinkDiscordPacket;
import me.matthewe.infure.enforcer.discordbot.InfureDiscordBot;
import me.matthewe.infure.enforcer.discordbot.InfureListener;
import me.matthewe.infure.enforcer.discordbot.config.Config;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.UUID;

/**
 * Created by Matthew E on 11/7/2019 at 5:25 PM for the project InfureEnforcer
 */
public class DiscordLinkListener extends InfureListener<InfureDiscordBot> {
    private InfureDiscordBot infureDiscordBot;

    public DiscordLinkListener(InfureDiscordBot infureDiscordBot) {
        this.infureDiscordBot = infureDiscordBot;
    }

    @Override
    public void onEvent(InfureDiscordBot discordBot, GenericEvent genericEvent) {
        this.discordBot = discordBot;

        if (genericEvent instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent event = (GuildMessageReceivedEvent) genericEvent;
            if (!event.getAuthor().isBot() && infureDiscordBot.config.getGuilds().containsKey(event.getGuild().getIdLong())) {
                Config.GuildConfig guildConfig = infureDiscordBot.config.getGuilds().get(event.getGuild().getIdLong());

                final String tokenMessage = event.getMessage().getContentRaw().trim();


                User author = event.getAuthor();
                if (infureDiscordBot.linkTokens.containsKey(tokenMessage)) {
                    event.getMessage().delete().queue();
                    long idLong = author.getIdLong();
                    String fullName = author.getName() + "#" + author.getDiscriminator();
                    UUID uuid = infureDiscordBot.linkTokens.get(tokenMessage.trim());
                    ConfirmLinkDiscordPacket confirmLinkDiscordPacket = new ConfirmLinkDiscordPacket(uuid, tokenMessage.trim(), idLong, fullName, System.currentTimeMillis());
                    this.infureDiscordBot.getAtherialRedis().sendPacket(confirmLinkDiscordPacket, "all");
                }
            }
        }

    }
}
