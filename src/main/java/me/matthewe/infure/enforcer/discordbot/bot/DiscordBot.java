package me.matthewe.infure.enforcer.discordbot.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 11/6/2019 at 12:26 PM for the project InfureEnforcer
 */
public abstract class DiscordBot<T extends DiscordBot<T>> extends ListenerAdapter implements Bot {
    private boolean running;
    private String botName;
    private String token;
    private List<OnEvent<T>> listeners = new ArrayList<>();
    public JDA jda;

    public DiscordBot(String botName) {
        this.botName = botName;
    }

    public void registerListener(OnEvent<T> eventCallback) {
        listeners.add(eventCallback);
    }

    public abstract Class<T> getDiscordBotClass();

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public JDA getJDA() {
        return jda;
    }

    @Override
    public String getBotName() {
        return botName;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void start() {
        registerListeners();
        try {
//            List<Object> listeners = new ArrayList<>();
////            listeners.add(this);
//            for (Object infureListener : infureListenerList) {
//                listeners.add(infureListener);
//            }

            DiscordBotEventCaller discordBotEventCaller = new DiscordBotEventCaller(this, listeners, getDiscordBotClass());
            this.jda = new JDABuilder(this.token)
//                    .setDisabledCacheFlags(EnumSet.of(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE))
//                    .setBulkDeleteSplittingEnabled(false)

                    .addEventListeners(this, discordBotEventCaller)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        running = false;
        onStop();
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        running = true;
        onStart();
    }

    public abstract void onConsoleMessage(String line);
}
