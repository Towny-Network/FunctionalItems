package dev.onebiteaidan.functionalItems.Commands;

import dev.onebiteaidan.functionalItems.ActionBar.ActionBarConfigGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FunctionalItemsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("menu")) {
            ActionBarConfigGUI.open(player);
            return true;
        }

        player.sendMessage("Usage: /actionbar menu");
        return true;
    }
}
