package dev.altair.WallixEvents.command;

import dev.altair.WallixEvents.WallixSpawn;
import dev.altair.WallixEvents.event.EventManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventCommand implements TabExecutor {

    private final WallixSpawn plugin;
    private final EventManager eventManager;

    public EventCommand(WallixSpawn plugin) {
        this.plugin = plugin;
        this.eventManager = plugin.getEventManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(WallixSpawn.colorize("&cЭту команду можно выполнять только в игре."));
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "start" -> {
                if (!player.hasPermission("wallixspawn.event.start")) {
                    player.sendMessage(WallixSpawn.colorize("&cУ вас нет прав для запуска ивентов."));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(WallixSpawn.colorize("&eИспользование: /event start <название>"));
                    return true;
                }
                String name = args[1];
                if (eventManager.startEvent(name)) {
                    player.sendMessage(WallixSpawn.colorize("&aИвент &e" + name + " &aзапущен!"));
                } else {
                    player.sendMessage(WallixSpawn.colorize("&cИвент с названием &e" + name + " &cне найден или уже запущен."));
                }
            }
            case "stop" -> {
                if (!player.hasPermission("wallixspawn.event.stop")) {
                    player.sendMessage(WallixSpawn.colorize("&cУ вас нет прав для остановки ивентов."));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(WallixSpawn.colorize("&eИспользование: /event stop <название>"));
                    return true;
                }
                String name = args[1];
                if (eventManager.stopEvent(name)) {
                    player.sendMessage(WallixSpawn.colorize("&aИвент &e" + name + " &aостановлен!"));
                } else {
                    player.sendMessage(WallixSpawn.colorize("&cИвент с названием &e" + name + " &cне найден или не запущен."));
                }
            }
            case "restart" -> {
                if (!player.hasPermission("wallixspawn.event.restart")) {
                    player.sendMessage(WallixSpawn.colorize("&cУ вас нет прав для перезапуска ивентов."));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(WallixSpawn.colorize("&eИспользование: /event restart <название>"));
                    return true;
                }
                String name = args[1];
                if (eventManager.restartEvent(name)) {
                    player.sendMessage(WallixSpawn.colorize("&aИвент &e" + name + " &aперезапущен!"));
                } else {
                    player.sendMessage(WallixSpawn.colorize("&cНе удалось перезапустить ивент &e" + name + "&c."));
                }
            }
            case "delay" -> {
                if (!player.hasPermission("wallixspawn.event.delay")) {
                    player.sendMessage(WallixSpawn.colorize("&cУ вас нет прав для просмотра задержки."));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(WallixSpawn.colorize("&eИспользование: /event delay <название>"));
                    return true;
                }
                String name = args[1];
                long seconds = eventManager.getDelaySeconds(name);
                if (seconds < 0) {
                    player.sendMessage(WallixSpawn.colorize("&cИвент &e" + name + " &cне найден."));
                } else {
                    long minutes = seconds / 60;
                    long secs = seconds % 60;
                    player.sendMessage(WallixSpawn.colorize("&aОставшееся время до ивента &e" + name + "&a: &e" + minutes + "м " + secs + "с"));
                }
            }
            case "reload" -> {
                if (!player.hasPermission("wallixspawn.event.reload")) {
                    player.sendMessage(WallixSpawn.colorize("&cУ вас нет прав для перезагрузки плагина."));
                    return true;
                }
                plugin.reloadConfig();
                plugin.saveResource("events.yml", true);
                plugin.saveResource("messages.yml", true);
                plugin.saveResource("hologram.yml", true);
                eventManager.reload();
                plugin.getHologramManager().removeAll();
                plugin.getHologramManager().loadAndSpawn();
                player.sendMessage(WallixSpawn.colorize("&aКонфиги перезагружены, ивенты и голограммы обновлены."));
            }
            default -> sendHelp(player);
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(WallixSpawn.colorize("&6--- &bКоманды WallixSpawn &6---"));
        player.sendMessage(WallixSpawn.colorize("&e/event start <название> &7- запустить ивент"));
        player.sendMessage(WallixSpawn.colorize("&e/event stop <название> &7- остановить ивент"));
        player.sendMessage(WallixSpawn.colorize("&e/event restart <название> &7- перезапустить ивент"));
        player.sendMessage(WallixSpawn.colorize("&e/event delay <название> &7- время до ивента"));
        player.sendMessage(WallixSpawn.colorize("&e/event reload &7- перезагрузить плагин"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("start", "stop", "restart", "delay", "reload")
                    .stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }
        if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            return eventManager.getAllEventNames()
                    .stream()
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}