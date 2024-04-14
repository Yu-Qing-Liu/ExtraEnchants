package com.github.yuqingliu.extraenchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.enchants.*;
import com.github.yuqingliu.extraenchants.enchants.utils.*;

public class AnvilMenu {
    private static final double REPAIR_COST_PER_RESOURCE = Constants.getRepairAnvilCostPerResource();
    private static final int ANVIL_COST_PER_LEVEL = Constants.getAnvilCostPerLevel();
    private static int COST = 0;
    private static final int RESULT_SLOT = 15;
    private static final int RESULT_PLACEHOLDER = 24;
    private static final List<Integer> SLOTS = Arrays.asList(11, 13, 15, 20, 22, 24);
    private static final List<Integer> PLACEHOLDER_SLOTS = Arrays.asList(20, 22);
    private static final HashMap<NamespacedKey, List<Object>> EnchantmentsRegistry = Constants.getEnchantments();
    private static final HashMap<String, List<Object>> CustomEnchantmentsRegistry = Constants.getCustomEnchantments();

    public static void openAnvilMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, Component.text("Anvil", NamedTextColor.DARK_GRAY));
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

        inv.setItem(RESULT_PLACEHOLDER, anvilPlaceholder);

        ItemStack slotPlaceholder = new ItemStack(Material.IRON_BLOCK);
        ItemMeta slotPlaceholderMeta = slotPlaceholder.getItemMeta();
        if (slotPlaceholderMeta != null) {
            slotPlaceholderMeta.displayName(Component.text("Anvil Slot", NamedTextColor.GREEN));
            slotPlaceholder.setItemMeta(slotPlaceholderMeta);
        }
        
        for(int i : PLACEHOLDER_SLOTS) {inv.setItem(i, slotPlaceholder);}

        ItemStack resultPlaceholder = new ItemStack(Material.BARRIER);
        ItemMeta resultPlaceholderMeta = resultPlaceholder.getItemMeta();
        if (resultPlaceholderMeta != null) {
            resultPlaceholderMeta.displayName(Component.text("Unavailable", NamedTextColor.RED));
            resultPlaceholder.setItemMeta(resultPlaceholderMeta);
        }
        inv.setItem(RESULT_SLOT, resultPlaceholder);
    }

    private static boolean canRepair(ItemStack repairable, ItemStack repaired) {
        // Enable string to repair ranged weapons
        if(repairable.getType() == Material.STRING && (repaired.getType() == Material.BOW || repaired.getType() == Material.CROSSBOW)) return true;

        return repairable.canRepair(repaired);
    }

    public static void updateResult(JavaPlugin plugin, Player player, Inventory inv, ItemStack leftItem, ItemStack rightItem, boolean recursion) {
        ItemStack Result = leftItem.clone();
        COST = 0;
        
        if(canRepair(leftItem, rightItem)) {
            COST += repairItem(Result, leftItem.getAmount());
            if(player.getLevel() >= COST) inv.setItem(RESULT_SLOT, Result);
        } else if(canRepair(rightItem, leftItem)) {
            COST += repairItem(Result, rightItem.getAmount());
            if(player.getLevel() >= COST) inv.setItem(RESULT_SLOT, Result);
        }

        if(!isCompatible(leftItem, rightItem)) return;

        applyVanillaEnchants(Result, leftItem.getEnchantments(), rightItem.getEnchantments());
        Result = applyCustomEnchants(plugin, inv, player, Result, UtilityMethods.getEnchantments(leftItem), UtilityMethods.getEnchantments(rightItem));
    
        if(COST == 0 && !recursion) {
            updateResult(plugin, player, inv, rightItem, leftItem, true);
        } 

        repairIdenticalItem(Result, rightItem);
        COST = COST * ANVIL_COST_PER_LEVEL;

        if(player.getLevel() >= COST) {
            placeHolder(inv, RESULT_PLACEHOLDER);
            inv.setItem(RESULT_SLOT, Result);
        } else {
            placeHolderWarning(inv, RESULT_PLACEHOLDER);
        }
    }

    private static void placeHolderWarning(Inventory inv, int slot) {
        ItemStack anvilPlaceholder = new ItemStack(Material.ANVIL);
        ItemMeta anvilPlaceholderMeta = anvilPlaceholder.getItemMeta();
        if (anvilPlaceholderMeta != null) {
            anvilPlaceholderMeta.displayName(Component.text("Not Enough Experience", NamedTextColor.RED));
            anvilPlaceholder.setItemMeta(anvilPlaceholderMeta);
        }
        inv.setItem(slot, anvilPlaceholder);
    }

    private static void placeHolder(Inventory inv, int slot) {
        ItemStack anvilPlaceholder = new ItemStack(Material.ANVIL);
        ItemMeta anvilPlaceholderMeta = anvilPlaceholder.getItemMeta();
        if (anvilPlaceholderMeta != null) {
            anvilPlaceholderMeta.displayName(Component.text("COST: " + COST, NamedTextColor.GREEN));
            anvilPlaceholder.setItemMeta(anvilPlaceholderMeta);
        }
        inv.setItem(slot, anvilPlaceholder);
    }

    private static boolean isCompatible(ItemStack item1, ItemStack item2) {
        Material M1 = item1.getType();
        Material M2 = item2.getType();

        HashMap<Material, List<Material>> anvilRegister = Constants.getAnvilData();
        
        // Books are allowed to interact with any
        if(M1 == Material.BOOK || M1 == Material.ENCHANTED_BOOK || M2 == Material.BOOK || M2 == Material.ENCHANTED_BOOK) return true;
        
        // Check anvil register for custom combos
        List<Material> applicable = anvilRegister.get(M1);
        if(applicable != null && applicable.size() > 0) {
            if(applicable.contains(M2)) {
                return true;
            }
        }

        // Check if both items are made of the same material, which makes them compatible
        if (M1 == M2) {
            return true;
        }

        // If none of the above conditions are met, the items are not compatible
        return false;
    }

    private static int repairItem(ItemStack item, int numResources) {
        int maxDurability = item.getType().getMaxDurability();
        int repairAmount = (int) ((double) maxDurability / 4.0) * numResources;

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;
                int damage = damageable.getDamage();

                // Calculate new damage, ensuring it does not go below 0
                int newDamage = Math.max(0, damage - repairAmount);

                // Apply the new damage value
                damageable.setDamage(newDamage);

                item.setItemMeta(meta);
            }
        }
        return (int) (numResources * REPAIR_COST_PER_RESOURCE);
    }

    private static void repairIdenticalItem(ItemStack item1, ItemStack item2) {
        int maxDurability = item1.getType().getMaxDurability();

        if (item1.hasItemMeta() && item2.hasItemMeta()) {
            ItemMeta meta1 = item1.getItemMeta();
            ItemMeta meta2 = item2.getItemMeta();
            if (meta1 instanceof Damageable && meta2 instanceof Damageable) {
                Damageable damageable1 = (Damageable) meta1;
                Damageable damageable2 = (Damageable) meta2; // Corrected to meta2
                int damage1 = damageable1.getDamage();
                int damage2 = damageable2.getDamage();

                // Sum of the remaining durabilities of both items
                int totalDurability = (maxDurability - damage1) + (maxDurability - damage2);

                // Adding a repair bonus of 5%, ensuring it doesn't exceed the max durability
                int repairBonus = (int) (maxDurability * 0.05);
                totalDurability += repairBonus;
                totalDurability = Math.min(totalDurability, maxDurability);

                // Calculate the new damage value for item1
                int newDamage = maxDurability - totalDurability;

                // Apply the new damage value
                damageable1.setDamage(newDamage);

                // Set the updated meta back to item1
                item1.setItemMeta(meta1);
            }
        }
    }

    public static boolean applyCost(Player player) {
        int playerLevel = player.getLevel();
        if(playerLevel >= COST) {
            player.setLevel(playerLevel - COST);
            // Play anvil sound
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
            return true;
        }
        return false;
    }

    private static void applyVanillaEnchants(ItemStack result, Map<Enchantment, Integer> ItemVanillaEnchants, Map<Enchantment, Integer> SacrificeVanillaEnchants) {
        Set<Enchantment> keys = new HashSet<>(ItemVanillaEnchants.keySet());
        keys.addAll(SacrificeVanillaEnchants.keySet());

        for(Enchantment enchant : keys) {
            int maxEnchantmentLevel = (int) EnchantmentsRegistry.get(enchant.getKey()).get(0);
            int itemVanillaEnchantLevel = 0;
            int sacrificeVanillaEnchantLevel = 0;
            if(ItemVanillaEnchants.get(enchant) != null) itemVanillaEnchantLevel = ItemVanillaEnchants.get(enchant);
            if(SacrificeVanillaEnchants.get(enchant) != null) sacrificeVanillaEnchantLevel = SacrificeVanillaEnchants.get(enchant);

            if(sacrificeVanillaEnchantLevel > itemVanillaEnchantLevel && enchant.canEnchantItem(result)) {
                result.removeEnchantment(enchant);
                result.addUnsafeEnchantment(enchant, sacrificeVanillaEnchantLevel);
                COST += sacrificeVanillaEnchantLevel - itemVanillaEnchantLevel;
            } else if (sacrificeVanillaEnchantLevel == itemVanillaEnchantLevel && sacrificeVanillaEnchantLevel + 1 <= maxEnchantmentLevel && enchant.canEnchantItem(result)) {
                result.removeEnchantment(enchant);
                result.addUnsafeEnchantment(enchant, sacrificeVanillaEnchantLevel + 1);
                COST += 1;
            }
        }
    }

    private static ItemStack applyCustomEnchants(JavaPlugin plugin, Inventory inv, Player player, ItemStack result, Map<CustomEnchantment, Integer> ItemCustomEnchants, Map<CustomEnchantment, Integer> SacrificeCustomEnchants) {
        Set<CustomEnchantment> keys = new HashSet<>(ItemCustomEnchants.keySet());
        keys.addAll(SacrificeCustomEnchants.keySet());
        ItemStack finalItem = result.clone();
        for(CustomEnchantment enchant : keys) {
            int maxEnchantmentLevel = (int) CustomEnchantmentsRegistry.get(enchant.getName()).get(0);
            int itemVanillaEnchantLevel = 0;
            int sacrificeVanillaEnchantLevel = 0;
            if(ItemCustomEnchants.get(enchant) != null) itemVanillaEnchantLevel = ItemCustomEnchants.get(enchant);
            if(SacrificeCustomEnchants.get(enchant) != null) sacrificeVanillaEnchantLevel = SacrificeCustomEnchants.get(enchant);

            if(sacrificeVanillaEnchantLevel > itemVanillaEnchantLevel && enchant.canEnchant(finalItem)) {
                if(enchant.getAddCmd() != null && !enchant.getAddCmd().isEmpty()) {
                    finalItem = UtilityMethods.removeExtraEnchant(plugin, inv, RESULT_SLOT, player, finalItem, enchant);
                    finalItem = UtilityMethods.addExtraEnchant(plugin, inv, RESULT_SLOT, player, enchant, sacrificeVanillaEnchantLevel, finalItem, enchant.getColor());
                } else {
                    finalItem = UtilityMethods.removeEnchantment(finalItem, enchant.getName(), true);
                    finalItem = UtilityMethods.addEnchantment(finalItem, enchant.getName(), sacrificeVanillaEnchantLevel, enchant.getColor(), true);
                }
                COST += sacrificeVanillaEnchantLevel - itemVanillaEnchantLevel;
            } else if (sacrificeVanillaEnchantLevel == itemVanillaEnchantLevel && sacrificeVanillaEnchantLevel + 1 <= maxEnchantmentLevel && enchant.canEnchant(finalItem)) {
                if(enchant.getAddCmd() != null && !enchant.getAddCmd().isEmpty()) {
                    finalItem = UtilityMethods.removeExtraEnchant(plugin, inv, RESULT_SLOT, player, finalItem, enchant);
                    finalItem = UtilityMethods.addExtraEnchant(plugin, inv, RESULT_SLOT, player, enchant, sacrificeVanillaEnchantLevel + 1, finalItem, enchant.getColor());
                } else {
                    finalItem = UtilityMethods.removeEnchantment(finalItem, enchant.getName(), true);
                    finalItem = UtilityMethods.addEnchantment(finalItem, enchant.getName(), sacrificeVanillaEnchantLevel + 1, enchant.getColor(), true);
                }
                COST += 1;
            }
        }

        if(finalItem == null) {
            return result;
        }
        return finalItem;
    }
}
