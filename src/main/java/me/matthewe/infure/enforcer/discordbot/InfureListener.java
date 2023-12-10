package me.matthewe.infure.enforcer.discordbot;

import me.matthewe.infure.enforcer.discordbot.bot.DiscordBot;
import me.matthewe.infure.enforcer.discordbot.bot.OnEvent;

/**
 * Created by Matthew E on 11/7/2019 at 5:24 PM for the project InfureEnforcer
 */
public abstract class InfureListener<T extends DiscordBot<T>> implements OnEvent<T> {

    protected InfureDiscordBot discordBot;

}
