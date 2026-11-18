package dev.altair.WallixEvents.manager;

import dev.altair.WallixEvents.WallixSpawn;
import dev.altair.WallixEvents.model.WallixEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EventManager {

    private final WallixSpawn plugin;
    private final Map<String, WallixEvent> events = new HashMap<>();

    public EventManager(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    /** Загрузка всех ивентов из events.yml */
    public void loadEvents() {
        events.clear();
        File file = new File(plugin.getDataFolder(), "events.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        for (String key : cfg.getKeys(false)) {
            String path = key + ".";
            String name = key;
            String world = cfg.getString(path + "world");
            double x = cfg.getDouble(path + "x");
            double y = cfg.getDouble(path + "y");
            double z = cfg.getDouble(path + "z");
            long delay = cfg.getLong(path + "delay", 0L);
            java.util.List<String> hologram = cfg.getStringList(path + "hologram");

            WallixEvent event = new WallixEvent(name, world, x, y, z, delay, hologram);
            events.put(name.toLowerCase(), event);
        }
        plugin.getLogger().info("Загружено " + events.size() + " ивентов.");
    }

    public void reload() {
        loadEvents();
    }

    public WallixEvent getEvent(String name) {
        return events.get(name.toLowerCase());
    }

    public void stopAllEvents() {
        for (WallixEvent event : events.values()) {
            if (event.isRunning()) {
                event.stop();
            }
        }
    }
}