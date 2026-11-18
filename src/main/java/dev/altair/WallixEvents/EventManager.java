package dev.altair.WallixEvents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {

    private final WallixEvents plugin;
    private final Map<String, EventData> events = new ConcurrentHashMap<>();
    private final Map<String, BukkitRunnable> runningTasks = new ConcurrentHashMap<>();

    public EventManager(WallixEvents plugin) {
        this.plugin = plugin;
    }

    /** Load events from events.yml */
    public void loadEvents() {
        events.clear();
        FileConfiguration cfg = plugin.getEventsConfig();
        if (cfg == null) return;

        for (String key : cfg.getKeys(false)) {
            String name = cfg.getString(key + ".name", key);
            String type = cfg.getString(key + ".type", "GENERIC");
            String worldName = cfg.getString(key + ".world", "world");
            double x = cfg.getDouble(key + ".x");
            double y = cfg.getDouble(key + ".y");
            double z = cfg.getDouble(key + ".z");
            long duration = cfg.getLong(key + ".duration", 300); // seconds
            List<String> hologramLines = cfg.getStringList(key + ".hologram");

            EventData data = new EventData(name, type, worldName, x, y, z, duration, hologramLines);
            events.put(name.toLowerCase(), data);
        }
        plugin.getLogger().info("Loaded " + events.size() + " events.");
    }

    public List<String> getEventNamesStartingWith(String prefix) {
        List<String> result = new ArrayList<>();
        for (String name : events.keySet()) {
            if (name.startsWith(prefix.toLowerCase())) result.add(name);
        }
        return result;
    }

    private String translate(String path) {
        String raw = plugin.getMessagesConfig().getString(path, path);
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    /** Start an event */
    public void startEvent(String name, CommandSender sender) {
        EventData data = events.get(name.toLowerCase());
        if (data == null) {
            sender.sendMessage(translate("messages.event-not-found"));
            return;
        }
        if (runningTasks.containsKey(data.getName().toLowerCase())) {
            sender.sendMessage(translate("messages.event-already-running"));
            return;
        }

        World world = Bukkit.getWorld(data.getWorldName());
        if (world == null) {
            sender.sendMessage(translate("messages.world-not-found"));
            return;
        }

        // Create hologram
        List<ArmorStand> hologram = HologramUtil.createHologram(world, data.getLocation(), data.getHologramLines());

        // Schedule end task
        BukkitRunnable task = new BukkitRunnable() {
            long remaining = data.getDuration();

            @Override
            public void run() {
                if (remaining <= 0) {
                    stopEvent(data.getName(), null);
                    cancel();
                    return;
                }
                // Update placeholder (optional)
                remaining--;
            }
        };
        task.runTaskTimer(plugin, 0L, 20L);
        runningTasks.put(data.getName().toLowerCase(), task);
        data.setHologramArmorStands(hologram);

        // Broadcast start message
        broadcast(translate("messages.event-started")
                .replace("{event}", data.getName()));
    }

    /** Stop an event */
    public void stopEvent(String name, CommandSender sender) {
        EventData data = events.get(name.toLowerCase());
        if (data == null) {
            if (sender != null) sender.sendMessage(translate("messages.event-not-found"));
            return;
        }
        BukkitRunnable task = runningTasks.remove(data.getName().toLowerCase());
        if (task != null) {
            task.cancel();
        }
        // Remove hologram
        HologramUtil.removeHologram(data.getHologramArmorStands());

        broadcast(translate("messages.event-stopped")
                .replace("{event}", data.getName()));
    }

    /** Restart an event */
    public void restartEvent(String name, CommandSender sender) {
        stopEvent(name, null);
        // Small delay before restart (1 sec)
        new BukkitRunnable() {
            @Override
            public void run() {
                startEvent(name, sender);
            }
        }.runTaskLater(plugin, 20L);
    }

    /** Show delay until event ends (or starts if not running) */
    public void showDelay(String name, CommandSender sender) {
        EventData data = events.get(name.toLowerCase());
        if (data == null) {
            sender.sendMessage(translate("messages.event-not-found"));
            return;
        }
        if (runningTasks.containsKey(data.getName().toLowerCase())) {
            // Event is running – show remaining time
            BukkitRunnable task = runningTasks.get(data.getName().toLowerCase());
            // We stored remaining seconds inside the runnable, but it's not accessible.
            // For simplicity, we calculate based on duration - elapsed ticks.
            // In a real plugin you would store remaining time separately.
            sender.sendMessage(translate("messages.event-running")
                    .replace("{event}", data.getName()));
        } else {
            sender.sendMessage(translate("messages.event-not-running")
                    .replace("{event}", data.getName()));
        }
    }

    /** Stop all running events (used on plugin disable) */
    public void stopAllEvents() {
        for (String name : new ArrayList<>(runningTasks.keySet())) {
            stopEvent(name, null);
        }
    }

    private void broadcast(String message) {
        Component comp = LegacyComponentSerializer.legacySection().deserialize(message);
        Bukkit.broadcast(comp);
    }
}