package dev.altair.WallixEvents.command;

import dev.altair.WallixEvents.WallixEvents;
import dev.altair.WallixEvents.manager.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventCommand implements CommandExecutor, TabCompleter {

    private final WallixEvents plugin;
    private final EventManager manager;

    public EventCommand(WallixEvents plugin) {
        this.plugin = plugin;
        this.manager = plugin.getEventManager();
    }

    private String getMessage(String path) {
        String raw = plugin.getConfig().getString("messages." + path, "&c[WallixEvents] Ошибка в конфиге!");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage("prefix") + " " + getMessage("only-player"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(getMessage("prefix") + " " + getMessage("usage"));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "start":
                if (!player.hasPermission("wallixevents.event.start")) {
                    player.sendMessage(getMessage("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(getMessage("prefix") + " " + getMessage("usage-start"));
                    return true;
                }
                manager.startEvent(args[1], player);
                break;

            case "stop":
                if (!player.hasPermission("wallixevents.event.stop")) {
                    player.sendMessage(getMessage("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(getMessage("prefix") + " " + getMessage("usage-stop"));
                    return true;
                }
                manager.stopEvent(args[1], player);
                break;

            case "restart":
                if (!player.hasPermission("wallixevents.event.restart")) {
                    player.sendMessage(getMessage("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(getMessage("prefix") + " " + getMessage("usage-restart"));
                    return true;
                }
                manager.restartEvent(args[1], player);
                break;

            case "delay":
                if (!player.hasPermission("wallixevents.event.delay")) {
                    player.sendMessage(getMessage("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(getMessage("prefix") + " " + getMessage("usage-delay"));
                    return true;
                }
                manager.showDelay(args[1], player);
                break;

            case "reload":
                if (!player.hasPermission("wallixevents.event.reload")) {
                    player.sendMessage(getMessage("no-permission"));
                    return true;
                }
                plugin.reloadConfig();
                plugin.saveResource("messages.yml", true);
                plugin.saveResource("events.yml", true);
                manager.loadEvents();
                player.sendMessage(getMessage("prefix") + " " + getMessage("reloaded"));
                break;

            default:
                player.sendMessage(getMessage("prefix") + " " + getMessage("usage"));
                break;
        }

        return true;
    }

    // Tab completion
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("start", "stop", "restart", "delay", "reload");
        }

        if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            return new ArrayList<>(manager.getAllEventNames());
        }

        return Collections.emptyList();
    }
}