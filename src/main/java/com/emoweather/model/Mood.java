package com.emoweather.model;

import org.bukkit.Material;

public enum Mood {
    HAPPY("happy", Material.SUNFLOWER),
    SAD("sad", Material.GHAST_TEAR),
    ANGRY("angry", Material.BLAZE_POWDER);

    private final String name;
    private final Material icon;

    Mood(String name, Material icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public static Mood fromString(String input) {
        for (Mood mood : values()) {
            if (mood.name.equalsIgnoreCase(input)) return mood;
        }
        return HAPPY;
    }
}

