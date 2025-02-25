package dev.onebiteaidan.functionalItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class FunctionalItems extends JavaPlugin implements Listener {

    private CompassManager compassManager;
    private ClockManager clockManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.compassManager = new CompassManager();
        this.clockManager = new ClockManager(this.getServer().getWorlds().getFirst());

        Bukkit.getPluginManager().registerEvents(this, this);

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


    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
