package me.matthewe.infure.enforcer.discordbot.bot;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Matthew E on 11/11/2019 at 10:39 AM for the project InfureEnforcer
 */
public class DiscordBotEventCaller<T extends DiscordBot> extends ListenerAdapter {
    private T discordBot;
    private List<OnEvent<T>> callbacks;
    private Class<T> clazz;

    public DiscordBotEventCaller(T discordBot,  List<OnEvent<T>> callbacks, Class<T> clazz) {
        this.discordBot = discordBot;
        this.callbacks = callbacks;
        this.clazz = clazz;
    }

    @Override
    public void onGenericEvent(@Nonnull GenericEvent event) {
        super.onGenericEvent(event);
        callbacks.forEach(eventCallback -> eventCallback.onEvent(discordBot,event));

    }

    public Class<T> getClazz() {
        return clazz;
    }
}
