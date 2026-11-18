package dev.altair.WallixEvents.hologram;

import dev.altair.WallixEvents.WallixSpawn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HologramManager {

    private final WallixSpawn plugin;
    private final List<ArmorStand> holograms = new ArrayList<>();

    public HologramManager(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    public void loadHolograms() {
        // Читаем hologram.yml
        if (!plugin.getConfig().isConfigurationSection("holograms")) {
            plugin.getLogger().warning("В конфиге нет раздела holograms!");
            return;
        }

        for (String key : plugin.getConfig().getConfigurationSection("holograms").getKeys(false)) {
            Map<String, Object> section = plugin.getConfig().getConfigurationSection("holograms." + key).getValues(false);
            String worldName = (String) section.get("world");
            double x = ((Number) section.get("x")).doubleValue();
            double y = ((Number) section.get("y")).doubleValue();
            double z = ((Number) section.get("z")).doubleValue();
            String text = (String) section