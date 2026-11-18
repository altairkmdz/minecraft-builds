package dev.altair.WallixEvents;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

public class EventData {
    private final String name;
    private final String type;
    private final String worldName;
    private final double x, y, z;
    private final long duration; // seconds
    private final List<String> hologramLines;

    // Runtime fields
    private List<ArmorStand> hologramArmorStands = new ArrayList<>();

    public EventData(String name, String type, String world