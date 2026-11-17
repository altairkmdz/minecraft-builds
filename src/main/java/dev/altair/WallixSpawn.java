package dev.altair;

import dev.altair.command.SetSpawnCommand;
import dev.altair.command.SpawnCommand;
import dev.altair.manager.SpawnManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public final class WallixSpawn extends JavaPlugin {

    private YamlConfiguration messages;

    @Override
    public void onEnable() {
        // Сохраняем дефолтные файлы, если их ещё нет
        saveDefaultConfig();
        saveResource("messages.yml", false);
        saveResource("commands.yml", false);

        // Загружаем messages.yml
        File messagesFile = new File(getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        // Регистрируем команды
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));

        // Инициализируем менеджер спавна (читаем из config)
        SpawnManager.load(this);
        getLogger().log(Level.INFO, "WallixSpawn включён!");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "WallixSpawn выключен!");
    }

    /**
     * Получить сообщение из messages.yml с поддержкой HEX‑цветов.
     *
     * @param path путь к сообщению в файле
     * @return компонент, готовый к отправке игроку
     */
    public net.kyori.adventure.text.Component getMessage(String path) {
        String raw = messages.getString(path, "§c[WallixSpawn] Ошибка конфигурации: " + path);
        // Десериализуем строку, где поддерживаются HEX‑коды вида &#RRGGBB
        return LegacyComponentSerializer.legacySection().deserialize(raw);
    }
}