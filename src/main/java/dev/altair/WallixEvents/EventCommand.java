package dev.altair.WallixEvents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventCommand implements CommandExecutor, TabCompleter {

    private final WallixEvents plugin;
    private final EventManager manager;

    public EventCommand(WallixEvents plugin, EventManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    private String translate(String path) {
        String raw = plugin.getMessagesConfig().getString(path, path);
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(translate("messages.usage"));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "start":
                if (!sender.hasPermission("wallixevents.event.start")) {
                    sender.sendMessage(translate("messages.no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(translate("messages.usage-start"));
                    return true;
                }
                manager.startEvent(args[1], sender);
                break;

            case "stop":
                if (!sender.hasPermission("wallixevents.event.stop")) {
                    sender.sendMessage(translate("messages.no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(translate("messages.usage-stop"));
                    return true;
                }
                manager.stopEvent(args[1], sender);
                break;

            case "restart":
                if (!sender.hasPermission("wallixevents.event.restart")) {
                    sender.sendMessage(translate("messages.no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(translate("messages.usage-restart"));
                    return true;
                }
                manager.restartEvent(args[1], sender);
                break;

            case "delay":
                if (!sender.hasPermission("wallixevents.event.delay")) {
                    sender.sendMessage(translate("messages.no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(translate("messages.usage-delay"));
                    return true;
                }
                manager.showDelay(args[1], sender);
                break;

            case "reload":
                if (!sender.hasPermission("wallixevents.event.reload")) {
                    sender.sendMessage(translate("messages.no-permission"));
                    return true;
                }
                plugin.reloadConfig();
                plugin.reloadConfig("messages.yml");
                plugin.reloadConfig("events.yml");
                plugin.reloadConfig("hologram.yml");
                manager.loadEvents();
                sender.sendMessage(translate("messages.reloaded"));
                break;

            default:
                sender.sendMessage(translate("messages.unknown-subcommand"));
                break;
        }

        return true;
    }

    // Simple tab completion for subcommands and event names
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subs = List.of("start", "stop", "restart", "delay", "reload");
            List<String> result = new ArrayList<>();
            for (String s : subs) {
                if (s.startsWith(args[0].toLowerCase())) result.add(s);
            }
            return result;
        }

        if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            return manager.getEventNamesStartingWith(args[1]);
        }

        return Collections.emptyList();
    }
}