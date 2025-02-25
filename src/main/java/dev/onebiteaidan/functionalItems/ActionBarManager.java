package dev.onebiteaidan.functionalItems;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ActionBarManager {
    private static final Map<Player, Integer> taskMap = new HashMap<>();
    private static final Map<Player, Map<String, Supplier<String>>> dataSources = new HashMap<>();

    public static void addSource(Player player, String key, Supplier<String> source) {
        dataSources.computeIfAbsent(player, k -> new HashMap<>()).put(key, source);
        startTask(player);
    }

    public static void removeSource(Player player, String key) {
        Map<String, Supplier<String>> sources = dataSources.get(player);
        if (sources != null) {
            sources.remove(key);
            if (sources.isEmpty()) {
                dataSources.remove(player);
                stopTask(player);
            }
        }
    }

    private static void startTask(Player player) {
        if (taskMap.containsKey(player)) return;

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                FunctionalItems.getPlugin(FunctionalItems.class),
                () -> {
                    if (!dataSources.containsKey(player)) return;
                    String actionBarText = mergeData(player);
                    player.sendActionBar(Component.text(actionBarText));
                },
                0L, 5L // Update every 5 ticks
        );
        taskMap.put(player, taskId);
    }

    private static void stopTask(Player player) {
        if (taskMap.containsKey(player)) {
            Bukkit.getScheduler().cancelTask(taskMap.remove(player));
        }
    }

    private static String mergeData(Player player) {
        Map<String, Supplier<String>> sources = dataSources.get(player);
        if (sources == null || sources.isEmpty()) return "";

        return sources.values().stream()
                .map(Supplier::get)
                .collect(Collectors.joining(" | ")); // Concatenate with separator
    }
}
