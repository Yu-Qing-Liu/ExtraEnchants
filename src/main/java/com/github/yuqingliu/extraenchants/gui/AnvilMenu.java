package com.github.yuqingliu.extraenchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Sound;
import org.bukkit.NamespacedKey;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.database.*;
import com.github.yuqingliu.extraenchants.utils.*;

public class AnvilMenu {
    private static final int MAX_IRON_BLOCKS = 15;
    private static int IRON_BLOCKS = 1;
    private static final int IRON_BLOCK_MULTIPLIER = 5;
    private static final int VANILLA_COST_PER_LEVEL = 5;
    private static final int CUSTOM_COST_PER_LEVEL = 10;
    private static int COST = 0;
    private static final int RESULT_SLOT = 15;
    private static final List<Integer> SLOTS = Arrays.asList(11, 13, 15, 20, 22, 24);
    private static final List<Integer> PLACEHOLDER_SLOTS = Arrays.asList(20, 22, 24);


    public static void openAnvilMenu(Player player, int ironBlocks) {
        Inventory inv = Bukkit.createInventory(null, 36, Component.text("Anvil", NamedTextColor.DARK_GRAY));
        IRON_BLOCKS = Math.min(ironBlocks, MAX_IRON_BLOCKS);
        displayItemFrame(inv);
        COST = 0;
        player.openInventory(inv);
    }

    public static void displayItemFrame(Inventory inv) {
        List<Integer> frame = new ArrayList<>();
        for (int i = 0; i < 36; i++) {if(!SLOTS.contains(i)) frame.add(i);}
        for (int slot : frame) {
            ItemStack frameTiles = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta frameMeta = frameTiles.getItemMeta();
            if (frameMeta != null) { // Check for null to prevent potential NullPointerException
                frameMeta.displayName(Component.text("Unavailable", NamedTextColor.DARK_PURPLE));
                frameTiles.setItemMeta(frameMeta); // Set the modified meta back on the ItemStack
            }
            inv.setItem(slot, frameTiles);
        }
        
        ItemStack anvilPlaceholder = new ItemStack(Material.ANVIL);
        ItemMeta anvilPlaceholderMeta = anvilPlaceholder.getItemMeta();
        if (anvilPlaceholderMeta != null) {
            anvilPlaceholderMeta.displayName(Component.text("Anvil Slot", NamedTextColor.GREEN));
            anvilPlaceholder.setItemMeta(anvilPlaceholderMeta);
        }
        
        for(int i : PLACEHOLDER_SLOTS) {inv.setItem(i, anvilPlaceholder);}

        ItemStack resultPlaceholder = new ItemStack(Material.BARRIER);
        ItemMeta resultPlaceholderMeta = resultPlaceholder.getItemMeta();
        if (resultPlaceholderMeta != null) {
            resultPlaceholderMeta.displayName(Component.text("Unavailable", NamedTextColor.RED));
            resultPlaceholder.setItemMeta(resultPlaceholderMeta);
        }
        inv.setItem(RESULT_SLOT, resultPlaceholder);
    }

    public static void updateResult(Inventory inv, ItemStack leftItem, ItemStack rightItem) {
        ItemStack Result = leftItem.clone();
        COST = 0;

        int anvilPower = IRON_BLOCKS * IRON_BLOCK_MULTIPLIER;
        int vanillaCost = applyVanillaEnchants(Result, leftItem.getEnchantments(), rightItem.getEnchantments());
        int customCost = applyCustomEnchants(Result, UtilityMethods.getEnchantments(leftItem), UtilityMethods.getEnchantments(rightItem));
    
        if(vanillaCost + customCost == 0) return;
        COST = vanillaCost * VANILLA_COST_PER_LEVEL + customCost * CUSTOM_COST_PER_LEVEL;
        
        if(anvilPower > COST) {
            inv.setItem(RESULT_SLOT, Result);
        }
    }

    public static boolean applyCost(Player player) {
        int playerLevel = player.getLevel();
        if(playerLevel > COST) {
            player.setLevel(playerLevel - COST);
            // Play anvil sound
            return true;
        }
        return false;
    }

    private static int applyVanillaEnchants(ItemStack result, Map<Enchantment, Integer> ItemVanillaEnchants, Map<Enchantment, Integer> SacrificeVanillaEnchants) {
        int cost = 0;
        Set<Enchantment> keys = new HashSet<>(ItemVanillaEnchants.keySet());
        keys.addAll(SacrificeVanillaEnchants.keySet());
        HashMap<NamespacedKey, Integer> Registry = Constants.getEnchantments();

        for(Enchantment enchant : keys) {
            int maxEnchantmentLevel = Registry.get(enchant.getKey());
            int itemVanillaEnchantLevel = 0;
            int sacrificeVanillaEnchantLevel = 0;
            if(ItemVanillaEnchants.get(enchant) != null) itemVanillaEnchantLevel = ItemVanillaEnchants.get(enchant);
            if(SacrificeVanillaEnchants.get(enchant) != null) sacrificeVanillaEnchantLevel = SacrificeVanillaEnchants.get(enchant);

            if(sacrificeVanillaEnchantLevel > itemVanillaEnchantLevel && enchant.canEnchantItem(result)) {
                result.removeEnchantment(enchant);
                result.addUnsafeEnchantment(enchant, sacrificeVanillaEnchantLevel);
                cost += sacrificeVanillaEnchantLevel - itemVanillaEnchantLevel;
            } else if (sacrificeVanillaEnchantLevel == itemVanillaEnchantLevel && sacrificeVanillaEnchantLevel + 1 <= maxEnchantmentLevel && enchant.canEnchantItem(result)) {
                result.removeEnchantment(enchant);
                result.addUnsafeEnchantment(enchant, sacrificeVanillaEnchantLevel + 1);
                cost += 1;
            }
        }
        return cost;
    }

    private static int applyCustomEnchants(ItemStack result, Map<CustomEnchantment, Integer> ItemCustomEnchants, Map<CustomEnchantment, Integer> SacrificeCustomEnchants) {
        int cost = 0;
        Set<CustomEnchantment> keys = new HashSet<>(ItemCustomEnchants.keySet());
        keys.addAll(SacrificeCustomEnchants.keySet());
        HashMap<String, Integer> Registry = Constants.getCustomEnchantments();

        for(CustomEnchantment enchant : keys) {
            int maxEnchantmentLevel = Registry.get(enchant.getName());
            int itemVanillaEnchantLevel = 0;
            int sacrificeVanillaEnchantLevel = 0;
            if(ItemCustomEnchants.get(enchant) != null) itemVanillaEnchantLevel = ItemCustomEnchants.get(enchant);
            if(SacrificeCustomEnchants.get(enchant) != null) sacrificeVanillaEnchantLevel = SacrificeCustomEnchants.get(enchant);

            if(sacrificeVanillaEnchantLevel > itemVanillaEnchantLevel && enchant.canEnchant(result)) {
                UtilityMethods.removeEnchantment(result, enchant.getName());
                UtilityMethods.addEnchantment(result, enchant.getName(), sacrificeVanillaEnchantLevel);
                cost += sacrificeVanillaEnchantLevel - itemVanillaEnchantLevel;
            } else if (sacrificeVanillaEnchantLevel == itemVanillaEnchantLevel && sacrificeVanillaEnchantLevel + 1 <= maxEnchantmentLevel && enchant.canEnchant(result)) {
                UtilityMethods.removeEnchantment(result, enchant.getName());
                UtilityMethods.addEnchantment(result, enchant.getName(), sacrificeVanillaEnchantLevel + 1);
                cost += 1;
            }
        }
        return cost;
    }
}


