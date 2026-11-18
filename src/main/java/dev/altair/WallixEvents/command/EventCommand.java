package dev.altair.WallixEvents.command;

import dev.altair.WallixEvents.WallixEventsPlugin;
import dev.altair.WallixEvents.event.Event;
import dev.altair.WallixEvents.event.EventManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class EventCommand implements CommandExecutor {

    private final WallixEventsPlugin plugin;
    private final EventManager manager;
    private final Component prefix;

    public EventCommand(WallixEventsPlugin plugin) {
        this.plugin = plugin;
        this.manager = plugin.getEventManager();
        this.prefix = LegacyComponentSerializer.legacySection()
                .deserialize(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("messages.prefix", "&6[&bWallixEvents&6] ")));
    }

    private Component translate(String path, Object... args) {
        String raw = plugin.getConfig().getString("messages." + path, path);
        raw = String.format(raw, args);
        raw = ChatColor.translateAlternateColorCodes('&', raw);
        return LegacyComponentSerializer.legacySection().deserialize(raw);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(prefix.append(translate("usage")));
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "start":
                if (!sender.hasPermission("wallixevents.event.start")) {
                    sender.sendMessage(prefix.append(translate("no-permission")));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(prefix.append(translate("specify-name")));
                    return true;
                }
                String startName = args[1];
                Event startEvent = manager.getEvent(startName);
                if (startEvent == null) {
                    sender.sendMessage(prefix.append(translate("event-not-found", startName)));
                    return true;
                }
                startEvent.start();
                sender.sendMessage(prefix.append(translate("event-started", startName)));
                break;

            case "stop":
                if (!sender.hasPermission("wallixevents.event.stop")) {
                    sender.sendMessage(prefix.append(translate("no-permission")));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(prefix.append(translate("specify-name")));
                    return true;
                }
                String stopName = args[1];
                Event stopEvent = manager.getEvent(stopName);
                if (stopEvent == null) {
                    sender.sendMessage(prefix.append(translate("event-not-found", stopName)));
                    return true;
                }
                stopEvent.stop();
                sender.sendMessage(prefix.append(translate("event-stopped", stopName)));
                break;

            case "restart":
                if (!sender.hasPermission("wallixevents.event.restart")) {
                    sender.sendMessage(prefix.append(translate("no-permission")));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(prefix.append(translate("specify-name")));
                    return true;
                }
                String restartName = args[1];
                Event restartEvent = manager.getEvent(restartName);
                if (restartEvent == null) {
                    sender.sendMessage(prefix.append(translate("event-not-found", restartName)));
                    return true;
                }
                restartEvent.restart();
                sender.sendMessage(prefix.append(translate("event-restarted", restartName)));
                break;

            case "delay":
                if (!sender.hasPermission("wallixevents.event.delay")) {
                    sender.sendMessage(prefix.append(translate("no-permission")));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(prefix.append(translate("usage-delay")));
                    return true;
                }
                String delayName = args[1];
                Event delayEvent = manager.getEvent(delayName);
                if (delayEvent == null) {
                    sender.sendMessage(prefix.append(translate("event-not-found", delayName)));
                    return true;
                }
                try {
                    long seconds = Long.parseLong(args[2]);
                    delayEvent.setDelay(seconds);
                    sender.sendMessage(prefix.append(translate("event-delay-set", delayName, seconds / 60, seconds % 60)));
                } catch (NumberFormatException e) {
                    sender.sendMessage(prefix.append(translate("invalid-number")));
                }
                break;

            case "reload":
                if (!sender.hasPermission("wallixevents.event.reload")) {
                    sender.sendMessage(prefix.append(translate("no-permission")));
                    return true;
                }
                plugin.reloadConfig();
                manager.reloadEvents();
                sender.sendMessage(prefix.append(translate("reloaded")));
                break;

            default:
                sender.sendMessage(prefix.append(translate("usage")));
                break;
        }

        return true;
    }
}