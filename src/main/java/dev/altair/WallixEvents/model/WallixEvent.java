package dev.altair.WallixEvents.model;

import dev.altair.WallixEvents.WallixSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий один ивент.
 */
public class WallixEvent {

    private final String name;
    private final String worldName;
    private final double x, y, z;
    private long delaySeconds;
    private final List<String> hologramLines;

    private boolean running = false;
    private final List<ArmorStand> hologramStands = new ArrayList<>();
    private BukkitRunnable scheduledTask;

    public WallixEvent(String name, String worldName, double x, double y, double z,
                       long delaySeconds, List<String> hologramLines) {
        this.name = name;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.delaySeconds = delaySeconds;
        this.hologramLines = hologramLines;
    }

    public String getName() {
        return name;
    }

    public boolean isRunning() {
        return running;
    }

    public void setDelaySeconds(long seconds) {
        this.delaySeconds = seconds;
    }

    private Location getLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;
        return new Location(world, x, y, z);
    }

    /** Запуск ивента (с учётом задержки). */
    public void start() {
        if (running) return;
        running = true;

        if (delaySeconds > 0) {
            scheduledTask = new BukkitRunnable() {
                long remaining = delaySeconds;

                @Override
                public void run() {
                    if (remaining <= 0) {
                        spawnHologram();
                        cancel();
                    } else {
                        // Можно добавить вывод таймера в чат, но пока просто считаем.
                        remaining--;
                    }
                }
            };
            scheduledTask.runTaskTimer(WallixSpawn.getInstance(), 0L, 20L);
        } else {
            spawnHologram();
        }
    }

    /** Остановка ивента. */
    public void stop() {
        if (!running) return;
        running = false;
        if (scheduledTask != null) {
            scheduledTask.cancel();
        }
        removeHologram();
    }

    /** Перезапуск. */
    public void restart() {
        stop();
        start();
    }

    /** Создание голограммы из ArmorStand'ов. */
    private void spawnHologram() {
        Location base = getLocation();
        if (base == null) {
            WallixSpawn.getInstance().getLogger().warning("Не найден мир " + worldName + " для ивента " + name);
            return;
        }

        double lineHeight = 0.25;
        double startY = base.getY() + (hologramLines.size() - 1) * lineHeight;

        for (int i = 0; i < hologramLines.size(); i++) {
            String raw = hologramLines.get(i);