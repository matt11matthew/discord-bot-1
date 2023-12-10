package me.matthewe.infure.enforcer.discordbot.bot;

import net.dv8tion.jda.api.events.GenericEvent;

/**
 * Created by Matthew E on 11/11/2019 at 11:04 AM for the project InfureEnforcer
 */
@FunctionalInterface
public interface OnEvent<T extends DiscordBot> {
    void onEvent(T discordBot, GenericEvent event);
}
