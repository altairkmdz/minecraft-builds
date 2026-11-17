package dev.altair.command;

import dev.altair.WallixSpawn;
import dev.altair.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final WallixSpawn plugin;

    public SpawnCommand(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LegacyComponentSerializer.builder()
                    .character('&')
                    .hexColors()
                    .build()
                    .deserialize(MessageUtil.getMessage(plugin, "messages.only_players")));
            return true;
        }

        if (!plugin.getConfig().contains("spawn.world")) {
            player.sendMessage(LegacyComponentSerializer.builder()
                    .character('&')
                    .hexColors()
                    .build()
                    .deserialize(MessageUtil.getMessage(plugin, "messages.no_spawn_set")));
            return true;
        }

        String worldName = plugin.getConfig().getString("spawn.world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(LegacyComponentSerializer.builder()
                    .character('&')
                    .hexColors()
                    .build()
                    .deserialize(MessageUtil.getMessage(plugin, "messages.world_not_found")));
            return true;
        }

        double x = plugin.getConfig().getDouble("spawn.x");
        double y = plugin.getConfig().getDouble("spawn.y");
        double z = plugin.getConfig().getDouble("spawn.z");
        float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
        float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");

        Location spawnLocation = new Location(world, x, y, z, yaw, pitch);
        player.teleport(spawnLocation);

        player.sendMessage(LegacyComponentSerializer.builder()
                .character('&')
                .hexColors()
                .build()
                .deserialize(MessageUtil.getMessage(plugin, "messages.teleport_success")));
        return true;
    }
}