package dev.altair.WallixEvents.manager;

import dev.altair.WallixEvents.WallixEventsPlugin;
import dev.altair.WallixEvents.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Управление пользовательскими ивентами.
 * В events.yml хранится список доступных ивентов (только названия, но можно расширить).
 */
public class EventManager {

    private final WallixEventsPlugin plugin;
    private final Map<String, BukkitTask> activeEvents = new HashMap<>();

    public EventManager(WallixEventsPlugin plugin) {
        this.plugin = plugin;
    }

    public void startEvent(String name) {
        if (activeEvents.containsKey(name)) {
            plugin.getServer().broadcast(ChatUtil.translate("&cИвент '&e" + name + "&c' уже запущен!"));
            return;
        }

        // Пример простого повторяющегося задания: каждые