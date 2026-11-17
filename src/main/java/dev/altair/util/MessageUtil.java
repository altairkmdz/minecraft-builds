package dev.altair.util;

import org.bukkit.ChatColor;

/**
 * Утилита для перевода цветовых кодов в сообщения.
 */
public final class MessageUtil {

    private MessageUtil() {
        // Утилитный класс
    }

    /**
     * Преобразует строку с кодами вида &#FF0000 в цветные сообщения.
     *
     * @param message строка с кодами
     * @return цветное сообщение
     */
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}