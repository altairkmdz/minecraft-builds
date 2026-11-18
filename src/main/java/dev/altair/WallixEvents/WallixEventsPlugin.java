package dev.altair.WallixEvents;

import dev.altair.WallixEvents.command.EventCommand;
import dev.altair.WallixEvents.event.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WallixEventsPlugin extends JavaPlugin {

    private static WallixEventsPlugin instance;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();               // config.yml
        saveResource("events.yml", false); // events.yml
        saveResource("messages.yml", false); // messages.yml

        this.eventManager = new EventManager(this);
        this.eventManager.loadEvents();

        // Регистрация команды
        this.getCommand("event").setExecutor(new EventCommand(this));

        getLogger().info("§aWallixEvents включён!");
    }

    @Override
    public void onDisable() {
        if (eventManager != null) {
            eventManager.shutdownAll();
        }
        getLogger().info("§cWallixEvents выключен.");
    }

    public static WallixEventsPlugin getInstance() {
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}