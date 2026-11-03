package com.enderdevelopment.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class Main extends JavaPlugin {

    private FileConfiguration config;
    private BukkitTask meteorTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();

        // Register commands
        getCommand("rivemeteor").setExecutor(new RivexMeteorCommand(this));

        // Start the meteor task
        startMeteorTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (meteorTask != null) {
            meteorTask.cancel();
        }
    }

    public void startMeteorTask() {
        long interval = config.getLong("meteor.interval", 6000) * 20L; // Convert seconds to ticks
        meteorTask = getServer().getScheduler().runTaskTimer(this, new MeteorTask(this), interval, interval);
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
        }
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            getLogger().severe("Could not save config to " + configFile);
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    public BukkitTask getMeteorTask() {
        return meteorTask;
    }

    private File configFile;
}