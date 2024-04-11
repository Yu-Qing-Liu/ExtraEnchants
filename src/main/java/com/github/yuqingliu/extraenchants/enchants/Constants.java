package com.github.yuqingliu.extraenchants.enchants;

import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class Constants {
    private static int bookshelfMultiplier = 5;
    private static double repairAnvilCostPerResource = 1.5;
    private static int vanillaAnvilCostPerLevel = 5;
    private static int customAnvilCostPerLevel = 10;

    private static HashMap<NamespacedKey, Integer> enchantments = new HashMap<>();
    private static HashMap<String, Integer> customEnchants = new HashMap<>();

    public static int getBookshelfMultiplier() {
        return bookshelfMultiplier;
    }

    public static void setBookshelfMultiplier(int bookshelfMultiplier) {
        Constants.bookshelfMultiplier = bookshelfMultiplier;
    }

    public static HashMap<NamespacedKey, Integer> getEnchantments() {
        return enchantments;
    }

    public static void setEnchantments(HashMap<NamespacedKey, Integer> enchantments) {
        Constants.enchantments = enchantments;
    }

    public static HashMap<String, Integer> getCustomEnchantments() {
        return customEnchants;
    }

    public static void setCustomEnchantments(HashMap<String, Integer> customEnchants) {
        Constants.customEnchants = customEnchants;
    }

    public static double getRepairAnvilCostPerResource() {
        return repairAnvilCostPerResource;
    }

    public static void setRepairAnvilCostPerResource(double newRepairAnvilCostPerResource) {
        repairAnvilCostPerResource = newRepairAnvilCostPerResource;
    }

    public static int getVanillaAnvilCostPerLevel() {
        return vanillaAnvilCostPerLevel;
    }

    public static void setVanillaAnvilCostPerLevel(int newVanillaAnvilCostPerLevel) {
        vanillaAnvilCostPerLevel = newVanillaAnvilCostPerLevel;
    }

    public static int getCustomAnvilCostPerLevel() {
        return customAnvilCostPerLevel;
    }

    public static void setCustomAnvilCostPerLevel(int newCustomAnvilCostPerLevel) {
        customAnvilCostPerLevel = newCustomAnvilCostPerLevel;
    }
}


