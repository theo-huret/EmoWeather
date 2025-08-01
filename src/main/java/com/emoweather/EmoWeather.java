package com.emoweather;

import com.emoweather.command.MoodCommand;
import com.emoweather.command.MoodGUICommand;
import com.emoweather.command.MoodListCommand;
import com.emoweather.config.MoodConfigManager;
import com.emoweather.gui.MoodGUIListener;
import com.emoweather.service.MoodService;
import com.emoweather.task.MoodScheduler;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;

public class EmoWeather extends JavaPlugin {

    private MoodService moodService;
    private MoodConfigManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        });

        this.configManager = new MoodConfigManager(this);
        this.moodService = new MoodService(this, configManager);
        new MoodScheduler(this, moodService, configManager).start();

        getServer().getPluginManager().registerEvents(new MoodGUIListener(moodService, configManager), this);

        getCommand("mood").setExecutor(new MoodCommand(moodService, configManager));
        getCommand("moodlist").setExecutor(new MoodListCommand(configManager));
        getCommand("moodgui").setExecutor(new MoodGUICommand(configManager, moodService));

        getLogger().info("✅ EmoWeather has been enabled!");
    }

    @Override
    public void onDisable() {
        moodService.saveMoods();
        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
        });
        getLogger().info("❌ EmoWeather has been disabled.");
    }
}
