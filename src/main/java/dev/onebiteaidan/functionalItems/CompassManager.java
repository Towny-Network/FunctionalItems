package dev.onebiteaidan.functionalItems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class CompassManager {

    Map<Player, Integer> players = new HashMap<>();

    public CompassManager() {}

    public void updateCompass(Player player) {
        ActionBarManager.addSource(player, "compass", () -> {
            String direction = getPlayerDirection(player);
            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();
            return String.format("🧭 %s | XYZ: %d,%d,%d", direction, x, y, z);
        });
    }

    public void removeCompass(Player player) {
        ActionBarManager.removeSource(player, "compass");
    }

    private String getPlayerDirection(Player player) {
        float yaw = (player.getLocation().getYaw() + 360) % 360;
        if (yaw < 45 || yaw >= 315) return "N";
        if (yaw < 135) return "E";
        if (yaw < 225) return "S";
        return "W";
    }
}