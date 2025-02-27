package dev.onebiteaidan.functionalItems.Items;

import dev.onebiteaidan.functionalItems.ActionBar.ActionBarManager;
import dev.onebiteaidan.functionalItems.Events.DayChangeEvent;
import dev.onebiteaidan.functionalItems.Events.WeekChangeEvent;
import dev.onebiteaidan.functionalItems.FunctionalItems;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ClockManager {

    int currentDay;

    public ClockManager(World world) {
        this.currentDay = (int) (((world.getFullTime()) / 24000) % 7);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(FunctionalItems.getInstance(), () -> {

            int day = (int) (((world.getFullTime() + 6000) / 24000) % 7);

            // Check for a day change
            if (this.currentDay != day) {
                // New DayChangeEvent
                DayChangeEvent event = new DayChangeEvent();
                Bukkit.getPluginManager().callEvent(event);
            }

            // Check for week change
            if (this.currentDay == 6) {
                if (day == 0) {
                    // New WeekChangeEvent
                    WeekChangeEvent event = new WeekChangeEvent();
                    Bukkit.getPluginManager().callEvent(event);
                }
            }

            this.currentDay = day;
        }, 0L, 5L);
    }

    public void updateClock(Player player) {
        ActionBarManager.addSource(player, "clockDay", () -> {
            long day = ((player.getWorld().getFullTime() + 6000) / 24000) % 7; // The plus 6000 is because the minecraft world starts at 6AM (6000). Without this, the day value updates at dawn (6am) rather than midnight

            return switch ((int) day) {
                case 0 -> "Sun";
                case 1 -> "Mon";
                case 2 -> "Tues";
                case 3 -> "Wed";
                case 4 -> "Thurs";
                case 5 -> "Fri";
                case 6 -> "Sat";
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
