package dev.altair.WallixEvents.event;

import dev.altair.WallixEvents.WallixSpawn;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Управление кастомными ивентами, описанными в events.yml.
 * Каждый ивент может иметь задержку (delay) и действие (command) – упрощённый пример.
 */
public class EventManager {

    private final WallixSpawn plugin;
    private final Map<String, EventData> events = new HashMap<>();
    private final Map<String, BukkitTask> runningTasks = new HashMap<>();

    public EventManager(WallixSpawn plugin) {
        this.plugin = plugin;
        load();
    }

    /** Читает events.yml и заполняет карту events */
    public void load() {
        events.clear();
        File file = new File(plugin.getDataFolder(), "events.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for (String key : cfg.getKeys(false)) {
            long delay = cfg.getLong(key + ".delay", 0L);
            String command = cfg.getString(key + ".command", "");
            events.put(key.toLowerCase(), new EventData(key, delay, command));
        }
    }

    /** Перезагружает конфиги и останавливает все текущие задачи */
    public void reload() {
        stopAll();
        load();
    }

    /** Запускает ивент, если он существует и не запущен */
    public boolean startEvent(String name) {
        String lower = name.toLowerCase();
        if (!events.containsKey(lower) || runningTasks.containsKey(lower)) return false