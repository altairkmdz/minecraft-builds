package dev.altair.WallixEvents.command;

import dev.altair.WallixEvents.WallixSpawn;
import dev.altair.WallixEvents.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Обработчик команды /event
 */
public class EventCommand implements CommandExecutor, TabCompleter {

    private final WallixSpawn plugin;

    public EventCommand(WallixSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtil.translate("&cЭту команду можно выполнять только в игре"));
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
                    player.sendMessage(ColorUtil.translate("&cУ вас нет прав для запуска ивентов"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(ColorUtil.translate("&cИспользование: /event start <название>"));
                    return true;
                }
                plugin.getEventManager().startEvent(args[1], player);
            }
            case "stop" -> {
                if (!player.hasPermission("wallixspawn.event.stop")) {
                    player.sendMessage(ColorUtil.translate("&cУ вас нет прав для остановки ивентов"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(ColorUtil.translate("&cИспользование: /event stop <название>"));
                    return true;
                }
                plugin.getEventManager().stopEvent(args[1], player);
            }
            case "restart" -> {
                if (!player.hasPermission("wallixspawn.event.restart")) {
                    player.sendMessage(ColorUtil.translate("&cУ вас нет прав для перезапуска ивентов"));
                    return true;