package dev.altair;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class WallixSpawn extends JavaPlugin {
    private File messagesFile;
    private YamlConfiguration messagesConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadMessages();
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getCommand("spawn").setExecutor(new SpawnCommand());
    }

    private void loadMessages() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    private String getMessage(String key) {
        return messagesConfig.getString(key, "");
    }

    private class SetSpawnCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("wallixspawn.setspawn")) {
                player.sendMessage(getMessage("no-permission"));
                return true;
            }
            Location loc = player.getLocation();
            FileConfiguration cfg = getConfig();
            cfg.set("spawn.world", loc.getWorld().getName());
            cfg.set("spawn.x", loc.getX());
            cfg.set("spawn.y", loc.getY());
            cfg.set("spawn.z", loc.getZ());
            cfg.set("spawn.yaw", loc.getYaw());
            cfg.set("spawn.pitch", loc.getPitch());
            saveConfig();
            player.sendMessage(getMessage("spawn-set"));
            return true;
        }
    }

    private class SpawnCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            FileConfiguration cfg = getConfig();
            if (!cfg.isSet("spawn.world")) {
                player.sendMessage(getMessage("no-spawn"));
                return true;
            }
            World world = Bukkit.getWorld(cfg.getString("spawn.world"));
            if (world == null) {
                player.sendMessage(getMessage("no-spawn"));
                return true;
            }
            double x = cfg.getDouble("spawn.x");
            double y = cfg.getDouble("spawn.y");
            double z = cfg.getDouble("spawn.z");
            float yaw = (float) cfg.getDouble("spawn.yaw");
            float pitch = (float) cfg.getDouble("spawn.pitch");
            Location spawnLoc = new Location(world, x, y, z, yaw, pitch);
            player.teleport(spawnLoc);
            player.sendMessage(getMessage("teleport"));
            return true;
        }
    }
}
