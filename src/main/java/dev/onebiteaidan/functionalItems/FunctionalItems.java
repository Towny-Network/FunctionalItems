package dev.onebiteaidan.functionalItems;

import dev.onebiteaidan.functionalItems.ActionBar.ActionBarConfigGUI;
import dev.onebiteaidan.functionalItems.Commands.FunctionalItemsCommand;
import dev.onebiteaidan.functionalItems.Events.DayChangeEvent;
import dev.onebiteaidan.functionalItems.Events.WeekChangeEvent;
import dev.onebiteaidan.functionalItems.Items.ClockManager;
import dev.onebiteaidan.functionalItems.Items.CompassManager;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class FunctionalItems extends JavaPlugin implements Listener {

    private static FunctionalItems instance;

    private CompassManager compassManager;
    private ClockManager clockManager;

    @Override
    public void onEnable() {
        instance = this;

        // Plugin startup logic
        this.compassManager = new CompassManager();
        this.clockManager = new ClockManager(this.getServer().getWorlds().getFirst());

        // Register events
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new ActionBarConfigGUI(), this);

        // Register commands
        getCommand("functionalitems").setExecutor(new FunctionalItemsCommand());

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        boolean hasClock = player.getInventory().contains(Material.CLOCK);
        boolean hasCompass = player.getInventory().contains(Material.COMPASS);

        if (hasClock) clockManager.updateClock(player);
        else clockManager.removeClock(player);

        if (hasCompass) compassManager.updateCompass(player);
        else compassManager.removeCompass(player);
    }


    @EventHandler
    public void onWeekChange(WeekChangeEvent event) {
        Random random = new Random();
        Difficulty[] difficulties = Difficulty.values();
        Difficulty difficulty = difficulties[random.nextInt(difficulties.length)];
        this.getServer().getWorlds().getFirst().setDifficulty(difficulty);

        this.getLogger().info("The server difficulty was changed to: " + difficulty);

        for (Player player : this.getServer().getOnlinePlayers()) {
            player.sendMessage("The server difficulty was changed to: " + difficulty);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
