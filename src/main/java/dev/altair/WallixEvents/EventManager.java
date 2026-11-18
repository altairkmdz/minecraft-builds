package dev.altair.WallixEvents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Менеджер ивентов.
 */
public class EventManager {

    private final WallixSpawn plugin;
    private final HologramManager hologramManager;
    private final Map<String, EventData> events = new HashMap<>();
    private final Map<String, BukkitTask> runningTasks = new HashMap<>();
    private final Map<String, Long> startTimestamps = new HashMap<>(); // для подсчёта оставшегося времени
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

    public EventManager(WallixSpawn plugin, HologramManager hologramManager) {
        this.plugin = plugin;
        this.hologramManager = hologramManager;
        loadEvents();
    }

    private void loadEvents() {
        events.clear();
        FileConfiguration cfg = plugin.getConfig(); // main config
        FileConfiguration evCfg = plugin.getConfig(); // placeholder, will be replaced
        ev