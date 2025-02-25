package dev.onebiteaidan.functionalItems.Items;

import dev.onebiteaidan.functionalItems.ActionBar.ActionBarManager;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
        ActionBarManager.addSource(player, "clockDay", () -> {
            long day = ((player.getWorld().getFullTime()) / 24000) % 7;

            return switch ((int) day) {
                case 0 -> "Monday";
                case 1 -> "Chewsday";
                case 2 -> "Wednesday";
                case 3 -> "Trusday";
                case 4 -> "Froiday";
                case 5 -> "Saturday";
                case 6 -> "Sonfdsu";
                default -> "UH OH";
            };
        });

        ActionBarManager.addSource(player, "clockTime", () -> {
            long currentTime = (player.getWorld().getTime());
            return formatMinecraftTime(currentTime);
        });
    }

    public void removeClock(Player player) {
        ActionBarManager.removeSource(player, "clockDay");
        ActionBarManager.removeSource(player, "clockTime");
    }

    private String formatMinecraftTime(long time) {
        int hours = (int) ((time / 1000 + 6) % 24); // Extract hours
        int minutes = (int) ((time % 1000) * 60 / 1000); // Extract minutes

        return String.format("%02d:%02d", hours, minutes); // Pads with zeroes
    }
}
