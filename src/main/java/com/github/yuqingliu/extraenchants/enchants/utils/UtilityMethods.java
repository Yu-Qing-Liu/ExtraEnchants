package com.github.yuqingliu.extraenchants.enchants.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.github.yuqingliu.extraenchants.enchants.*;

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
                return 0;
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
                        String[] levelParts = levelPart.split(" ");
                        for(String part : levelParts) {
                            if(fromRoman(part) > 0) return fromRoman(part);
                        }
                    }
                }
            }
        }
        return 0;
    }

    public static Map<CustomEnchantment, Integer> getEnchantments(ItemStack item) {
        List<CustomEnchantment> customEnchantsRegistry = Database.getCustomEnchantmentRegistry();
        Map<CustomEnchantment, Integer> itemEnchants = new HashMap<>();

        for (CustomEnchantment enchantment : customEnchantsRegistry) {
            String registryName = enchantment.getName();
            int level = getEnchantmentLevel(item, registryName);
            if(level > 0) {
                itemEnchants.put(enchantment, level);
            }
        }
        return itemEnchants;
    }

    public static boolean addEnchantment(ItemStack item, String enchantmentName, int level, TextColor color) {
        List<CustomEnchantment> Register = Database.getCustomEnchantmentRegistry();
        CustomEnchantment enchant = null;
        for (CustomEnchantment enchantment : Register) {
            if(enchantment.getName().equals(enchantmentName)) {
                enchant = enchantment;
                break;
            } 
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null && enchant != null) {
            List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
            PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();

            // Determine if the enchantment already exists and its level
            int prevLevel = getEnchantmentLevel(item, enchantmentName);

            if (prevLevel > 0) {
                // The enchantment already exists, determine action based on level comparison
                if (level == prevLevel) {
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
            Component newEnchantText = Component.text(enchantmentName + " " + toRoman(level), color);
            existingLore.add(newEnchantText);

            // Update the lore
            meta.lore(existingLore);
            item.setItemMeta(meta);
            return true;
        }
        return false;
    }

    public static void removeEnchantment(ItemStack item, String enchantmentName) {
        ItemMeta meta = item.getItemMeta();
        List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
        PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();
        existingLore = existingLore.stream()
                .filter(loreComponent -> !plainTextSerializer.serialize(loreComponent).contains(enchantmentName))
                .collect(Collectors.toList());
        meta.lore(existingLore);
        item.setItemMeta(meta);
    }

    public static void applyVanillaEnchant(Player player, EnchantmentOffer selectedOffer, ItemStack item) {
        HashMap<NamespacedKey, Integer> Registry = Constants.getEnchantments();
        
        NamespacedKey enchantmentName = selectedOffer.getEnchantment().getKey();
        int enchantmentLevel = selectedOffer.getEnchantmentLevel();
        int prevEnchantLevel = item.getEnchantmentLevel(selectedOffer.getEnchantment());
        int maxEnchantmentLevel = Registry.get(enchantmentName);

        int requiredLevel = selectedOffer.getCost();
        int playerLevel = player.getLevel();
        if(playerLevel >= requiredLevel) {
            if(prevEnchantLevel > selectedOffer.getEnchantmentLevel()) {
                return;
            } else if(prevEnchantLevel == enchantmentLevel) {
                if(prevEnchantLevel == maxEnchantmentLevel) return;
                item.addUnsafeEnchantment(selectedOffer.getEnchantment(), selectedOffer.getEnchantmentLevel() + 1);
                player.setLevel(playerLevel - 1);
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
            } else {
                item.addUnsafeEnchantment(selectedOffer.getEnchantment(), selectedOffer.getEnchantmentLevel());
                player.setLevel(playerLevel - 1);
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
            }
        }
    }

    public static void applyCustomEnchant(Player player, CustomEnchantmentOffer selectedOfferCustom, ItemStack item, TextColor color) {
        HashMap<String, Integer> Registry = Constants.getCustomEnchantments();

        String enchantmentName = selectedOfferCustom.getEnchant().getName();
        int enchantmentLevel = selectedOfferCustom.getEnchantmentLevel();
        int prevLevel = getEnchantmentLevel(item, enchantmentName);
        int maxEnchantmentLevel = Registry.get(enchantmentName);

        int requiredLevel = selectedOfferCustom.getCost();
        int playerLevel = player.getLevel();
        if(playerLevel >= requiredLevel) {
            if(prevLevel == enchantmentLevel && prevLevel == maxEnchantmentLevel) return;
            addEnchantment(item, enchantmentName, enchantmentLevel, color);
            player.setLevel(playerLevel - 1);
            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        }
    }

    public static void applyExtraEnchant(JavaPlugin plugin, Inventory inv, Player player, CustomEnchantmentOffer selectedOfferCustom, ItemStack item, TextColor color) {
        int requiredLevel = selectedOfferCustom.getCost();
        int playerLevel = player.getLevel();
        if(playerLevel < requiredLevel) return;
        applyCustomEnchant(player, selectedOfferCustom, item, color);
        String baseCommand = selectedOfferCustom.getEnchant().getAddCmd();
        String command = baseCommand.replace("enchant", "enchant " + player.getName());
        String finalCommand = command + " " + selectedOfferCustom.getEnchantmentLevel();

        inv.close();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), finalCommand);
        }, 1L);
    }

    public static void removeExtraEnchant(JavaPlugin plugin, Inventory inv, Player player, ItemStack item, CustomEnchantment enchant) {
        removeEnchantment(item, enchant.getName());
        String baseCommand = enchant.getRmCmd();
        String finalCommand = baseCommand.replace("unenchant", "unenchant " + player.getName());
        inv.close();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), finalCommand);
        }, 1L);
    }

    public static int countSurroundingEffectiveBlocks(Block centerBlock, Material checkBlockType) {
        int count = 0;
        // Coordinates relative to the enchanting table for all 24 valid surrounding positions
        int[][] relativePositions = {
            {-1, 0, 2}, {0, 0, 2}, {1, 0, 2},
            {2, 0, 1}, {2, 0, 0}, {2, 0, -1},
            {1, 0, -2}, {0, 0, -2}, {-1, 0, -2},
            {-2, 0, -1}, {-2, 0, 0}, {-2, 0, 1},
            {-1, 1, 2}, {0, 1, 2}, {1, 1, 2},
            {2, 1, 1}, {2, 1, 0}, {2, 1, -1},
            {1, 1, -2}, {0, 1, -2}, {-1, 1, -2},
            {-2, 1, -1}, {-2, 1, 0}, {-2, 1, 1}
        };

        for (int[] pos : relativePositions) {
            Block checkBlock = centerBlock.getRelative(pos[0], pos[1], pos[2]);
            if (checkBlock.getType() == checkBlockType) {
                Block airCheck1 = centerBlock.getRelative(pos[0], 1, pos[2]); // Directly above enchanting table
                Block airCheck2 = centerBlock.getRelative(pos[0], 2, pos[2]); // One above the bookshelf level
                if ((airCheck1.getType() == Material.AIR || airCheck1.getType() == checkBlockType) && 
                    airCheck2.getType() == Material.AIR) {
                    count++;
                }
            }
        }
        if(count == 0) return 1;
        return count;
    }
}
