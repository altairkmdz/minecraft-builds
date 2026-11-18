package dev.altair.WallixEvents.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

/**
 * Утилита для перевода строк с цветами в формат, поддерживаемый Paper.
 * Поддерживает как обычные & коды, так и HEX коды вида &#RRGGBB.
 */
public final class ColorUtil {

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    private ColorUtil() {}

    /**
     * Преобразует строку с цветами в готовый к отправке игроку компонент.
     *
     * @param input строка с & и &#HEX кодами
     * @return уже готовый к отправке компонент
     */
    public static String translate(String input) {
        if (input == null) return "";
        // Преобразуем &#RRGGBB в &x&R&R&G&G&B&B (формат, который понимает Bukkit)
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (c == '&' && i + 7 < input.length() && input.charAt(i + 1) == '#') {
                // найдено &#RRGGBB
                String hex = input.substring(i + 2, i + 8);
                sb.append("&x");
                for (char h : hex.toCharArray()) {
                    sb.append('&').append(h);
                }
                i += 8; // пропускаем &#RRGGBB
            } else {
                sb.append(c);
                i++;
            }
        }
        // Теперь переводим обычные & коды
        String withLegacy = ChatColor.translateAlternateColorCodes('&', sb.toString());
        // И, если нужен Adventure‑компонент, можно использовать LegacyComponentSerializer
        return LEGACY_SERIALIZER.serialize(LEGACY_SERIALIZER.deserialize(withLegacy));
    }
}