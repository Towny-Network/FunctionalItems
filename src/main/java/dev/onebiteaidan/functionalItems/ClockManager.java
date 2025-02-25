package dev.onebiteaidan.functionalItems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class ClockManager {


    Map<Player, Integer> players = new HashMap<>();
    long initialTime;
    long timeInDays;

    public ClockManager(World world) {
        this.initialTime = world.getFullTime();
        this.timeInDays = 0;

    }

    public void updateClock(Player player) {
        ActionBarManager.addSource(player, "clock", () -> {
            long day = ((player.getWorld().getFullTime()) / 24000) % 7;
            String dayString = switch ((int) day) {
                case 0 -> "Monday";
                case 1 -> "Chewsday";
                case 2 -> "Wednesday";
                case 3 -> "Trusday";
                case 4 -> "Froiday";
                case 5 -> "Saturday";
                case 6 -> "Sonfdsu";
                default -> "UH OH";
            };

            long currentTime = (player.getWorld().getTime());
            String timeString = formatMinecraftTime(currentTime);
            return dayString + " | " + timeString;
        });
    }

    private String formatMinecraftTime(long time) {
        int hours = (int) ((time / 1000 + 6) % 24); // Extract hours
        int minutes = (int) ((time % 1000) * 60 / 1000); // Extract minutes

        return String.format("%02d:%02d", hours, minutes); // Pads with zeroes
    }

    void removeClock(Player player) {
        ActionBarManager.removeSource(player, "clock");
    }
}
