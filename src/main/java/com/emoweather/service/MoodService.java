package com.emoweather.service;

import com.emoweather.EmoWeather;
import com.emoweather.config.MoodConfigManager;
import com.emoweather.model.Mood;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MoodService {

    private final Map<UUID, Mood> playerMoods = new HashMap<>();
    private final EmoWeather plugin;
    private final MoodConfigManager configManager;

    private final File moodsFile;
    private final FileConfiguration moodsConfig;

    private Mood currentMood = Mood.HAPPY;

    public MoodService(EmoWeather plugin, MoodConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;

        this.moodsFile = new File(plugin.getDataFolder(), "moods.yml");
        this.moodsConfig = YamlConfiguration.loadConfiguration(moodsFile);

        loadMoods();
    }

    private void loadMoods() {
        if (!moodsFile.exists()) {
            try {
                moodsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create moods.yml: " + e.getMessage());
                return;
            }
        }

        for (String uuidStr : moodsConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidStr);
                Mood mood = Mood.fromString(moodsConfig.getString(uuidStr));
                playerMoods.put(uuid, mood);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in moods.yml: " + uuidStr);
            }
        }
    }

    public void saveMoods() {
        for (Map.Entry<UUID, Mood> entry : playerMoods.entrySet()) {
            moodsConfig.set(entry.getKey().toString(), entry.getValue().getName());
        }

        try {
            moodsConfig.save(moodsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save moods.yml: " + e.getMessage());
        }
    }

    public void setMood(UUID playerId, Mood mood) {
        playerMoods.put(playerId, mood);
    }

    public Mood getMood(UUID playerId) {
        return playerMoods.getOrDefault(playerId, Mood.HAPPY);
    }

    public Map<Mood, Integer> countMoods(Collection<UUID> playerIds) {
        Map<Mood, Integer> counts = new HashMap<>();

        for (UUID id : playerIds) {
            Mood mood = getMood(id);
            if (!configManager.isMoodEnabled(mood)) continue;

            counts.put(mood, counts.getOrDefault(mood, 0) + 1);
        }

        return counts;
    }

    public Set<UUID> getAllPlayers() {
        return playerMoods.keySet();
    }

    public Mood getCurrentMood() {
        return currentMood;
    }

    public void setCurrentMood(Mood mood) {
        this.currentMood = mood;
    }
}
