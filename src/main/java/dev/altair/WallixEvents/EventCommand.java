package dev.altair.WallixEvents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventCommand implements CommandExecutor, TabCompleter {

    private final WallixSpawn plugin;

    public EventCommand(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    private Component prefix() {
        return plugin.getMessage("prefix");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(prefix().append(plugin.getMessage("usage")));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "start":
                return handleStart(sender, args);
            case "stop":
                return handleStop(sender, args);
            case "restart":
                return handleRestart(sender, args);
            case "delay":
                return handleDelay(sender, args);
            case "reload":
                return handleReload(sender);
            default:
                sender.sendMessage(prefix().append(plugin.getMessage("unknown-command")));
                return true;
        }
    }

    private boolean handleStart(CommandSender sender, String[] args) {
        if (!sender.hasPermission("wallixspawn.event.start")) {
            sender.sendMessage(prefix().append(plugin.getMessage("no-permission")));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(prefix().append(plugin.getMessage("usage-start")));
            return true;
        }
        String name = args[1];
        if (plugin.getEventManager().startEvent(name)) {
            sender.sendMessage(prefix().append(
                    plugin.getMessage("event-started").replaceText(
                            builder -> builder.matchLiteral("{event}").replacement(name))));
        } else {
            sender.sendMessage(prefix().append(
                    plugin.getMessage("event-not-found").replaceText(
                            builder -> builder.matchLiteral("{event}").replacement(name))));
        }
        return true;
    }

    private boolean handleStop(CommandSender sender, String[] args) {
        if (!sender.hasPermission("wallixspawn.event.stop")) {
            sender.sendMessage(prefix().append(plugin.getMessage("no-permission")));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(prefix().append(plugin.getMessage("usage-stop")));
            return true;
        }
        String name = args[1];
        if (plugin.getEventManager().stopEvent(name)) {
            sender.sendMessage(prefix().append(
                    plugin.getMessage("event-stopped").replaceText(
                            builder -> builder.matchLiteral("{event}").replacement(name))));
        } else {
            sender.sendMessage(prefix().append(
                    plugin.getMessage("event-not-found").replaceText(
                            builder -> builder.matchLiteral("{event}").replacement(name))));
        }
        return true;
    }

    private boolean handleRestart(CommandSender sender, String[] args) {
        if (!sender.hasPermission("wallixspawn.event.restart")) {
            sender.sendMessage(prefix().append(plugin.getMessage("no-permission")));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(prefix().append(plugin.getMessage("usage-restart")));
            return true;
        }
        String name = args[1];
        if (plugin.getEventManager().restartEvent(name)) {
            sender.sendMessage(prefix().append(
                    plugin.getMessage("event-restarted").replaceText(
                            builder -> builder.matchLiteral("{event}").replacement(name))));
        } else {
            sender.sendMessage(prefix().append(
                    plugin.getMessage("event-not-found").replaceText(
                            builder -> builder.matchLiteral("{event}").replacement(name))));
        }
        return true;
    }

    private boolean handleDelay(CommandSender sender, String[] args) {
        if (!sender.hasPermission("wallixspawn.event.delay")) {
            sender.sendMessage(prefix().append(plugin.getMessage("no-permission")));
            return true;
        }
        if (args.length < 4) {
            sender.sendMessage(prefix().append(plugin.getMessage("usage-delay")));
            return true;
        }
        String name = args[1];
        int minutes, seconds;
        try {
            minutes = Integer.parseInt(args[2]);
            seconds = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(prefix().append(plugin.getMessage("invalid-time")));
            return true;
        }
        long totalTicks = (minutes * 60L + seconds) * 20L;
        if (plugin.getEventManager().scheduleEvent(name, totalTicks)) {
            Component msg = plugin.getMessage("event-delay-set")
                    .replaceText(builder -> builder.matchLiteral("{event}").replacement(name))
                    .replaceText(builder -> builder.matchLiteral("{minutes}").replacement(String.valueOf(minutes)))
                    .replaceText(builder -> builder.matchLiteral("{seconds}").replacement(String.valueOf(seconds)));
            sender.sendMessage(prefix().append(msg));
        } else {
            sender.sendMessage(prefix().append(
                    plugin.getMessage("event-not-found").replaceText(
                            builder -> builder.matchLiteral("{event}").replacement(name))));
        }
        return true;
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("wallixspawn.event.reload")) {
            sender.sendMessage(prefix().append(plugin.getMessage("no-permission")));
            return true;
        }
        plugin.reloadConfigs();
        sender.sendMessage(prefix().append(plugin.getMessage("plugin-reloaded")));
        return true;
    }

    // -------------------- TAB COMPLETION --------------------
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(List.of("start", "stop", "restart", "delay", "reload"));
            return completions;
        }

        if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            completions.addAll(plugin.getEventManager().getAllEventNames());
            return completions;
        }

        return completions;
    }
}