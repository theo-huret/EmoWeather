package com.emoweather.gui;

import com.emoweather.config.MoodConfigManager;
import com.emoweather.model.Mood;
import com.emoweather.service.MoodService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MoodGUI {

    private final MoodService moodService;
    private final MoodConfigManager configManager;

    public MoodGUI(MoodService moodService, MoodConfigManager configManager) {
        this.moodService = moodService;
        this.configManager = configManager;
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8Mood Selector");

        Mood dominantMood = moodService.getCurrentMood();

        ItemStack currentItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta currentMeta = currentItem.getItemMeta();
        currentMeta.setDisplayName("§bCurrent Mood: §e" + dominantMood.getName());


        double intervalMinutes = configManager.getUpdateIntervalTicks() / 1200.0;
        String minutesText = String.format(Locale.US, "%.1f minutes", intervalMinutes);

        currentMeta.setLore(List.of(
                "§7Updated every " + minutesText + "."
        ));

        currentItem.setItemMeta(currentMeta);
        inv.setItem(4, currentItem);

        Map<Mood, Integer> moodCounts = moodService.countMoods(moodService.getAllPlayers());

        int[] moodSlots = {12, 13, 14};
        int index = 0;

        for (Mood mood : configManager.getEnabledMoods()) {
            ItemStack item = new ItemStack(mood.getIcon());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§fMood: §e" + mood.getName());

            List<String> lore = new ArrayList<>();
            lore.add("§7Live votes: §a" + moodCounts.getOrDefault(mood, 0));

            if (moodService.getMood(player.getUniqueId()) == mood) {
                lore.add("§aYou selected this mood.");
            } else {
                lore.add("§7Click to vote for this mood.");
            }

            meta.setLore(lore);
            item.setItemMeta(meta);

            if (index < moodSlots.length) {
                inv.setItem(moodSlots[index++], item);
            }
        }

        player.openInventory(inv);
    }
}