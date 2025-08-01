package com.emoweather.command;

import com.emoweather.config.MoodConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MoodListCommand implements CommandExecutor {

    private final MoodConfigManager configManager;

    public MoodListCommand(MoodConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String moods = String.join(", ", configManager.getEnabledMoodNames());
        sender.sendMessage("§eAvailable moods: §f" + moods);
        return true;
    }
}
