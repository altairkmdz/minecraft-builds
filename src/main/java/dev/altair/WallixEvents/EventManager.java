package dev.altair.WallixEvents;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

public class EventManager {

    private final WallixSpawn plugin;
    private final Map<String, EventData> events = new HashMap<>();
    private final Map<String, BukkitTask> scheduledTasks = new HashMap<>();

    public EventManager(WallixSpawn plugin) {
        this.plugin = plugin;
        loadEvents();
    }

    public void reloadEvents() {
        events.clear();
        scheduledTasks.values().forEach(BukkitTask::cancel);
        scheduledTasks.clear();
        loadEvents();
    }

    private void loadEvents() {
        File file = new File(plugin.getDataFolder(), "events.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for (String key : cfg.getKeys(false)) {
            String name = cfg.getString(key + ".name", key);
            String description = cfg.getString(key + ".description", "");
            events.put(name.toLowerCase(), new EventData(name, description));
        }
    }

    public Set<String> getAllEventNames() {
        return events.keySet();
    }

    public boolean startEvent(String name) {
        EventData data = events.get(name.toLowerCase());
        if (data == null) return false;
        // Здесь можно добавить любую логику старта ивента
        data.setActive(true);
        // Обновляем голограмму (если требуется)
        plugin.getHologramManager().updateHologramForEvent(data);
        return true;
    }

    public boolean stopEvent(String name) {
        EventData data = events.get(name.toLowerCase());
        if (data == null) return false;
        data.setActive(false);
        plugin.getHologramManager().removeHologramForEvent(data);
        return true;
    }

    public boolean restartEvent(String name) {
        if (!stopEvent(name)) return false;
        return startEvent(name);
    }

    public boolean scheduleEvent(String name, long delayTicks) {
        EventData data = events.get(name.toLowerCase());
        if (data == null) return false;
        // Если уже запланировано – отменяем
        BukkitTask old = scheduledTasks.remove(name.toLowerCase());
        if (old != null) old.cancel();

        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> startEvent(name), delayTicks);
        scheduledTasks.put(name.toLowerCase(), task);
        return true;
    }

    // Внутренний класс для хранения данных ивента
    public static class EventData {
        private final String name;
        private final String description;
        private boolean active = false;

        public EventData(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
}