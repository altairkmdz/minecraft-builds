package dev.altair.WallixEvents;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Главный класс плагина WallixSpawn.
 * Совместим с Paper 1.19.4, Java 17.
 */
public final class WallixSpawn extends JavaPlugin {

    private EventManager eventManager;
    private HologramManager hologramManager;

    @Override
    public void onEnable() {
        // Создаём/обновляем конфиги, если их ещё нет
        saveDefaultConfig();
        saveResource("events.yml", false);
        saveResource("messages.yml", false);
        saveResource("hologram.yml", false);

        // Инициализируем менеджеры
        this.hologramManager = new HologramManager(this);
        this.eventManager = new EventManager(this, hologramManager);

        // Регистрируем команду /event
        if (getCommand("event") != null) {
            EventCommand executor = new EventCommand(this, eventManager);
            getCommand("event").setExecutor(executor);
            getCommand("event").setTabCompleter(executor);
        }

        getLogger().info("WallixSpawn включён!");
    }

    @Override
    public void onDisable() {
        // Останавливаем все запущенные ивенты и удаляем голограммы
        eventManager.stopAllEvents();
        hologramManager.removeAll();
        getLogger().info("WallixSpawn выключен!");
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }
}