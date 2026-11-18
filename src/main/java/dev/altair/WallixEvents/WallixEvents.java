package dev.altair.WallixEvents;

import dev.altair.WallixEvents.command.EventCommand;
import dev.altair.WallixEvents.manager.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WallixEvents extends JavaPlugin {

    private static WallixEvents instance;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;

        // Сохраняем дефолтные файлы, если их ещё нет
        saveDefaultConfig();          // config.yml
        saveResource("messages.yml", false);
        saveResource("events.yml", false);

        // Инициализируем менеджер ивентов
        this.eventManager = new EventManager(this);
        this.eventManager.loadEvents();

        // Регистрируем команду /event
        this.getCommand("event").setExecutor(new EventCommand(this));
        this.getCommand("event").setTabCompleter(new EventCommand(this));

        getLogger().info("WallixEvents включён!");
    }

    @Override
    public void onDisable() {
        // Останавливаем все запущенные ивенты и удаляем голограммы
        if (eventManager != null) {
            eventManager.stopAllEvents();
        }
        getLogger().info("WallixEvents выключен!");
    }

    public static WallixEvents getInstance() {
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}