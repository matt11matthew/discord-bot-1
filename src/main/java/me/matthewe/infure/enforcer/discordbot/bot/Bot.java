package me.matthewe.infure.enforcer.discordbot.bot;

import net.dv8tion.jda.api.JDA;

/**
 * Created by Matthew E on 11/6/2019 at 12:25 PM for the project InfureEnforcer
 */
public interface Bot {
    void onStart();

    void setToken(String token);

    String getToken();

    String getBotName();

    boolean isRunning();

    void registerListeners();


    JDA getJDA();

    void start();

    void stop();

    void onStop();
}
