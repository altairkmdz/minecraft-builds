package dev.altair.WallixEvents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Обработчик команды /event
 */
public class EventCommand implements TabExecutor {

    private final WallixSpawn plugin;
    private final EventManager eventManager;
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

    public EventCommand(WallixSpawn plugin, EventManager eventManager) {
        this.plugin = plugin;
        this.eventManager = eventManager;
    }

    private void send(Player player, String path, Object... replace) {
        String raw = plugin.getConfig().getString("messages." + path, "&c[WallixSpawn] Ошибка сообщения");
        raw = raw.replace("%prefix%", plugin.getConfig().getString("messages.prefix", "&6[WallixSpawn]"));
        raw = raw.replace("%event%", replace.length > 0 ? replace[0].toString() : "");
        raw = raw.replace("%minutes%", replace.length > 1 ? replace[1].toString() : "");
        raw = raw.replace("%seconds%", replace.length > 2 ? replace[2].toString() : "");
        Component component = serializer.deserialize(raw);
        player.sendMessage(component);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эту команду можно выполнять только в игре.");
            return true;
        }

        if (args.length == 0) {
            send(player, "usage");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "start" -> {
                if (!player.hasPermission("wallixspawn.event.start")) {
                    send(player, "no-permission");
                    return true;
                }
                if (args.length < 2) {
                    send(player, "usage-start");
                    return true;
                }
                String startName = args[1];
                eventManager.startEvent(startName, player);
            }
            case "stop" -> {
                if (!player.hasPermission("wallixspawn.event.stop")) {
                    send(player, "no-permission");
                    return true;
                }
                if (args.length < 2) {
                    send(player, "usage-stop");
                    return true;
                }
                String stopName = args[1];
                eventManager.stopEvent(stopName, player);
            }
            case "restart" -> {
                if (!player.hasPermission("wallixspawn.event.restart")) {
                    send(player, "no-permission");
                    return true;
                }
                if (args.length < 2) {
                    send(player, "usage-restart");
                    return true;
                }
                String restartName = args[1];
                eventManager.restartEvent(restartName, player);
            }
            case "delay" -> {
                if (!player.hasPermission("wallixspawn.event.delay")) {
                    send(player, "no-permission");
                    return true;
                }
                if (args.length < 2) {
                    send(player, "usage-delay");
                    return true;
                }
                String delayName = args[1];
                eventManager.sendDelayInfo(delayName, player);
            }
            case "reload" -> {
                if (!player.hasPermission("wallixspawn.event.reload")) {
                    send(player, "no-permission");
                    return true;
                }
                plugin.reloadConfig();
                plugin.reloadConfig(); // reload main config
                plugin.saveResource("events.yml", true);
                plugin.saveResource("messages.yml", true);
                plugin.saveResource("hologram.yml", true);
                eventManager.reload();
                hologramManager.reload();
                send(player, "reload");
            }
            default -> send(player, "usage");
        }

        return true;
    }

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
            return new ArrayList<>(eventManager.getAllEventNames());
        }
        return Collections.emptyList();
    }
}