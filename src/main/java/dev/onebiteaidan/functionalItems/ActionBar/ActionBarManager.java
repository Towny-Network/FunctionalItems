package dev.onebiteaidan.functionalItems.ActionBar;

import dev.onebiteaidan.functionalItems.FunctionalItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ActionBarManager {
    private static final Map<Player, Integer> taskMap = new HashMap<>();
    private static final Map<Player, PlayerActionBarConfig> playerConfigs = new HashMap<>();
    private static final Map<Player, Map<String, Supplier<String>>> dataSources = new HashMap<>();

    public static void addSource(Player player, String key, Supplier<String> source) {
        playerConfigs.computeIfAbsent(player, k -> new PlayerActionBarConfig()).addComponent(key);
        dataSources.computeIfAbsent(player, k -> new HashMap<>()).put(key, source);
        startTask(player);
    }

    public static void removeSource(Player player, String key) {
        PlayerActionBarConfig config = playerConfigs.get(player);
        if (config != null) {
            config.setEnabled(key, false);
            if (config.getEnabledComponents().isEmpty()) {
                stopTask(player);
                playerConfigs.remove(player);
                dataSources.remove(player);
            }
        }
    }

    public static PlayerActionBarConfig getPlayerConfig(Player player) {
        return playerConfigs.computeIfAbsent(player, k -> new PlayerActionBarConfig());
    }

    private static void startTask(Player player) {
        if (taskMap.containsKey(player)) return;

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(FunctionalItems.getInstance(), () -> {
            PlayerActionBarConfig config = playerConfigs.get(player);
            Map<String, Supplier<String>> sources = dataSources.get(player);

            if (config == null || sources == null) return;

            StringBuilder actionBarText = new StringBuilder();

            for (String key : config.getEnabledComponents()) {
                if (!actionBarText.isEmpty()) {
                    actionBarText.append(" | ");
                }

                Supplier<String> supplier = sources.get(key);
                actionBarText.append(supplier.get());
            }

            player.sendActionBar(Component.text(actionBarText.toString()));

        },0L, 5L); // Update every 5 ticks

        taskMap.put(player, taskId);
    }

    private static void stopTask(Player player) {
        if (taskMap.containsKey(player)) {
            Bukkit.getScheduler().cancelTask(taskMap.remove(player));
        }
    }
}
