package dev.altair.manager;

import dev.altair.WallixSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Управление точкой спавна, сохраняемой в config.yml.
 */
public final class SpawnManager {

    private static Location spawnLocation = null;

    private SpawnManager() {}

    /**
     * Загрузка спавна из config.yml при старте плагина.
     */
    public static void load(WallixSpawn plugin) {
        FileConfiguration cfg = plugin.getConfig();
        if (cfg.contains("spawn.world")) {
            String worldName = cfg.getString("spawn.world");
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                double x = cfg.getDouble("spawn.x");
                double y = cfg.getDouble("spawn.y");
                double z = cfg.getDouble("spawn.z");
                float yaw = (float) cfg.getDouble("spawn.yaw");
                float pitch = (float) cfg.getDouble("spawn.pitch");
                spawnLocation = new Location(world, x, y, z, yaw, pitch);
            }
        }
    }

    /**
     * Сохранить новую точку спавна в config.yml.
     */
    public static void setSpawn(Location loc) {
        spawnLocation = loc.clone();
        WallixSpawn plugin = (WallixSpawn) Bukkit.getPluginManager().getPlugin("WallixSpawn");
        if (plugin == null) return;

        FileConfiguration cfg = plugin.getConfig();
        cfg.set("spawn.world", loc.getWorld().getName());
        cfg.set("spawn.x", loc.getX());
        cfg.set("spawn.y", loc.getY());
        cfg.set("spawn.z", loc.getZ());
        cfg.set("spawn.yaw", loc.getYaw());
        cfg.set("spawn.pitch", loc.getPitch());
        plugin.saveConfig();
    }

    /**
     * Получить текущую точку спавна.
     *
     * @return Location или null, если спавн не установлен
     */
    public static Location getSpawn() {
        return spawnLocation;
    }
}