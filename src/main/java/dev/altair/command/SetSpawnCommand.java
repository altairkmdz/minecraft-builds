package dev.altair.command;

import dev.altair.WallixSpawn;
import dev.altair.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Команда /setspawn – сохраняет текущую позицию игрока как точку спавна.
 */
public class SetSpawnCommand implements CommandExecutor {

    private final WallixSpawn plugin;

    public SetSpawnCommand(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверка, что отправитель – игрок
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtil.color(plugin.getMessages().getString("messages.only-player",
                    "&#FF5555Эту команду может выполнить только игрок!")));
            return true;
        }

        // Проверка прав
        if (!player.hasPermission("wallixspawn.setspawn")) {
            player.sendMessage(MessageUtil.color(plugin.getMessages().getString("messages.no-permission",
                    "&#FF5555У вас нет прав для выполнения этой команды!")));
            return true;
        }

        // Сохраняем координаты в config.yml
        Location loc = player.getLocation();
        plugin.getConfig().set("spawn.world", loc.getWorld().getName());
        plugin.getConfig().set("spawn.x", loc.getX());
        plugin.getConfig().set("spawn.y", loc.getY());
        plugin.getConfig().set("spawn.z", loc.getZ());
        plugin.getConfig().set("spawn.yaw", loc.getYaw());
        plugin.getConfig().set("spawn.pitch", loc.getPitch());
        plugin.saveConfig();

        // Сообщение об успехе
        player.sendMessage(MessageUtil.color(plugin.getMessages().getString("messages.setspawn-success",
                "&#55FF55Точка спавна успешно установлена!")));
        return true;
    }
}