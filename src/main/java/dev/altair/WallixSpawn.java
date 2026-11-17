package dev.altair;

import dev.altair.command.SetSpawnCommand;
import dev.altair.command.SpawnCommand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.File;
import java.io.IOException;

public final class WallixSpawn extends JavaPlugin {

    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        // Создаём/загружаем конфиги
        saveDefaultConfig(); // config.yml
        saveResource("messages.yml", false);
        saveResource("commands.yml", false);
        loadMessages();

        // Регистрация команд
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
    }

    @Override
    public void onDisable() {
        // ничего не требуется
    }

    /**
     * Загружает messages.yml в отдельный FileConfiguration.
     */
    public void loadMessages() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    /**
     * Возвращает сообщение из messages.yml с поддержкой HEX‑цветов.
     *
     * @param path путь к сообщению в messages.yml
     * @return готовый к отправке Component
     */
    public net.kyori.adventure.text.Component getMessage(String path) {
        String raw = messagesConfig.getString(path, "");
        // Переводим цветовые коды (&) и поддерживаем HEX (&#RRGGBB)
        String colored = ChatColor.translateAlternateColorCodes('&', raw);
        return LegacyComponentSerializer.legacySection().deserialize(colored);
    }

    /**
     * Сохраняет messages.yml (если понадобится динамически менять сообщения).
     */
    public void saveMessages() {
        try {
            messagesConfig.save(new File(getDataFolder(), "messages.yml"));
        } catch (IOException e) {
            getLogger().severe("Не удалось сохранить messages.yml: " + e.getMessage());
        }
    }
}