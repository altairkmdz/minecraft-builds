package dev.altair.command;

import dev.altair.WallixSpawn;
import dev.altair.manager.SpawnManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /spawn – телепортация к установленному спавну.
 */
public class SpawnCommand implements CommandExecutor {

    private final WallixSpawn plugin;

    public SpawnCommand(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("&#FF5555Только игроки могут использовать эту команду."));
            return true;
        }

        if (!player.hasPermission("wallixspawn.spawn")) {
            player.sendMessage(plugin.getMessage("no_permission"));
            return true;
        }

        Location spawn = SpawnManager.getSpawn();
        if (spawn == null) {
            player.sendMessage(plugin.getMessage("no_spawn_set"));
            return true;
        }

        player.teleport(spawn);
        player.sendMessage(plugin.getMessage("spawn_teleport"));
        return true;
    }
}