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
 * /setspawn – установить точку спавна.
 */
public class SetSpawnCommand implements CommandExecutor {

    private final WallixSpawn plugin;

    public SetSpawnCommand(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("&#FF5555Только игроки могут использовать эту команду."));
            return true;
        }

        if (!player.hasPermission("wallixspawn.setspawn")) {
            player.sendMessage(plugin.getMessage("no_permission"));
            return true;
        }

        Location loc = player.getLocation();
        SpawnManager.setSpawn(loc);
        plugin.saveConfig(); // сохраняем в config.yml
        player.sendMessage(plugin.getMessage("setspawn_success"));
        return true;
    }
}