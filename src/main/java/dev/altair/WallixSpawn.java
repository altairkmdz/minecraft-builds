package dev.altair;

import dev.altair.command.SetSpawnCommand;
import dev.altair.command.SpawnCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Главный класс плагина WallixSpawn.
 */
public final class WallixSpawn extends JavaPlugin {

    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        // Сохраняем дефолтные файлы, если их ещё нет
        saveDefaultConfig();               // config.yml
        saveResource("messages.yml", false);
        saveResource("commands.yml", false);

        // Загружаем messages.yml
        File messagesFile = new File(getDataFolder(), "messages.yml");
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        // Регистрация команд
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
    }

    @Override
    public void onDisable() {
        // Ничего не требуется
    }

    /**
     * Возвращает конфиг сообщений.
     *
     * @return messages.yml
     */
    public FileConfiguration getMessages() {
        return messagesConfig;
    }
}