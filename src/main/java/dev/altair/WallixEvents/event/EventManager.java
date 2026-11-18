package dev.altair.WallixEvents.event;

import dev.altair.WallixEvents.WallixEventsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class EventManager {

    private final WallixEventsPlugin plugin;
    private final Map<String, Event> events = new HashMap<>();

    public EventManager(WallixEventsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadEvents() {
        events.clear();
        File file = new File(plugin.getDataFolder(), "events.yml");
        if (!file.exists()) {
            plugin.saveResource("events.yml", false);
        }
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for (String key : cfg.getKeys(false)) {
            String path = key + ".";
            String name = key;
            String worldName = cfg.getString(path + "world");
            double x = cfg.getDouble(path + "x");
            double y = cfg.getDouble(path + "y");
            double z = cfg.getDouble(path + "z");
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                plugin.getLogger().warning("World '" + worldName + "' for event '" + name + "' not found, skipping.");
                continue;
            }
            Location loc = new Location(world, x, y, z);
            List<String> hologram = cfg.getStringList(path + "hologram");
            long delay = cfg.getLong(path + "delay", 0L);
            Event event = new Event(name, loc, hologram, delay, plugin);
            events.put(name.toLowerCase(Locale.ROOT), event);
        }
        plugin.getLogger().info("Loaded " + events.size() + " events.");
    }

    public void reloadEvents() {
        // Останавливаем все текущие ивенты, потом перечитываем конфиг
        shutdownAll();
        loadEvents();
    }

    public void shutdownAll() {
        for (Event e : events.values()) {
            e.stop();
        }
    }

    public Event getEvent(String name) {
        return events.get(name.toLowerCase(Locale.ROOT));
    }

    public Collection<Event> getAllEvents() {
        return Collections.unmodifiableCollection(events.values());
    }
}