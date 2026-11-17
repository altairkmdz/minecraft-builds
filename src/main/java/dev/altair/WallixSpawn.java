package dev.altair;

import dev.altair.command.SetSpawnCommand;
import dev.altair.command.SpawnCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class WallixSpawn extends JavaPlugin {

    @Override
    public void onEnable() {
        // Сохраняем файлы конфигураций, если их ещё нет
        saveDefaultConfig();
        saveResource("messages.yml", false);
        saveResource("commands.yml", false);

        // Регистрируем команды
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
    }

    @Override
    public void onDisable() {
        // Ничего не требуется
    }
}