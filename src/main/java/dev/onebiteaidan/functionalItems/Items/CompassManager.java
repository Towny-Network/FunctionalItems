package dev.onebiteaidan.functionalItems.Items;

import dev.onebiteaidan.functionalItems.ActionBar.ActionBarManager;
import org.bukkit.entity.Player;

public class CompassManager {

    public CompassManager() {}

    public void updateCompass(Player player) {
        ActionBarManager.addSource(player, "coords", () -> {
            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();
            return String.format("XYZ: %d,%d,%d", x, y, z);
        });

        ActionBarManager.addSource(player, "direction", () -> {
            String direction = getPlayerDirection(player);
            return String.format("🧭 %s", direction);
        });
    }

    public void removeCompass(Player player) {
        ActionBarManager.removeSource(player, "coords");
        ActionBarManager.removeSource(player, "direction");
    }

    private String getPlayerDirection(Player player) {
        float yaw = (player.getLocation().getYaw() + 360) % 360;
        if (yaw < 45 || yaw >= 315) return "N";
        if (yaw < 135) return "E";
        if (yaw < 225) return "S";
        return "W";
    }
}