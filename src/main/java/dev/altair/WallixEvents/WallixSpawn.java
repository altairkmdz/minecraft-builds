package dev.altair.WallixEvents;

import dev.altair.WallixEvents.command.EventCommand;
import dev.altair.WallixEvents.manager.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WallixSpawn extends JavaPlugin {

    private static WallixSpawn instance;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();               // config.yml
        saveResource("events.yml", false); // events.yml
        saveResource("messages.yml", false);
        saveResource("hologram.yml", false);

        this.eventManager = new EventManager(this);
        this.eventManager.loadEvents();

        // Регистрация команды
        this.getCommand("event").setExecutor(new EventCommand(this));

        getLogger().info("WallixSpawn включён!");
    }

    @Override
    public void onDisable() {
        if (eventManager != null) {
            eventManager.stopAllEvents();
        }
        getLogger().info("WallixSpawn выключен!");
    }

    public static WallixSpawn getInstance() {
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    /** Утилита для перевода цветовых кодов (hex + &). */
    public static String color(String s) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', s);
    }
}