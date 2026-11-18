package dev.altair.WallixEvents.command;

import dev.altair.WallixEvents.WallixSpawn;
import dev.altair.WallixEvents.manager.EventManager;
import dev.altair.WallixEvents.model.WallixEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommand implements CommandExecutor {

    private final WallixSpawn plugin;

    public EventCommand(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    private void sendMessage(CommandSender sender, String path) {
        String msg = plugin.getConfig().getString("messages." + path, "&c[WallixSpawn] Ошибка сообщения.");
        sender.sendMessage(WallixSpawn.color(msg));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sendMessage(sender, "usage");
            return true;
        }

        EventManager manager = plugin.getEventManager();
        String sub = args[0].toLowerCase();

        switch (sub) {
            case "start":
                if (!sender.hasPermission("wallixspawn.event.start")) {
                    sendMessage(sender, "no-permission");
                    return true;
                }
                if (args.length < 2) {
                    sendMessage(sender, "usage-start");
                    return true;
                }
                String startName = args[1];
                WallixEvent startEvent = manager.getEvent(startName);
                if (startEvent == null) {
                    sendMessage(sender, "event-not-found");
                    return true;
                }
                if (startEvent.isRunning()) {
                    sendMessage(sender, "event-already-running");
                    return true;
                }
                startEvent.start();
                sendMessage(sender, "event-started");
                break;

            case "stop":
                if (!sender.hasPermission("wallixspawn.event.stop")) {
                    sendMessage(sender, "no-permission");
                    return true;
                }
                if (args.length < 2) {
                    sendMessage(sender, "usage-stop");
                    return true;
                }
                String stopName = args[1];
                WallixEvent stopEvent = manager.getEvent(stopName);
                if (stopEvent == null) {
                    sendMessage(sender, "event-not-found");
                    return true;
                }
                if (!stopEvent.isRunning()) {
                    sendMessage(sender, "event-not-running");
                    return true;
                }
                stopEvent.stop();
                sendMessage(sender, "event-stopped");
                break;

            case "restart":
                if (!sender.hasPermission("wallixspawn.event.restart")) {
                    sendMessage(sender, "no-permission");
                    return true;
                }
                if (args.length < 2) {
                    sendMessage(sender, "usage-restart");
                    return true;
                }
                String restartName = args[1];
                WallixEvent restartEvent = manager.getEvent(restartName);
                if (restartEvent == null) {
                    sendMessage(sender, "event-not-found");
                    return true;
                }
                restartEvent.restart();
                sendMessage(sender, "event-restarted");
                break;

            case "delay":
                if (!sender.hasPermission("wallixspawn.event.delay")) {
                    sendMessage(sender, "no-permission");
                    return true;
                }
                if (args.length < 3) {
                    sendMessage(sender, "usage-delay");
                    return true;
                }
                String delayName = args[1];
                WallixEvent delayEvent = manager.getEvent(delayName);
                if (delayEvent == null) {
                    sendMessage(sender, "event-not-found");
                    return true;
                }
                try {
                    long minutes = Long.parseLong(args[2]);
                    long seconds = args.length >= 4 ? Long.parseLong(args[3]) : 0L;
                    long total = minutes * 60 + seconds;
                    delayEvent.setDelaySeconds(total);
                    sendMessage(sender, "event-delay-set");
                } catch (NumberFormatException e) {
                    sendMessage(sender, "invalid-number");
                }
                break;

            case "reload":
                if (!sender.hasPermission("wallixspawn.event.reload")) {
                    sendMessage(sender, "no-permission");
                    return true;
                }
                plugin.reloadConfig();
                manager.reload();
                sendMessage(sender, "plugin-reloaded");
                break;

            default:
                sendMessage(sender, "usage");
                break;
        }
        return true;
    }
}