package dev.altair.WallixEvents.command;

import dev.altair.WallixEvents.WallixSpawn;
import dev.altair.WallixEvents.event.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventCommand implements CommandExecutor, TabCompleter {

    private final WallixSpawn plugin;
    private final FileConfiguration messages;

    public EventCommand(WallixSpawn plugin) {
        this.plugin = plugin;
        this.messages = plugin.getConfig(); // messages.yml будет загружен в основной конфиг
    }

    private String translate(String path) {
        String raw = plugin.getConfig().getString("messages." + path, "&c[Missing message: " + path + "]");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(translate("prefix") + " " + translate("only-player"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(translate("usage"));
            return true;
        }

        EventManager manager = plugin.getEventManager();

        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (!player.hasPermission("wallixspawn.event.start")) {
                    player.sendMessage(translate("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(translate("usage-start"));
                    return true;
                }
                String name = args[1];
                manager.startEvent(name, player);
            }
            case "stop" -> {
                if (!player.hasPermission("wallixspawn.event.stop")) {
                    player.sendMessage(translate("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(translate("usage-stop"));
                    return true;
                }
                String name = args[1];
                manager.stopEvent(name, player);
            }
            case "restart" -> {
                if (!player.hasPermission("wallixspawn.event.restart")) {
                    player.sendMessage(translate("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(translate("usage-restart"));
                    return true;
                }
                String name = args[1];
                manager.restartEvent(name, player);
            }
            case "delay" -> {
                if (!player.hasPermission("wallixspawn.event.delay")) {
                    player.sendMessage(translate("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(translate("usage-delay"));
                    return true;
                }
                String name = args[1];
                manager.sendDelayInfo(name, player);
            }
            case "reload" -> {
                if (!player.hasPermission("wallixspawn.event.reload")) {
                    player.sendMessage(translate("no-permission"));
                    return true;
                }
                plugin.reloadConfig();
                plugin.saveResource("messages.yml", true);
                plugin.saveResource("events.yml", true);
                plugin.saveResource("hologram.yml", true);
                plugin.getHologramManager().removeAll();
                plugin.getHologramManager().loadHolograms();
                player.sendMessage(translate("reload-success"));
            }
            default -> player.sendMessage(translate("usage"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("start");
            completions.add("stop");
            completions.add("restart");
            completions.add("delay");
            completions.add("reload");
            return completions;
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("start")
                || args[0].equalsIgnoreCase("stop")
                || args[0].equalsIgnoreCase("restart")
                || args[0].equalsIgnoreCase("delay"))) {
            // Предлагаем названия событий из events.yml
            completions.addAll(plugin.getEventManager().getAllEventNames());
        }

        return completions;
    }
}