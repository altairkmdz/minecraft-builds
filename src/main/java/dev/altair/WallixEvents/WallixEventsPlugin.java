package dev.altair.WallixEvents;

import dev.altair.WallixEvents.command.EventCommand;
import dev.altair.WallixEvents.manager.EventManager;
import dev.altair.WallixEvents.manager.HologramManager;
import dev.altair.WallixEvents.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WallixEventsPlugin extends JavaPlugin {

    private static WallixEventsPlugin instance;
    private EventManager eventManager;
    private HologramManager hologramManager;

    @Override
    public void onEnable() {
        instance = this;

        // Сохраняем дефолтные файлы, если их ещё нет
        saveDefaultConfig();          // config.yml
        saveResource("events.yml", false);
        saveResource("messages.yml", false);
        saveResource("hologram.yml", false);

        // Инициализируем менеджеры
        this.eventManager = new EventManager(this);
        this.hologramManager = new HologramManager(this);

        // Регистрируем команду /event
        this.getCommand("event").setExecutor(new EventCommand(this));

        // Регистрируем простой листенер (можно расширить под нужды сервера)
        Bukkit.getPluginManager().registerEvents(new dev.altair.WallixEvents.event.EventListener(this), this);

        // Загружаем голограммы
        hologramManager.loadHolograms();

        getLogger().info(ChatUtil.translate("&aПлагин WallixEvents успешно загружен!"));
    }

    @Override
    public void onDisable() {
        // Удаляем все голограммы
        hologramManager.removeAll();
        // Останавливаем все запущенные ивенты
        eventManager.stopAll();
        getLogger().info(ChatUtil.translate("&cПлагин WallixEvents отключён."));
    }

    public static WallixEventsPlugin getInstance() {
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }
}