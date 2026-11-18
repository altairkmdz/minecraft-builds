package dev.altair.WallixEvents.hologram;

import dev.altair.WallixEvents.WallixSpawn;
import dev.altair.WallixEvents.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * Простейший менеджер голограмм на основе невидимых ArmorStand.
 */
public class HologramManager {

    private final WallixSpawn plugin;
    private final FileConfiguration hologramConfig;
    private final Map<String, ArmorStand> activeHolograms = new HashMap<>();

    public HologramManager(WallixSpawn plugin) {
        this.plugin = plugin;
        this.hologramConfig = plugin.getConfig("hologram.yml");
    }

    /**
     * Показать голограмму с именем, совпадающим с названием ивента.
     */
    public void showHologram(String eventName) {
        if (!hologramConfig.contains(eventName)) return;

        String worldName = hologramConfig.getString(eventName + ".world");
        double x = hologramConfig.getDouble(eventName + ".x");
        double y = hologramConfig.getDouble(eventName + ".y");
        double z = hologramConfig.getDouble(eventName + ".z");
        String text = hologramConfig.getString(eventName + ".text", "&a[Ивент] " + eventName);

        World world = Bukkit.getWorld(worldName);
        if (world == null) return;

        Location loc = new Location(world, x, y, z);
        ArmorStand stand = world.spawn(loc, ArmorStand.class, as -> {
            as.setInvisible(true);
            as.setCustomNameVisible(true);
            as.setGravity(false);
            as.setMarker(true);
            as.setCustomName(ColorUtil.translate(text));
            as.setSmall(true);
        });
        activeHolograms.put(eventName, stand);
    }

    /**
     * Удалить голограмму.
     */
    public void hideHologram(String eventName) {
        ArmorStand stand = activeHolograms.remove(eventName);
        if (stand != null && !stand.isDead()) {
            stand.remove();
        }
    }

    public void reload() {
        plugin.reloadConfigFile("hologram.yml");
        // При перезагрузке удаляем все текущие голограммы
        activeHolograms.values().forEach(ArmorStand::remove);
        activeHolograms.clear();
    }
}