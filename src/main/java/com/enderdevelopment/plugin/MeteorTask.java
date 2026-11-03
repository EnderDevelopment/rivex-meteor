package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MeteorTask implements Runnable {

    private final Main plugin;

    public MeteorTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // Get a random player to target
        Player targetPlayer = Bukkit.getOnlinePlayers().stream().findAny().orElse(null);
        if (targetPlayer == null) {
            return;
        }

        // Get the player's location
        Location playerLocation = targetPlayer.getLocation();
        World world = playerLocation.getWorld();

        // Calculate the meteor spawn location (above the player)
        Location meteorLocation = playerLocation.clone().add(0, 50, 0);

        // Spawn the meteor
        FallingBlock meteor = world.spawnFallingBlock(meteorLocation, Material.OBSIDIAN.createBlockData());
        meteor.setHurtEntities(false);
        meteor.setDropItem(false);

        // Broadcast meteor announcement
        Component message = Component.text("A meteor is falling near " + targetPlayer.getName() + "!").color(NamedTextColor.RED);
        Bukkit.broadcast(message);

        // Schedule the meteor impact
        new BukkitRunnable() {
            @Override
            public void run() {
                // Create impact effect
                world.spawnParticle(Particle.EXPLOSION, meteorLocation, 1);
                world.playSound(meteorLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);

                // Spawn loot chest
                Location chestLocation = meteorLocation.clone().subtract(0, 1, 0);
                world.getBlockAt(chestLocation).setType(Material.CHEST);

                // Add loot to the chest
                // This is a simplified example; you would need to implement your own loot system
                // For example, you could use a configuration file to define the loot
                // and then randomly select items based on their chances
                // Here, we just add a diamond sword as an example
                ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
                world.getBlockAt(chestLocation).getState().getInventory().addItem(diamondSword);
            }
        }.runTaskLater(plugin, 100L); // 100 ticks = 5 seconds
    }
}