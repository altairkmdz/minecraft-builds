package dev.altair.WallixEvents;

import dev.altair.WallixEvents.command.EventCommand;
import dev.altair.WallixEvents.event.EventManager;
import dev.altair.WallixEvents.hologram.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class WallixSpawn extends JavaPlugin {

    private EventManager eventManager;
    private HologramManager hologramManager;

    @Override
    public void onEnable() {
        // Сохраняем дефолтные файлы конфигураций, если их ещё нет
        saveDefaultConfig();
        saveResource("messages.yml", false);
        saveResource("events.yml", false);
        saveResource("hologram.yml", false);

        // Инициализируем менеджеры
        this.eventManager = new EventManager(this);
        this.hologramManager = new HologramManager(this);

        // Регистрируем команду /event
        this.getCommand("event").setExecutor(new EventCommand(this));
        this.getCommand("event").setTabCompleter(new EventCommand(this));

        // Загружаем голограммы
        hologramManager.loadHolograms();

        getLogger().info(ChatColor.translateAlternateColorCodes('&',
                "&aПлагин WallixSpawn успешно загружен!"));
    }

    @Override
    public void onDisable() {
        // Удаляем все голограммы при выключении сервера
        hologramManager.removeAll();
        getLogger().info(ChatColor.translateAlternateColorCodes('&',
                "&cПлагин WallixSpawn выключен."));
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }
}