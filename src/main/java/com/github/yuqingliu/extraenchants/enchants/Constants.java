package com.github.yuqingliu.extraenchants.enchants;

import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.List;

public class Constants {
    private static int bookshelfMultiplier = 5;
    private static double repairAnvilCostPerResource = 1.5;
    private static int anvilCostPerLevel = 3;

    private static HashMap<NamespacedKey, List<Object>> enchantments = new HashMap<>();
    private static HashMap<String, List<Object>> customEnchants = new HashMap<>();

    public static int getBookshelfMultiplier() {
        return bookshelfMultiplier;
    }

    public static void setBookshelfMultiplier(int bookshelfMultiplier) {
        Constants.bookshelfMultiplier = bookshelfMultiplier;
    }

    public static HashMap<NamespacedKey, List<Object>> getEnchantments() {
        return enchantments;
    }

    public static void setEnchantments(HashMap<NamespacedKey, List<Object>> enchantments) {
        Constants.enchantments = enchantments;
    }

    public static HashMap<String, List<Object>> getCustomEnchantments() {
        return customEnchants;
    }

    public static void setCustomEnchantments(HashMap<String, List<Object>> customEnchants) {
        Constants.customEnchants = customEnchants;
    }

    public static double getRepairAnvilCostPerResource() {
        return repairAnvilCostPerResource;
    }

    public static void setRepairAnvilCostPerResource(double newRepairAnvilCostPerResource) {
        repairAnvilCostPerResource = newRepairAnvilCostPerResource;
    }

    public static int getAnvilCostPerLevel() {
        return anvilCostPerLevel;
    }

    public static void setAnvilCostPerLevel(int newAnvilCostPerLevel) {
        anvilCostPerLevel = newAnvilCostPerLevel;
    }
}


