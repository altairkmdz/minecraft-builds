package dev.altair.command;

import dev.altair.WallixSpawn;
import dev.altair.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Команда /spawn – телепортирует игрока на сохранённый спавн.
 */
public class SpawnCommand implements CommandExecutor {

    private final WallixSpawn plugin;

    public SpawnCommand(WallixSpawn plugin) {
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

        // Читаем данные спавна из config.yml
        String worldName = plugin.getConfig().getString("spawn.world");
        if (worldName == null) {
            player.sendMessage(MessageUtil.color(plugin.getMessages().getString("messages.spawn-not-set",
                    "&#FF5555Точка спавна ещё не установлена!")));
            return true;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(MessageUtil.color(plugin.getMessages().getString("messages.spawn-not-set",
                    "&#FF5555Точка спавна указана в несуществующем мире!")));
            return true;
        }

        double x = plugin.getConfig().getDouble("spawn.x");
        double y = plugin.getConfig().getDouble("spawn.y");
        double z = plugin.getConfig().getDouble("spawn.z");
        float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
        float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");

        Location spawnLocation = new Location(world, x, y, z, yaw, pitch);
        player.teleport(spawnLocation);

        player.sendMessage(MessageUtil.color(plugin.getMessages().getString("messages.spawn-teleport",
                "&#55FF55Вы телепортированы на спавн!")));
        return true;
    }
}