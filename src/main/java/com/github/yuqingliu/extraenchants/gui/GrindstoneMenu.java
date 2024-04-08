package com.github.yuqingliu.extraenchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.EnchantmentOffer;
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
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import com.github.yuqingliu.extraenchants.database.*;
import com.github.yuqingliu.extraenchants.utils.*;

public class GrindstoneMenu {
    private static final int START_SLOT = 0;
    private static final int PLACEHOLDER_SLOT = 34;
    private static HashMap<Integer,Enchantment> slotData = new HashMap<>();
    private static HashMap<Integer,CustomEnchantment> slotDataCustom = new HashMap<>();

    
    public static void openGrindstoneMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Grindstone", NamedTextColor.GOLD));
        displayItemFrame(inv);
        optionsFill(inv);
        player.openInventory(inv);
    }

    public static void displayOptions(Inventory inv, ItemStack item) {
        if(item == null) return;
        clearOptions(inv);
        Map<Enchantment, Integer> vanillaEnchants = item.getEnchantments();
        Map<CustomEnchantment, Integer> customEnchants = UtilityMethods.getEnchantments(item);
        int slotptr = START_SLOT;
        for(Enchantment enchant : vanillaEnchants.keySet()) {
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = enchant.getKey().getKey();
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.AQUA));
                enchantOption.setItemMeta(metaOffer);
            }
            inv.setItem(slotptr, enchantOption);
            slotData.put(slotptr, enchant);
            if(slotptr == 5) slotptr+=4;
            else if(slotptr == 14) slotptr+=4;
            else if(slotptr == 23) slotptr+=4;
            else if(slotptr == 32) slotptr+=4;
            else if(slotptr == 41) slotptr+=4;
            else if(slotptr == 50) break;
            else {
                slotptr++;
            }
        }
        if(slotptr < 50) {
            for(CustomEnchantment enchant : customEnchants.keySet()) {
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = enchant.getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GOLD));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
                slotDataCustom.put(slotptr, enchant);
                if(slotptr == 5) slotptr+=4;
                else if(slotptr == 14) slotptr+=4;
                else if(slotptr == 23) slotptr+=4;
                else if(slotptr == 32) slotptr+=4;
                else if(slotptr == 41) slotptr+=4;
                else if(slotptr == 50) break;
                else {
                    slotptr++;
                }
            }
        }
        optionsFill(inv);
    }

    public static void applyOption(Player player, Inventory inv, int slot, ItemStack item) {
        if(item == null) return;
        Enchantment enchant = slotData.get(slot);
        CustomEnchantment customEnchant = slotDataCustom.get(slot);

        if(enchant != null) {
            item.removeEnchantment(enchant);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
        } 

        if(customEnchant != null) {
            UtilityMethods.removeEnchantment(item, customEnchant.getName());
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
        }
        displayOptions(inv, item);
    }

    public static void clearOptions(Inventory inv) {
        int slotptr = START_SLOT;
        while(slotptr < 51) {
            inv.setItem(slotptr, new ItemStack(Material.AIR));
            if(slotptr == 5) slotptr+=4;
            else if(slotptr == 14) slotptr+=4;
            else if(slotptr == 23) slotptr+=4;
            else if(slotptr == 32) slotptr+=4;
            else if(slotptr == 41) slotptr+=4;
            else if(slotptr == 50) break;
            else {
                slotptr++;
            }
        }
    }

    public static void displayItemFrame(Inventory inv) {
        List<Integer> frame = Arrays.asList(6,7,8,15,16,17,24,26,33,35,42,43,44,51,52,53);
        for (int slot : frame) {
            ItemStack frameTiles = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
            ItemMeta frameMeta = frameTiles.getItemMeta();
            if (frameMeta != null) { // Check for null to prevent potential NullPointerException
                frameMeta.displayName(Component.text("Unavailable", NamedTextColor.DARK_PURPLE));
                frameTiles.setItemMeta(frameMeta); // Set the modified meta back on the ItemStack
            }
            inv.setItem(slot, frameTiles);
        }
        
        ItemStack enchantTablePlaceholder = new ItemStack(Material.GRINDSTONE);
        ItemMeta enchantTablePlaceholderMeta = enchantTablePlaceholder.getItemMeta();
        if (enchantTablePlaceholderMeta != null) {
            enchantTablePlaceholderMeta.displayName(Component.text("Enchantment Slot", NamedTextColor.GREEN));
            enchantTablePlaceholder.setItemMeta(enchantTablePlaceholderMeta);
        }

        inv.setItem(PLACEHOLDER_SLOT, enchantTablePlaceholder); // Assuming PLACEHOLDER_SLOT is correctly defined
    }

    private static void optionsFill(Inventory inv) {
        int slotptr = START_SLOT;
        ItemStack Placeholder = new ItemStack(Material.GLASS_PANE);
        ItemMeta PlaceholderMeta = Placeholder.getItemMeta();
        if (PlaceholderMeta != null) {
            PlaceholderMeta.displayName(Component.text("Unavailable", NamedTextColor.DARK_PURPLE));
            Placeholder.setItemMeta(PlaceholderMeta);
        }
        while(slotptr < 51) {
            if(inv.getItem(slotptr) == null || inv.getItem(slotptr).getType() != Material.ENCHANTED_BOOK) {
                inv.setItem(slotptr, Placeholder);
            }
            if(slotptr == 5) slotptr+=4;
            else if(slotptr == 14) slotptr+=4;
            else if(slotptr == 23) slotptr+=4;
            else if(slotptr == 32) slotptr+=4;
            else if(slotptr == 41) slotptr+=4;
            else if(slotptr == 50) break;
            else {
                slotptr++;
            }
        }
    }
}

