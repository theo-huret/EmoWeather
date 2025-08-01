package com.emoweather.command;

import com.emoweather.config.MoodConfigManager;
import com.emoweather.model.Mood;
import com.emoweather.service.MoodService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoodCommand implements CommandExecutor {

    private final MoodService moodService;
    private final MoodConfigManager configManager;

    public MoodCommand(MoodService moodService, MoodConfigManager configManager) {
        this.moodService = moodService;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (args.length != 1) {
            String enabled = String.join(" | ", configManager.getEnabledMoodNames());
            player.sendMessage("§cUsage: /mood [" + enabled + "]");
            return true;
        }

        Mood mood = Mood.fromString(args[0]);
        if (!configManager.isMoodEnabled(mood)) {
            player.sendMessage("§cInvalid or disabled mood.");
            return true;
        }

        moodService.setMood(player.getUniqueId(), mood);
        player.sendMessage("§aYour mood has been set to: §e" + mood.getName());
        return true;
    }
}