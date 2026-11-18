package dev.altair.WallixEvents;

import dev.altair.WallixEvents.command.EventCommand;
import dev.altair.WallixEvents.event.EventManager;
import dev.altair.WallixEvents.hologram.HologramManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WallixSpawn extends JavaPlugin {

    private EventManager eventManager;
    private HologramManager hologramManager;

    @Override
    public void onEnable() {
        // Сохраняем дефолтные файлы, если их ещё нет
        saveDefaultConfig(); // config.yml
        saveResource("events.yml", false);
        saveResource("messages.yml", false);
        saveResource("hologram.yml", false);

        // Инициализируем менеджеры
        this.eventManager = new EventManager(this);
        this.hologramManager = new HologramManager(this);
        this.hologramManager.loadAndSpawn();

        // Регистрируем команду
        this.getCommand("event").setExecutor(new EventCommand(this));
        this.getCommand("event").setTabCompleter(new EventCommand(this));

        // Выводим в консоль сообщение о загрузке
        getServer().getConsoleSender().sendMessage(colorize("&a[WallixSpawn] Плагин успешно загружен!"));
    }

    @Override
    public void onDisable() {
        // Останавливаем все запущенные ивенты
        eventManager.stopAll();
        // Удаляем голограммы
        hologramManager.removeAll();
        getServer().getConsoleSender().sendMessage(colorize("&c[WallixSpawn] Плагин отключён."));
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    /**
     * Преобразует строку с цветами (включая HEX) в Component.
     */
    public static Component colorize(String message) {
        // LegacyComponentSerializer поддерживает &#RRGGBB
        return LegacyComponentSerializer.legacySection().deserialize(message);
    }
}