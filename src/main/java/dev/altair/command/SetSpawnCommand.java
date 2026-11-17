package dev.altair.command;

import dev.altair.WallixSpawn;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class SetSpawnCommand implements CommandExecutor {

    private final WallixSpawn plugin;
    private static final Permission SETSPAWN_PERMISSION = new Permission("wallixspawn.setspawn");

    public SetSpawnCommand(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может выполнить только игрок.");
            return true;
        }

        if (!player.hasPermission(SETSPAWN_PERMISSION)) {
            player.sendMessage(plugin.getMessage("no_permission"));
            return true;
        }

        Location loc = player.getLocation();

        plugin.getConfig().set("spawn.world", loc.getWorld().getName());
        plugin.getConfig().set("spawn.x", loc.getX());
        plugin.getConfig().set("spawn.y", loc.getY());
        plugin.getConfig().set("spawn.z", loc.getZ());
        plugin.getConfig().set("spawn.yaw", loc.getYaw());
        plugin.getConfig().set("spawn.pitch", loc.getPitch());
        plugin.saveConfig();

        player.sendMessage(plugin.getMessage("spawn_set"));
        return true;
    }
}