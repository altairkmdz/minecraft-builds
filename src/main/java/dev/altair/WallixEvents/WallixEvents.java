package dev.altair.WallixEvents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class WallixEvents extends JavaPlugin {

    private static WallixEvents instance;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();               // config.yml
        saveResource("events.yml", false); // events definition
        saveResource("messages.yml", false);
        saveResource("hologram.yml", false);

        this.eventManager = new EventManager(this);
        this.eventManager.loadEvents();

        // Register command
        PluginCommand cmd = this.getCommand("event");
        if (cmd != null) {
            EventCommand executor = new EventCommand(this, eventManager);
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        }

        // Register generic listener (for future extensions)
        Bukkit.getPluginManager().registerEvents(new GenericListener(this, eventManager), this);

        getLogger().info(ChatColor.translateAlternateColorCodes('&',
                "&aWallixEvents включён!"));
    }

    @Override
    public void onDisable() {
        if (eventManager != null) {
            eventManager.stopAllEvents();
        }
        getLogger().info(ChatColor.translateAlternateColorCodes('&',
                "&cWallixEvents выключен!"));
    }

    public static WallixEvents getInstance() {
        return instance;
    }

    public FileConfiguration getMessagesConfig() {
        return getConfig("messages.yml");
    }

    public FileConfiguration getEventsConfig() {
        return getConfig("events.yml");
    }

    public FileConfiguration getHologramConfig() {
        return getConfig("hologram.yml");
    }

    private FileConfiguration getConfig(String fileName) {
        return YamlConfigLoader.load(this, fileName);
    }
}