package dev.altair.WallixEvents.event;

import dev.altair.WallixEvents.WallixSpawn;
import dev.altair.WallixEvents.util.ColorUtil;
import dev.altair.WallixEvents.hologram.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Управление пользовательскими ивентами.
 */
public class EventManager {

    private final WallixSpawn plugin;
    private final FileConfiguration eventsConfig;
    private final Map<String, BukkitTask> runningEvents = new HashMap<>();

    public EventManager(WallixSpawn plugin) {
        this.plugin = plugin;
        this.eventsConfig = plugin.getConfig("events.yml");
    }

    public void startEvent(String name, Player executor) {
        if (!eventsConfig.contains(name)) {
            executor.sendMessage(ColorUtil.translate("&cИвент &e" + name + " &cне найден в events.yml"));
            return;
        }
        if (runningEvents.containsKey(name)) {
            executor.sendMessage(ColorUtil.translate("&cИвент &e" + name + " &cуже запущен"));
            return;
        }

        // Пример простого ивента: каждые 5 секунд отправляем сообщение всем игрокам
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            String msg = eventsConfig.getString(name + ".message", "&aИвент &e" + name + " &aвыполняется!");
            Bukkit.broadcastMessage(ColorUtil.translate(msg));
        }, 0L, 100L); // 5 секунд (20 тик = 1 сек)

        runningEvents.put(name, task);
        executor.sendMessage(ColorUtil.translate("&aИвент &e" + name + " &aзапущен"));
        // Показать голограмму, если она указана
        HologramManager hologramManager = plugin.getHologramManager();
        hologramManager.showHologram(name);
    }

    public void stopEvent(String name, Player executor) {
        BukkitTask task = runningEvents.remove(name);
        if (task == null) {
            executor.sendMessage(ColorUtil.translate("&cИвент &e" + name + " &cне запущен"));
            return;
        }
        task.cancel();
        executor.sendMessage(ColorUtil.translate("&aИвент &e" + name + " &aостановлен"));
        // Убрать голограмму
        plugin.getHologramManager().hideHologram(name);
    }

    public void restartEvent(String name, Player executor) {
        stopEvent(name, executor);
        // небольшая задержка 2 секунды перед рестартом
        Bukkit.getScheduler().runTaskLater(plugin, () -> startEvent(name, executor), 40L);
    }

    public void delayEvent(String name, long seconds, Player executor) {
        if (!eventsConfig.contains(name)) {
            executor.sendMessage(ColorUtil.translate("&cИвент &e" + name + " &cне найден"));
            return;
        }
        if (runningEvents.containsKey(name)) {
            executor.sendMessage(ColorUtil.translate("&cИвент &e" + name + " &cуже запущен"));
            return;
        }
        long minutes = seconds / 60;
        long secs = seconds % 60;
        executor.sendMessage(ColorUtil.translate("&aИвент &e" + name + " &aзапустится через &e" + minutes + "м " + secs + "с"));
        Bukkit.getScheduler().runTaskLater(plugin, () -> startEvent(name, executor), seconds * 20L);
    }

    public void reload() {
        plugin.reloadConfigFile("events.yml");
    }
}