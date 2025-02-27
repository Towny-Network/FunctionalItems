package dev.onebiteaidan.functionalItems.ActionBar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActionBarConfigGUI implements Listener {
    private static final String GUI_TITLE = "Customize Your Action Bar";
    private static final int INVENTORY_SIZE = 9;
    private static final Material ENABLED_ITEM = Material.LIME_WOOL;
    private static final Material DISABLED_ITEM = Material.RED_WOOL;

    public static void open(Player player) {
        Inventory gui = Bukkit.createInventory(player, INVENTORY_SIZE, Component.text(GUI_TITLE));

        PlayerActionBarConfig config = ActionBarManager.getPlayerConfig(player);
        if (config == null) return;

        int index = 0;
        for (Map.Entry<String, Boolean> entry : config.getComponentStates().entrySet()) {
            gui.setItem(index, createItem(entry.getKey(), entry.getValue()));
            index++;
        }

        player.openInventory(gui);
    }

    private static ItemStack createItem(String key, boolean enabled) {
        ItemStack item = new ItemStack(enabled ? ENABLED_ITEM : DISABLED_ITEM);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(key));
        meta.lore(List.of(
                Component.text("Status: ").color(NamedTextColor.GRAY).append(enabled ? Component.text("Enabled").color(NamedTextColor.GREEN) : Component.text("Disabled").color(NamedTextColor.RED)),
                Component.text("Right-click to toggle").color(NamedTextColor.YELLOW),
                        Component.text("§eDrag to reorder").color(NamedTextColor.YELLOW)
        ));
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(GUI_TITLE)) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getItemMeta() == null) return;

        String key = clickedItem.getItemMeta().getDisplayName().replace("§6", ""); // Get component name
        if (event.getClick() == ClickType.RIGHT) {

            event.setCancelled(true);

            // Toggle enable/disable
            PlayerActionBarConfig config = ActionBarManager.getPlayerConfig(player);
            if (config != null) {
                boolean newState = !config.isEnabled(key);
                config.setEnabled(key, newState);
                event.getClickedInventory().setItem(event.getSlot(), createItem(key, newState));
            }
        } else if (event.getClick() == ClickType.LEFT) {
//            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(GUI_TITLE)) return;

        LinkedList<String> keys = new LinkedList<>();

        for (ItemStack item : event.getInventory()) {
            if (item != null) {
                keys.add(item.getItemMeta().getDisplayName());
            }
        }

        PlayerActionBarConfig config = ActionBarManager.getPlayerConfig(player);
        config.reorderKeys(keys);
    }
}