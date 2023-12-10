package me.matthewe.infure.enforcer.discordbot.config;

/**
 * Created by Matthew E on 11/6/2019 at 12:38 PM for the project InfureEnforcer
 */
public class Settings {
    public String token;
    public Redis redis;

    public Settings(String token, Redis redis) {
        this.token = token;
        this.redis = redis;
    }

    public Redis getRedis() {
        return redis;
    }

    public String getToken() {
        return token;
    }

    public static class Redis {
        public String host;
        public int port;
        public String password;
        public String username;

        public Redis(String host, int port, String password, String username) {
            this.host = host;
            this.port = port;
            this.password = password;
            this.username = username;
        }

        public boolean hasAuth() {
            return password != null && !password.isEmpty();
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }
    }
}
