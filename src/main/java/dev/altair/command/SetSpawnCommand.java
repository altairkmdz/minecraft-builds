package dev.altair.command;

import dev.altair.WallixSpawn;
import dev.altair.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final WallixSpawn plugin;

    public SetSpawnCommand(WallixSpawn plugin) {
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

        if (!player.hasPermission("wallixspawn.setspawn")) {
            player.sendMessage(LegacyComponentSerializer.builder()
                    .character('&')
                    .hexColors()
                    .build()
                    .deserialize(MessageUtil.getMessage(plugin, "messages.no_permission")));
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

        player.sendMessage(LegacyComponentSerializer.builder()
                .character('&')
                .hexColors()
                .build()
                .deserialize(MessageUtil.getMessage(plugin, "messages.spawn_set")));
        return true;
    }
}