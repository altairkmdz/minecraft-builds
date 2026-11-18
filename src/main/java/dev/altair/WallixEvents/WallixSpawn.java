package dev.altair.WallixEvents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class WallixSpawn extends JavaPlugin {

    private static WallixSpawn instance;
    private EventManager eventManager;
    private HologramManager hologramManager;
    private FileConfiguration messages;

    @Override
    public void onEnable() {
        instance = this;

        // Сохраняем и загружаем конфиги
        saveDefaultConfig();
        saveResource("events.yml", false);
        saveResource("messages.yml", false);
        saveResource("hologram.yml", false);
        reloadConfigs();

        // Инициализируем менеджеры
        this.eventManager = new EventManager(this);
        this.hologramManager = new HologramManager(this);

        // Регистрируем команду
        this.getCommand("event").setExecutor(new EventCommand(this));
        this.getCommand("event").setTabCompleter(new EventCommand(this));

        // При включении плагина создаём голограммы
        hologramManager.loadHolograms();

        getLogger().info(ChatColor.translateAlternateColorCodes('&',
                "&aWallixSpawn включён!"));
    }

    @Override
    public void onDisable() {
        // Удаляем все голограммы
        hologramManager.removeAll();
        getLogger().info(ChatColor.translateAlternateColorCodes('&',
                "&cWallixSpawn выключен!"));
    }

    public void reloadConfigs() {
        reloadConfig();
        messages = YamlConfiguration.loadConfiguration(
                new java.io.File(getDataFolder(), "messages.yml"));
        eventManager.reloadEvents();
        hologramManager.reloadHolograms();
    }

    public static WallixSpawn getInstance() {
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public Component getMessage(String path) {
        String raw = messages.getString(path, "");
        // Переводим HEX коды в компонент
        return LegacyComponentSerializer.legacySection().deserialize(raw);
    }
}