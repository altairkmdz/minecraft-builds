package dev.altair.WallixEvents.event;

import dev.altair.WallixEvents.WallixEventsPlugin;
import dev.altair.WallixEvents.util.Hologram;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.ChatColor;

import java.util.List;

public class Event {

    private final String name;
    private final Location location;
    private final List<String> hologramLines;
    private long delaySeconds; // задержка перед запуском
    private final WallixEventsPlugin plugin;

    private boolean running = false;
    private BukkitTask task;
    private Hologram hologram;

    public Event(String name, Location location, List<String> hologramLines, long delaySeconds, WallixEventsPlugin plugin) {
        this.name = name;
        this.location = location;
        this.hologramLines = hologramLines;
        this.delaySeconds = delaySeconds;
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void start() {
        if (running) return;
        running = true;
        // Показ голограммы сразу
        hologram = new Hologram(location, hologramLines);
        hologram.spawn();

        // Планируем автоматическое завершение через delay (если >0)
        if (delaySeconds > 0) {
            task = Bukkit.getScheduler().runTaskLater(plugin, this::stop, delaySeconds * 20L);
        }
    }

    public void stop() {
        if (!running) return;
        running = false;
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (hologram != null) {
            hologram.despawn();
            hologram = null;
        }
    }

    public void restart() {
        stop();
        start();
    }

    public void setDelay(long seconds) {
        this.delaySeconds = seconds;
        // Если событие уже запущено, пересчитываем задачу
        if (running) {
            if (task != null) task.cancel();
            if (seconds > 0) {
                task = Bukkit.getScheduler().runTaskLater(plugin, this::stop, seconds * 20L);
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public long getDelaySeconds() {
        return delaySeconds;
    }
}