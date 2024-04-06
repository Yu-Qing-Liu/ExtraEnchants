package com.github.yuqingliu.extraenchants.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;

public class UtilityMethods {
    public static String toRoman(int number) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanLiterals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                number -= values[i];
                roman.append(romanLiterals[i]);
            }
        }
        return roman.toString();
    }

    public static int fromRoman(String roman) {
        // Mapping of Roman numerals to their integer values
        Map<Character, Integer> romanNumerals = Map.of(
            'I', 1,
            'V', 5,
            'X', 10,
            'L', 50,
            'C', 100,
            'D', 500,
            'M', 1000
        );
        int intValue = 0;
        for (int i = 0; i < roman.length(); i++) {
            if (i > 0 && romanNumerals.get(roman.charAt(i)) > romanNumerals.get(roman.charAt(i - 1))) {
                intValue += romanNumerals.get(roman.charAt(i)) - 2 * romanNumerals.get(roman.charAt(i - 1));
            } else {
                intValue += romanNumerals.get(roman.charAt(i));
            }
        }
        return intValue;
    }

    public static String formatString(String original) {
        if (original == null || original.isEmpty()) {
            return original;
        }
        // Replace underscores with spaces and then capitalize the first letter of the resulting string
        String formatted = original.replace("_", " ");
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1).toLowerCase();
    }

    public static int RandomIntBetween(int start, int end) {
        Random rand = new Random(); // Create a Random object
        if(start < 0) start = 0;
        return rand.nextInt((end - start) + 1) + start;
    }

    public static boolean hasEnchantment(ItemStack item, String enchantmentName, int level) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            return meta.lore().contains(Component.text(enchantmentName + " " + toRoman(level), NamedTextColor.GOLD));
        }
        return false;
    }

    public static int getEnchantmentLevel(ItemStack item, String enchantmentName) {
        if (item == null || !item.hasItemMeta()) return 0; // Return 0 to indicate enchantment not present
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            for (Component loreComponent : meta.lore()) {
                String loreText = loreComponent.toString(); // Convert Component to String
                if (loreText.contains(enchantmentName)) {
                    // Extract the level part of the string, assuming it's the part after the enchantment name
                    String levelPart = loreText.substring(loreText.indexOf(enchantmentName) + enchantmentName.length()).trim();
                    // Convert the Roman numeral to an integer
                    int level = fromRoman(levelPart);
                    return level;
                }
            }
        }
        return 0; // Enchantment not found
    }

    public static boolean addEnchantment(ItemStack item, String enchantmentName, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<Component> existingLore = meta.lore();
            if (existingLore == null) {
                existingLore = new ArrayList<>();
            }

            // Prepare the enchantment text you're planning to add
            Component newEnchantText = Component.text(enchantmentName + " " + toRoman(level), NamedTextColor.GOLD);

            // Check if the lore already contains this enchantment
            boolean alreadyHasEnchant = existingLore.stream().anyMatch(line -> line.equals(newEnchantText));

            if (!alreadyHasEnchant) {
                // Add your new lore since it's not a duplicate
                existingLore.add(newEnchantText);
                // Set the updated lore back to the item
                meta.lore(existingLore);
                item.setItemMeta(meta);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean isArmor(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        String materialName = item.getType().name();
        return materialName.endsWith("_HELMET") || 
               materialName.endsWith("_CHESTPLATE") || 
               materialName.endsWith("_LEGGINGS") || 
               materialName.endsWith("_BOOTS") ||
               materialName.endsWith("_CAP");
    }

    public static boolean isSword(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        String materialName = item.getType().name();
        return materialName.endsWith("_SWORD");
    }
}
