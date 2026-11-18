package dev.altair.WallixEvents.event;

import dev.altair.WallixEvents.WallixEventsPlugin;
import dev.altair.WallixEvents.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Пример простого листенера. При входе игрока выводим приветственное сообщение
 * и показываем голограммы (если они находятся в радиусе).
 */
public class EventListener implements Listener {

    private final WallixEventsPlugin plugin;

    public EventListener(WallixEventsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(ChatUtil.translate("&aДобро пожаловать на сервер, &e" + e.getPlayer().getName() + "&a!"));
        // При желании можно добавить логику отображения голограмм только игрокам в определённом мире/регионе.
    }
}