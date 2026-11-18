package dev.altair.WallixEvents.event;

import dev.altair.WallixEvents.WallixSpawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class EventManager {

    private final WallixSpawn plugin;
    private final Map<String, EventData> events = new HashMap<>();
    private final Set<String> runningEvents = new HashSet<>();

    public EventManager(WallixSpawn plugin) {
        this.plugin = plugin;
        loadEvents();
    }

    private void loadEvents() {
        FileConfiguration cfg = plugin.getConfig();
        if (!cfg.isConfigurationSection("events")) {
            plugin.getLogger().warning("В конфиге нет раздела events!");
            return;
        }
        for (String key : cfg.getConfigurationSection("events").getKeys(false)) {
            String displayName = cfg.getString("events." + key + ".display-name", key);
            long delay = cfg.getLong("events." + key + ".delay-seconds", 0L);
            events.put(key.toLowerCase(), new EventData(key, displayName, delay));
        }
    }

    public List<String> getAllEventNames() {
        return new ArrayList<>(events.keySet());
    }

    private String translate(String path) {
        String raw = plugin.getConfig().getString("messages." + path, "&c[Missing message: " + path + "]");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public void startEvent(String name, Player initiator) {
        EventData data = events.get(name.toLowerCase());
        if (data == null) {
            initiator.sendMessage(translate("event-not-found"));
            return;
        }
        if (runningEvents.contains(data.id)) {
            initiator.sendMessage(translate("event-already-running"));
            return;
        }
        runningEvents.add(data.id);
        Bukkit.broadcastMessage(translate("event-started")
                .replace("{event}", data.displayName));
        // Здесь можно добавить любую логику, связанную с событием
    }

    public void stopEvent(String name, Player initiator) {
        EventData data = events.get(name.toLowerCase());
        if (data == null) {
            initiator.sendMessage(translate("event-not-found"));
            return;
        }
        if (!runningEvents.contains(data.id)) {
            initiator.sendMessage(translate("event-not-running"));
            return;
        }
        runningEvents.remove(data.id);
        Bukkit.broadcastMessage(translate("event-stopped")
                .replace("{event}", data.displayName));
        // Очистка/завершение логики события
    }

    public void restartEvent(String name, Player initiator) {
        stopEvent(name, initiator);
        // небольшая задержка перед стартом, чтобы игроки успели увидеть сообщение
        Bukkit.getScheduler().runTaskLater(plugin, () -> startEvent(name, initiator), 20L);
    }

    public void sendDelayInfo(String name, Player player) {
        EventData data = events.get(name.toLowerCase());
        if (data == null) {
            player.sendMessage(translate("event-not-found"));
            return;
        }
        long seconds = data.delaySeconds;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        String msg = translate("event-delay")
                .replace("{event}", data.displayName)
                .replace("{minutes}", String.valueOf(minutes))
                .replace("{seconds}", String.valueOf(remainingSeconds));
        player.sendMessage(msg);
    }

    private static class EventData {
        final String id;
        final String displayName;
        final long delaySeconds;

        EventData(String id, String displayName, long delaySeconds) {
            this.id = id;
            this.displayName = displayName;
            this.delaySeconds = delaySeconds;
        }
    }
}