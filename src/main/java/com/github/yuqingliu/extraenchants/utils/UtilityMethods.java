package com.github.yuqingliu.extraenchants.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.github.yuqingliu.extraenchants.database.Constants;

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

    private static final Map<Character, Integer> romanNumerals = Map.of(
        'I', 1,
        'V', 5,
        'X', 10,
        'L', 50,
        'C', 100,
        'D', 500,
        'M', 1000
    );

    public static int fromRoman(String roman) {
        int result = 0;
        int prevValue = 0;
        for (int i = roman.length() - 1; i >= 0; i--) {
            char ch = roman.charAt(i);
            Integer value = romanNumerals.get(ch);
            if (value != null) {
                if (value < prevValue) {
                    // Subtract this value for subtractive notation
                    result -= value;
                } else {
                    result += value;
                }
                prevValue = value;
            } else {
                // Handle the case where the map returns null due to an unexpected character
                System.err.println("Unexpected character in Roman numeral: " + ch);
                return 0; // Or throw an exception, depending on how you wish to handle this
            }
        }
        return result;
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
        if (item == null || !item.hasItemMeta()) return 0;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();
            for (Component loreComponent : meta.lore()) {
                // Use PlainTextComponentSerializer to convert Component to plain text
                String loreText = plainTextSerializer.serialize(loreComponent);
                if (loreText.contains(enchantmentName)) {
                    // Assuming the format "[EnchantmentName] [Level]"
                    String[] parts = loreText.split(enchantmentName, 2);
                    if (parts.length > 1) {
                        String levelPart = parts[1].trim();
                        return fromRoman(levelPart);
                    }
                }
            }
        }
        return 0;
    }

    public static boolean addEnchantment(ItemStack item, String enchantmentName, int level) {
        final Set<String> NOT_UPGRADABLE = new HashSet<>(Arrays.asList(
            "Homing",
            "Flame"
        ));
        final Set<String> ENCHANTS_LIST = new HashSet<>(Arrays.asList(
            "Homing",
            "Flame",
            "Growth",
            "Mitigation",
            "Snipe",
            "Venom",
            "Wither",
            "Replant"
        ));

        final int MAX_LEVEL = Constants.getMaxCustomEnchantLevel();

        ItemMeta meta = item.getItemMeta();
        if (meta != null && ENCHANTS_LIST.contains(enchantmentName)) {
            List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
            PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();

            // Determine if the enchantment already exists and its level
            int prevLevel = getEnchantmentLevel(item, enchantmentName);

            if (prevLevel > 0) {
                // The enchantment already exists, determine action based on level comparison
                if (NOT_UPGRADABLE.contains(enchantmentName) || level > MAX_LEVEL) {
                    // If the enchantment is not upgradable or the new level exceeds max, do nothing
                    return true;
                } else if (level == prevLevel && level < MAX_LEVEL) {
                    // If the new level is the same and it's below max, upgrade by 1
                    level++;
                } else if (level <= prevLevel) {
                    // If the new level is not greater, do nothing
                    return true;
                }
                // Remove the old enchantment lore
                existingLore = existingLore.stream()
                        .filter(loreComponent -> !plainTextSerializer.serialize(loreComponent).contains(enchantmentName))
                        .collect(Collectors.toList());
            }

            // Add or upgrade the enchantment
            Component newEnchantText = Component.text(enchantmentName + " " + toRoman(level), NamedTextColor.GOLD);
            existingLore.add(newEnchantText);

            // Update the lore
            meta.lore(existingLore);
            item.setItemMeta(meta);
            return true;
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

    public static boolean isWeapon(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        String materialName = item.getType().name();
        return materialName.endsWith("_SWORD") ||
               materialName.endsWith("_AXE") ||
               materialName.equals("BOW") ||
               materialName.equals("CROSSBOW") ||
               materialName.equals("TRIDENT");
    }
}
