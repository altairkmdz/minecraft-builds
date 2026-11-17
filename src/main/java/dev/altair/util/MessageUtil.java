package dev.altair.util;

import dev.altair.WallixSpawn;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageUtil {

    /**
     * Получает сообщение из messages.yml и подставляет префикс, если он задан.
     *
     * @param plugin  основной класс плагина
     * @param path    путь к сообщению в messages.yml
     * @return готовая строка сообщения
     */
    public static String getMessage(WallixSpawn plugin, String path) {
        FileConfiguration messages = plugin.getConfig("messages.yml");
        String raw = messages.getString(path, "&c[WallixSpawn] Ошибка сообщения");
        // При необходимости можно добавить замену плейсхолдеров здесь
        return raw;
    }
}