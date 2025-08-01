package com.emoweather.task;

import com.emoweather.config.MoodConfigManager;
import com.emoweather.model.Mood;
import com.emoweather.service.MoodService;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class MoodScheduler {

    private final JavaPlugin plugin;
    private final MoodService moodService;
    private final MoodConfigManager configManager;
    private Mood lastMood = Mood.HAPPY;

    public MoodScheduler(JavaPlugin plugin, MoodService moodService, MoodConfigManager configManager) {
        this.plugin = plugin;
        this.moodService = moodService;
        this.configManager = configManager;
    }

    public void start() {
        long interval = configManager.getUpdateIntervalTicks();

        new BukkitRunnable() {
            @Override
            public void run() {
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                if (players.isEmpty()) return;

                List<UUID> playerIds = players.stream().map(Player::getUniqueId).toList();
                Map<Mood, Integer> moodCounts = moodService.countMoods(playerIds);

                Mood dominantMood = getDominantMood(moodCounts);
                applyWeather(dominantMood);

                if (!dominantMood.equals(lastMood)) {
                    Bukkit.broadcastMessage("§b[EmoWeather] Current mood: §e" + dominantMood.getName());
                    lastMood = dominantMood;
                    moodService.setCurrentMood(dominantMood);
                }
            }
        }.runTaskTimer(plugin, 0L, interval);
    }

    private Mood getDominantMood(Map<Mood, Integer> moodCounts) {
        Mood dominant = Mood.HAPPY;
        int max = -1;

        for (Map.Entry<Mood, Integer> entry : moodCounts.entrySet()) {
            if (entry.getValue() > max) {
                dominant = entry.getKey();
                max = entry.getValue();
            }
        }

        return dominant;
    }

    private void applyWeather(Mood mood) {
        World world = Bukkit.getWorlds().get(0);

        switch (mood) {
            case HAPPY -> {
                world.setStorm(false);
                world.setThundering(false);
            }
            case SAD -> {
                world.setStorm(true);
                world.setThundering(false);
            }
            case ANGRY -> {
                world.setStorm(true);
                world.setThundering(true);
            }
            default -> {
                world.setStorm(false);
                world.setThundering(false);
            }
        }
    }
}
