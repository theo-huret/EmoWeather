package com.emoweather.gui;

import com.emoweather.config.MoodConfigManager;
import com.emoweather.model.Mood;
import com.emoweather.service.MoodService;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MoodGUIListener implements Listener {

    private final MoodService moodService;
    private final MoodConfigManager configManager;

    public MoodGUIListener(MoodService moodService, MoodConfigManager configManager) {
        this.moodService = moodService;
        this.configManager = configManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equalsIgnoreCase("§8Mood Selector")) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String name = clicked.getItemMeta().getDisplayName();
        if (!name.contains("Mood:")) return;

        String moodName = name.replace("§fMood: §e", "").toLowerCase();
        Mood chosenMood = Mood.fromString(moodName);

        if (!configManager.isMoodEnabled(chosenMood)) {
            player.sendMessage("§cThis mood is disabled.");
            return;
        }

        moodService.setMood(player.getUniqueId(), chosenMood);
        player.sendMessage("§aYou selected mood: §e" + chosenMood.getName());
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.2f);
        player.closeInventory();
    }
}
