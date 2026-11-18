package dev.altair.WallixEvents.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Простая реализация голограммы через стеклянные ArmorStand'ы.
 */
public class Hologram {

    private final Location baseLocation;
    private final List<String> rawLines;
    private final List<ArmorStand> stands = new ArrayList<>();

    public Hologram(Location baseLocation, List<String>