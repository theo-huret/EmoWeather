package com.emoweather.config;

import com.emoweather.model.Mood;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MoodConfigManager {

    private final JavaPlugin plugin;

    public MoodConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isMoodEnabled(Mood mood) {
        return plugin.getConfig().getBoolean("moods." + mood.getName(), false);
    }

    public Set<Mood> getEnabledMoods() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("moods");
        if (section == null) return Set.of();

        return section.getKeys(false).stream()
                .filter(key -> plugin.getConfig().getBoolean("moods." + key, false))
                .map(Mood::fromString)
                .collect(Collectors.toSet());
    }

    public List<String> getEnabledMoodNames() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("moods");
        if (section == null) return List.of();

        return section.getKeys(false).stream()
                .filter(key -> plugin.getConfig().getBoolean("moods." + key, false))
                .collect(Collectors.toList());
    }

    public long getUpdateIntervalTicks() {
        return plugin.getConfig().getLong("interval", 600L);
    }
}