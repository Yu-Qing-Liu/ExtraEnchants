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
import org.bukkit.enchantments.Enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.lang.String;

import com.github.yuqingliu.extraenchants.enchants.*;

import de.tr7zw.changeme.nbtapi.NBTItem;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class UtilityMethods {
    private static HashMap<NamespacedKey, List<Object>> EnchantmentsRegistry = Constants.getEnchantments();
    private static HashMap<String, List<Object>> CustomEnchantmentsRegistry = Constants.getCustomEnchantments();

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

    public static boolean hasVanillaEnchantment(ItemStack item, Enchantment enchant) {
        return item != null && item.getItemMeta() != null && item.getItemMeta().hasEnchant(enchant);
    }


    public static int getEnchantmentLevel(ItemStack item, String enchantmentName) {
        if(item == null || item.getType() == Material.AIR) return 0;
        NBTItem nbtItem = new NBTItem(item);
        int level = 0;
        if (nbtItem != null && nbtItem.hasTag("extra-enchants"+enchantmentName)) {
            level = nbtItem.getInteger("extra-enchants"+enchantmentName);
        }
        return level;
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

    public static ItemStack addEnchantment(ItemStack item, String enchantmentName, int level, TextColor color, boolean editLore) {
        if(item == null || item.getType() == Material.AIR) return null;
        List<CustomEnchantment> Register = Database.getCustomEnchantmentRegistry();
        CustomEnchantment enchant = null;
        for (CustomEnchantment enchantment : Register) {
            if(enchantment.getName().equals(enchantmentName)) {
                enchant = enchantment;
                break;
            } 
        }
        if(enchant == null) return null;
        String description = enchant.getDescription();
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
                    return null;
                }
                // Remove the old enchantment lore and description
                if(editLore) {
                    existingLore = existingLore.stream()
                            .filter(loreComponent -> !plainTextSerializer.serialize(loreComponent).contains(enchantmentName) && !plainTextSerializer.serialize(loreComponent).contains(description))
                            .collect(Collectors.toList());
                }
            }

            // Add lore
            if(editLore) {
                // Add or upgrade the enchantment
                Component name_component = Component.text(enchantmentName + " " + toRoman(level), color);
                Component description_component = Component.text(description, NamedTextColor.GRAY);
                existingLore.add(name_component);
                existingLore.add(description_component);

                // Update the lore
                meta.lore(existingLore);
                item.setItemMeta(meta);
            }

            // Add nbt tag
            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setInteger("extra-enchants"+enchantmentName, level);

            return nbtItem.getItem();
        }
        return null;
    }

    public static ItemStack removeEnchantment(ItemStack item, String enchantmentName, boolean editLore) {
        if(item == null || item.getType() == Material.AIR) return null;
        List<CustomEnchantment> Register = Database.getCustomEnchantmentRegistry();
        CustomEnchantment enchant = null;
        for (CustomEnchantment enchantment : Register) {
            if(enchantment.getName().equals(enchantmentName)) {
                enchant = enchantment;
                break;
            } 
        }
        if(enchant == null) return null;
        String description = enchant.getDescription();

        // Remove lore
        if(editLore) {
            ItemMeta meta = item.getItemMeta();
            List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
            PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();
            existingLore = existingLore.stream()
                    .filter(loreComponent -> !plainTextSerializer.serialize(loreComponent).contains(enchantmentName) && !plainTextSerializer.serialize(loreComponent).contains(description))
                    .collect(Collectors.toList());
            meta.lore(existingLore);
            item.setItemMeta(meta);
        }

        // Remove nbt tag
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasTag("extra-enchants"+enchantmentName)) {
            nbtItem.removeKey("extra-enchants"+enchantmentName);
        }
        return nbtItem.getItem();
    }

    public static boolean applyVanillaEnchant(Player player, EnchantmentOffer selectedOffer, ItemStack item) {
        NamespacedKey enchantmentName = selectedOffer.getEnchantment().getKey();
        int enchantmentLevel = selectedOffer.getEnchantmentLevel();
        int prevEnchantLevel = item.getEnchantmentLevel(selectedOffer.getEnchantment());
        int maxEnchantmentLevel = (int) EnchantmentsRegistry.get(enchantmentName).get(0);

        int requiredLevel = selectedOffer.getCost();
        int playerLevel = player.getLevel();
        if(playerLevel >= requiredLevel) {
            if(prevEnchantLevel > selectedOffer.getEnchantmentLevel()) {
                return true;
            } else if(prevEnchantLevel == enchantmentLevel) {
                if(prevEnchantLevel == maxEnchantmentLevel) return true;
                item.addUnsafeEnchantment(selectedOffer.getEnchantment(), selectedOffer.getEnchantmentLevel() + 1);
                player.setLevel(playerLevel - 1);
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                return true;
            } else {
                item.addUnsafeEnchantment(selectedOffer.getEnchantment(), selectedOffer.getEnchantmentLevel());
                player.setLevel(playerLevel - 1);
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                return true;
            }
        }
        return false;
    }

    public static ItemStack applyCustomEnchant(Player player, CustomEnchantmentOffer selectedOfferCustom, ItemStack item, TextColor color) {
        if(item == null || item.getType() == Material.AIR) return null;
        String enchantmentName = selectedOfferCustom.getEnchant().getName();
        int enchantmentLevel = selectedOfferCustom.getEnchantmentLevel();
        int prevLevel = getEnchantmentLevel(item, enchantmentName);
        int maxEnchantmentLevel = (int) CustomEnchantmentsRegistry.get(enchantmentName).get(0);
        int playerLevel = player.getLevel();
        if(prevLevel == enchantmentLevel && prevLevel == maxEnchantmentLevel) return null;
        player.setLevel(playerLevel - 1);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        return addEnchantment(item, enchantmentName, enchantmentLevel, color, true);
    }

    public static ItemStack applyExtraEnchant(JavaPlugin plugin, Inventory inv, int itemSlot, Player player, CustomEnchantmentOffer selectedOfferCustom, ItemStack item, TextColor color) {
        if(item == null || item.getType() == Material.AIR) return null;
        String enchantmentName = selectedOfferCustom.getEnchant().getName();
        int enchantmentLevel = selectedOfferCustom.getEnchantmentLevel();
        int prevLevel = getEnchantmentLevel(item, enchantmentName);
        int maxEnchantmentLevel = (int) CustomEnchantmentsRegistry.get(enchantmentName).get(0);
        int playerLevel = player.getLevel();
        if(prevLevel == enchantmentLevel && prevLevel == maxEnchantmentLevel) return null;
        ItemStack enchantedItem = addEnchantment(item, enchantmentName, enchantmentLevel, color, false);
        String baseCommand = selectedOfferCustom.getEnchant().getAddCmd();
        String command = baseCommand.replace("enchant", "enchant " + player.getName());
        String finalCommand = command + " " + selectedOfferCustom.getEnchantmentLevel();
        // Move item to player's main hand
        ItemStack originalItem = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(enchantedItem);
        // Send command
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), finalCommand);
        // Move item to slot
        ItemStack finalItem = player.getInventory().getItemInMainHand();
        inv.setItem(itemSlot, finalItem);
        player.getInventory().setItemInMainHand(originalItem);
        player.setLevel(playerLevel - 1);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        return finalItem;
    }

    public static ItemStack addExtraEnchant(JavaPlugin plugin, Inventory inv, int itemSlot, Player player, CustomEnchantment enchant, int level, ItemStack item, TextColor color) {
        if(item == null || item.getType() == Material.AIR) return null;
        String enchantmentName = enchant.getName();
        ItemStack enchantedItem = addEnchantment(item, enchantmentName, level, color, false);
        String baseCommand = enchant.getAddCmd();
        String command = baseCommand.replace("enchant", "enchant " + player.getName());
        String finalCommand = command + " " + level;
        // Move item to player's main hand
        ItemStack originalItem = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(enchantedItem);
        // Send command
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), finalCommand);
        // Move item to slot
        ItemStack finalItem = player.getInventory().getItemInMainHand();
        inv.setItem(itemSlot, finalItem);
        player.getInventory().setItemInMainHand(originalItem);
        return finalItem;
    }

    public static ItemStack removeExtraEnchant(JavaPlugin plugin, Inventory inv, int itemSlot, Player player, ItemStack item, CustomEnchantment enchant) {
        if(item == null || item.getType() == Material.AIR) return null;
        ItemStack enchantedItem = removeEnchantment(item, enchant.getName(), false);
        String baseCommand = enchant.getRmCmd();
        String finalCommand = baseCommand.replace("unenchant", "unenchant " + player.getName());
        // Move item to player's main hand
        ItemStack originalItem = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(enchantedItem);
        // Send command
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), finalCommand);
        // Move item to slot
        ItemStack finalItem = player.getInventory().getItemInMainHand();
        inv.setItem(itemSlot, finalItem);
        player.getInventory().setItemInMainHand(originalItem);
        return finalItem;
    }

    public static int evaluateExpression(String expression, int value) {
        Expression exp = new ExpressionBuilder(expression)
            .variables("x")
            .build()
            .setVariable("x", value); // Set value for x

        // Evaluate the expression
        double result = exp.evaluate();
        return (int) result;
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
