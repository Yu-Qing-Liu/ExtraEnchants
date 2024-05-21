package com.github.yuqingliu.extraenchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.anvil.AnvilManager;
import com.github.yuqingliu.extraenchants.configuration.implementations.AnvilConstants;
import com.github.yuqingliu.extraenchants.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.item.ItemUtils;

public class AnvilMenu {
    private ItemUtils itemUtils;
    private AnvilManager anvilManager;
    private double REPAIR_COST_PER_RESOURCE = 1.5;
    private double ANVIL_COST_PER_LEVEL = 2;
    private int COST = 0;
    private final int RESULT_SLOT = 15;
    private final int RESULT_PLACEHOLDER = 24;
    private final List<Integer> SLOTS = Arrays.asList(11, 13, 15, 20, 22, 24);
    private final List<Integer> PLACEHOLDER_SLOTS = Arrays.asList(20, 22);

    public AnvilMenu(ItemUtils itemUtils, AnvilManager anvilManager, AnvilConstants anvilConstants) {
        this.itemUtils = itemUtils;
        this.anvilManager = anvilManager;
        this.REPAIR_COST_PER_RESOURCE = anvilConstants.getAnvilRepairCostPerResource();
        this.ANVIL_COST_PER_LEVEL = anvilConstants.getAnvilCostPerLevel();
    }

    public void openAnvilMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, Component.text("Anvil", NamedTextColor.DARK_GRAY));
        displayItemFrame(inv);
        this.COST = 0;
        player.openInventory(inv);
    }

    public void displayItemFrame(Inventory inv) {
        List<Integer> frame = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            if(!SLOTS.contains(i)) {
                frame.add(i);
            }
        }
        for (int slot : frame) {
            ItemStack frameTiles = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta frameMeta = frameTiles.getItemMeta();
            if (frameMeta != null) { 
                frameMeta.displayName(Component.text("Unavailable", NamedTextColor.DARK_PURPLE));
                frameTiles.setItemMeta(frameMeta);
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

    private boolean canRepair(ItemStack repairable, ItemStack repaired) {
        // Enable string to repair ranged weapons
        if(repairable.getType() == Material.STRING && (repaired.getType() == Material.BOW || repaired.getType() == Material.CROSSBOW)) {
            return true;
        } 
        return repairable.canRepair(repaired);
    }

    public void updateResult(JavaPlugin plugin, Player player, Inventory inv, ItemStack leftItem, ItemStack rightItem, boolean recursion) {
        ItemStack Result = leftItem.clone();
        this.COST = 0;
        
        if(canRepair(leftItem, rightItem)) {
            this.COST += repairItem(Result, leftItem.getAmount());
            if(player.getLevel() >= COST) {
                inv.setItem(RESULT_SLOT, Result);
            } 
        } else if(canRepair(rightItem, leftItem)) {
            this.COST += repairItem(Result, rightItem.getAmount());
            if(player.getLevel() >= COST) {
                inv.setItem(RESULT_SLOT, Result);
            } 
        }

        if(!isCompatible(leftItem, rightItem)) {
            return;
        } 
        Result = applyEnchants(Result, itemUtils.getEnchantments(leftItem), itemUtils.getEnchantments(rightItem));
    
        if(this.COST == 0 && !recursion) {
            updateResult(plugin, player, inv, rightItem, leftItem, true);
        } 

        repairIdenticalItem(Result, rightItem);

        if(player.getLevel() >= this.COST) {
            placeHolder(inv, RESULT_PLACEHOLDER);
            inv.setItem(RESULT_SLOT, Result);
        } else {
            placeHolderWarning(inv, RESULT_PLACEHOLDER);
        }
    }

    private void placeHolderWarning(Inventory inv, int slot) {
        ItemStack anvilPlaceholder = new ItemStack(Material.ANVIL);
        ItemMeta anvilPlaceholderMeta = anvilPlaceholder.getItemMeta();
        if (anvilPlaceholderMeta != null) {
            anvilPlaceholderMeta.displayName(Component.text("Not Enough Experience", NamedTextColor.RED));
            anvilPlaceholder.setItemMeta(anvilPlaceholderMeta);
        }
        inv.setItem(slot, anvilPlaceholder);
    }

    private void placeHolder(Inventory inv, int slot) {
        ItemStack anvilPlaceholder = new ItemStack(Material.ANVIL);
        ItemMeta anvilPlaceholderMeta = anvilPlaceholder.getItemMeta();
        if (anvilPlaceholderMeta != null) {
            anvilPlaceholderMeta.displayName(Component.text("COST: " + COST, NamedTextColor.GREEN));
            anvilPlaceholder.setItemMeta(anvilPlaceholderMeta);
        }
        inv.setItem(slot, anvilPlaceholder);
    }

    private boolean isCompatible(ItemStack item1, ItemStack item2) {
        Material M1 = item1.getType();
        Material M2 = item2.getType();

        Map<Material, List<Material>> anvilRegistry = anvilManager.getAnvilRegistry();
        
        // Books are allowed to interact with any
        if(M1 == Material.ENCHANTED_BOOK || M2 == Material.ENCHANTED_BOOK) return true;
        
        // Check anvil register for custom combos
        List<Material> applicable = anvilRegistry.get(M1);
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

    private int repairItem(ItemStack item, int numResources) {
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

    private void repairIdenticalItem(ItemStack item1, ItemStack item2) {
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

    public boolean applyCost(Player player) {
        int playerLevel = player.getLevel();
        if(playerLevel >= this.COST) {
            player.setLevel(playerLevel - this.COST);
            // Play anvil sound
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
            return true;
        }
        return false;
    }

    private ItemStack applyEnchants(ItemStack result, Map<Enchantment, Integer> itemEnchants, Map<Enchantment, Integer> sacrificeEnchants) {
        int levels = 0;
        Set<Enchantment> keys = new HashSet<>(itemEnchants.keySet());
        keys.addAll(sacrificeEnchants.keySet());
        ItemStack finalItem = result.clone();
        for(Enchantment enchant : keys) {
            int maxEnchantmentLevel = enchant.getMaxLevel();
            int itemEnchantLevel = 0;
            int sacrificeEnchantLevel = 0;
            if(itemEnchants.containsKey(enchant)) {
                itemEnchantLevel = itemEnchants.get(enchant);
            } 
            if(sacrificeEnchants.containsKey(enchant)) {
                sacrificeEnchantLevel = sacrificeEnchants.get(enchant);
            } 
            if(sacrificeEnchantLevel > itemEnchantLevel && enchant.canEnchant(finalItem)) {
                finalItem = enchant.removeEnchantment(finalItem);
                finalItem = enchant.applyEnchantment(finalItem, sacrificeEnchantLevel);
                levels += sacrificeEnchantLevel - itemEnchantLevel;
            } else if (sacrificeEnchantLevel == itemEnchantLevel && sacrificeEnchantLevel + 1 <= maxEnchantmentLevel && enchant.canEnchant(finalItem)) {
                finalItem = enchant.removeEnchantment(finalItem);
                finalItem = enchant.applyEnchantment(finalItem, sacrificeEnchantLevel + 1);
                levels += 1;
            }
        }
        this.COST += (int) (levels * ANVIL_COST_PER_LEVEL);
        if(finalItem == null) {
            return result;
        }
        return finalItem;
    }
}
