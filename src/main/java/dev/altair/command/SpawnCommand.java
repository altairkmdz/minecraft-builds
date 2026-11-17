package dev.altair.command;

import dev.altair.WallixSpawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class SpawnCommand implements CommandExecutor {

    private final WallixSpawn plugin;
    private static final Permission SPAWN_PERMISSION = new Permission("wallixspawn.spawn");

    public SpawnCommand(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может выполнить только игрок.");
            return true;
        }

        if (!player.hasPermission(SPAWN_PERMISSION)) {
            player.sendMessage(plugin.getMessage("no_permission"));
            return true;
        }

        if (!plugin.getConfig().isSet("spawn.world")) {
            player.sendMessage(plugin.getMessage("spawn_not_set"));
            return true;
        }

        String worldName = plugin.getConfig().getString("spawn.world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(plugin.getMessage("spawn_not_set"));
            return true;
        }

        double x = plugin.getConfig().getDouble("spawn.x");
        double y = plugin.getConfig().getDouble("spawn.y");
        double z = plugin.getConfig().getDouble("spawn.z");
        float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
        float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");

        Location spawnLocation = new Location(world, x, y, z, yaw, pitch);
        player.teleport(spawnLocation);
        player.sendMessage(plugin.getMessage("spawn_teleport"));
        return true;
    }
}