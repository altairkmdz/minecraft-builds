package dev.altair.WallixEvents.command;

import dev.altair.WallixEvents.WallixEventsPlugin;
import dev.altair.WallixEvents.manager.EventManager;
import dev.altair.WallixEvents.util.ChatUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventCommand implements TabExecutor {

    private final WallixEventsPlugin plugin;
    private final EventManager eventManager;

    public EventCommand(WallixEventsPlugin plugin) {
        this.plugin = plugin;
        this.eventManager = plugin.getEventManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtil.translate("&cЭту команду можно выполнять только в игре."));
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "start" -> {
                if (!player.hasPermission("wallixevents.start")) {
                    player.sendMessage(ChatUtil.translate("&cУ вас нет прав для запуска ивентов."));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(ChatUtil.translate("&cИспользование: /event start <название>"));
                    return true;
                }
                String name = args[1];
                eventManager.startEvent(name);
                player.sendMessage(ChatUtil.translate("&aИвент '&e" + name + "&a' запущен."));
            }
            case "stop" -> {
                if (!player.hasPermission("wallixevents.stop")) {
                    player.sendMessage(ChatUtil.translate("&cУ вас нет прав для остановки ивентов."));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(ChatUtil.translate("&cИспользование: /event stop <название>"));
                    return true;
                }
                String name = args[1];
                eventManager.stopEvent(name);
                player.sendMessage(ChatUtil.translate("&aИвент '&e" + name + "&a' остановлен."));
            }
            case "restart" -> {
                if (!player.hasPermission("wallixevents.restart")) {
                    player.sendMessage(ChatUtil.translate("&cУ вас нет прав для перезапуска ивентов."));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(ChatUtil.translate("&cИспользование: /event restart <название>"));
                    return true;
                }
                String name = args[1];
                eventManager.restartEvent(name);
                player.sendMessage(ChatUtil.translate("&aИвент '&e" + name + "&a' перезапущен."));
            }
            case "delay" -> {
                if (!player.hasPermission("wallixevents.delay")) {
                    player.sendMessage(ChatUtil.translate("&cУ вас нет прав для отложенного запуска."));
                    return true;
                }
                if (args.length < 3) {
                    player.sendMessage(ChatUtil.translate("&cИспользование: /event delay <название> <секунды>"));
                    return true;
                }
                String name = args[1];
                try {
                    long seconds = Long.parseLong(args[2]);
                    eventManager.scheduleStart(name, seconds);
                    long minutes = seconds / 60;
                    long secs = seconds % 60;
                    player.sendMessage(ChatUtil.translate("&aИвент '&e" + name + "&a' запустится через &e" + minutes + "м " + secs + "с&a."));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatUtil.translate("&cВремя должно быть числом в секундах."));
                }
            }
            case "reload" -> {
                if (!player.hasPermission("wallixevents.reload")) {
                    player.sendMessage(ChatUtil.translate("&cУ вас нет прав для перезагрузки плагина."));
                    return true;
                }
                plugin.reloadConfig();
                plugin.getHologramManager().removeAll();
                plugin.getHologramManager().loadHolograms();
                player.sendMessage(ChatUtil.translate("&aКонфиги и голограммы перезагружены."));
            }
            default -> sendHelp(player);
        }

        return true;
    }

    private void sendHelp(Player player) {
        List<Component> lines = List.of(
                ChatUtil.translate("&6--- &eWallixEvents команды &6---"),
                ChatUtil.translate("&e/event start <название> &7- запустить ивент"),
                ChatUtil.translate("&e/event stop <название> &7- остановить ивент"),
                ChatUtil.translate("&e/event restart <название> &7- перезапустить ивент"),
                ChatUtil.translate("&e/event delay <название> <сек> &7- отложенный запуск"),
                ChatUtil.translate("&e/event reload &7- перезагрузить конфиги")
        );
        lines.forEach(player::sendMessage);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("start", "stop", "restart", "delay", "reload")
                    .stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("start") ||
                args[0].equalsIgnoreCase("stop") ||
                args[0].equalsIgnoreCase("restart") ||
                args[0].equalsIgnoreCase("delay"))) {
            // Предлагаем названия уже известных ивентов из events.yml
            return plugin.getEventManager().getAllEventNames()
                    .stream()
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}