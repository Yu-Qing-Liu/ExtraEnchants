package com.github.yuqingliu.extraenchants.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

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

    public static boolean hasEnchantment(ItemStack item, String name) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            return meta.lore().contains(Component.text(name, NamedTextColor.GOLD));
        }
        return false;
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
            boolean alreadyHasEnchant = existingLore.stream()
                    .anyMatch(line -> line.equals(newEnchantText));

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
}
