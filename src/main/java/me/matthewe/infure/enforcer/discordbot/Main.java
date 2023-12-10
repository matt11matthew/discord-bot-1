package me.matthewe.infure.enforcer.discordbot;

import java.util.Scanner;

/**
 * Created by Matthew E on 11/6/2019 at 12:57 PM for the project InfureEnforcer
 */
public class Main {
    public static void main(String[] args) {
        InfureDiscordBot infureDiscordBot = new InfureDiscordBot();

        infureDiscordBot.start();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("stop")){
                scanner.close();
                System.exit(1);
                return;
            }
            infureDiscordBot.onConsoleMessage(line);
        }
    }
}
