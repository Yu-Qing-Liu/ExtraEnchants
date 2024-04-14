package com.github.yuqingliu.extraenchants.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

public class Constants {
    private static double repairAnvilCostPerResource = 1.5;
    private static int anvilCostPerLevel = 3;

    private static HashMap<NamespacedKey, List<Object>> enchantments = new HashMap<>();
    private static HashMap<String, List<Object>> customEnchants = new HashMap<>();
    private static HashMap<Material, List<Material>> anvilData = new HashMap<>();

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

    public static HashMap<Material, List<Material>> getAnvilData() {
        return anvilData;
    }

    public static void setAnvilData(HashMap<Material, List<Material>> newData) {
        Constants.anvilData = newData;
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


