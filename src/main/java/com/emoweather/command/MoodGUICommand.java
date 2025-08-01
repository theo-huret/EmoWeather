package com.emoweather.command;

import com.emoweather.config.MoodConfigManager;
import com.emoweather.gui.MoodGUI;
import com.emoweather.service.MoodService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoodGUICommand implements CommandExecutor {

    private final MoodConfigManager configManager;
    private final MoodService moodService;

    public MoodGUICommand(MoodConfigManager configManager, MoodService moodService) {
        this.configManager = configManager;
        this.moodService = moodService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        new MoodGUI(moodService, configManager).open(player);
        return true;
    }
}
