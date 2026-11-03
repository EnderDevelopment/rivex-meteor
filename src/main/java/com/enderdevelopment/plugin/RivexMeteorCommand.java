package com.enderdevelopment.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class RivexMeteorCommand implements CommandExecutor {

    private final Main plugin;

    public RivexMeteorCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Only players can use this command.").color(NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Component.text("Usage: /rivemeteor <reload|start|stop>").color(NamedTextColor.RED));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfig();
                player.sendMessage(Component.text("Config reloaded.").color(NamedTextColor.GREEN));
                break;
            case "start":
                plugin.startMeteorTask();
                player.sendMessage(Component.text("Meteor task started.").color(NamedTextColor.GREEN));
                break;
            case "stop":
                if (plugin.getMeteorTask() != null) {
                    plugin.getMeteorTask().cancel();
                }
                player.sendMessage(Component.text("Meteor task stopped.").color(NamedTextColor.GREEN));
                break;
            default:
                player.sendMessage(Component.text("Unknown command.").color(NamedTextColor.RED));
                break;
        }

        return true;
    }
}