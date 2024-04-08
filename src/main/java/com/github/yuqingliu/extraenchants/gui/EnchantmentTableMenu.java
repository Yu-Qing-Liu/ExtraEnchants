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
import java.util.Collections;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.database.*;
import com.github.yuqingliu.extraenchants.utils.*;

public class EnchantmentTableMenu {
    private static final int START_SLOT = 0;
    private static final int PREVIOUS_PAGE = 6;
    private static final int NEXT_PAGE = 51;
    private static final int PLACEHOLDER_SLOT = 34;
    private static final int BOOKSHELF_MULTIPLIER = Constants.getBookshelfMultiplier();
    private static final int MAX_BOOKSHELVES = 15;
    private static int PAGE_NUMBER = 1;
    private static int MAX_PAGES = 25;
    private static HashMap<Integer, HashMap<Integer, EnchantmentOffer>> pageDataVanillaEnchants = new HashMap<>();
    private static HashMap<Integer, HashMap<Integer, CustomEnchantmentOffer>> pageDataCustomEnchants = new HashMap<>();
    private static EnchantmentOffer selectedVanillaOffer = null;
    private static CustomEnchantmentOffer selectedCustomOffer = null;
    private static int experienceLevelStep;
    
    public static void openEnchantmentTableMenu(Player player, int bookshelves) {
        bookshelves = (int) Math.min(MAX_BOOKSHELVES, bookshelves);
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE));
        experienceLevelStep = (bookshelves * BOOKSHELF_MULTIPLIER) / 10;
        displayItemFrame(inv);
        optionsFill(inv);
        initializePages();
        player.openInventory(inv);
    }

    private static void initializePages() {
        for (int i = 1; i < 25; i++) {
            pageDataVanillaEnchants.put(i, new HashMap<>());
            pageDataCustomEnchants.put(i, new HashMap<>());
        }
    }

    public static void displayEnchantmentOptions(Inventory inv, ItemStack item) {
        if(item == null) return;
        clearOptions(inv);
        // Display all enchantments that are applicable for the item
        int slotptr = START_SLOT;
        List<EnchantmentOffer> applicableEnchants = getEnchants(item);
        List<CustomEnchantmentOffer> applicableCustomEnchants = getCustomEnchants(item);
        MAX_PAGES = applicableEnchants.size() / 36 + applicableCustomEnchants.size() / 36 + 1;
        for (EnchantmentOffer offer : applicableEnchants) {
            pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = offer.getEnchantment().getKey().getKey();
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.AQUA));
                enchantOption.setItemMeta(metaOffer);
            }
            inv.setItem(slotptr, enchantOption);
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
            for (CustomEnchantmentOffer offer : applicableCustomEnchants) {
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GOLD));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
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
        
        // Fill options
        optionsFill(inv);

        // Set Previous page ptr
        ItemStack prevPagePtr = new ItemStack(Material.ARROW);
        ItemMeta prevPagePtrMeta = prevPagePtr.getItemMeta();
        if (prevPagePtrMeta != null) {
            prevPagePtrMeta.displayName(Component.text("Previous Page", NamedTextColor.AQUA));
            prevPagePtr.setItemMeta(prevPagePtrMeta);
        }
        inv.setItem(PREVIOUS_PAGE, prevPagePtr);
        // Set Next page ptr
        ItemStack nextPagePtr = new ItemStack(Material.ARROW);
        ItemMeta nextPagePtrMeta = nextPagePtr.getItemMeta();
        if (nextPagePtrMeta != null) {
            nextPagePtrMeta.displayName(Component.text("Next Page", NamedTextColor.AQUA));
            nextPagePtr.setItemMeta(nextPagePtrMeta);
        }
        inv.setItem(NEXT_PAGE, nextPagePtr);

    }

    public static void displaySelectedEnchantmentOptions(Player player, ItemStack item, Inventory inv, int slot) {
        if(item == null) return;
        EnchantmentOffer selectedOffer = pageDataVanillaEnchants.get(PAGE_NUMBER).get(slot);
        CustomEnchantmentOffer selectedOfferCustom = pageDataCustomEnchants.get(PAGE_NUMBER).get(slot);
        selectedVanillaOffer = selectedOffer;
        selectedCustomOffer = selectedOfferCustom;
        clearOptions(inv);

        if(selectedOffer != null) {
            if(selectedOffer.getCost() > 0) {
                // Apply the enchantment and return
                UtilityMethods.applyVanillaEnchant(player, selectedOffer, item);
                displayEnchantmentOptions(inv, item);
                return;
            }
            int slotptr = 0;
            int maxEnchantmentLevel = selectedOffer.getEnchantmentLevel();
            MAX_PAGES = maxEnchantmentLevel / 36 + 1;
            for (int i = 1; i <= maxEnchantmentLevel; i++) {
                // Display offers by increasing level up to max level as well as their prices
                EnchantmentOffer offer = new EnchantmentOffer(selectedOffer.getEnchantment(), i, i * experienceLevelStep);
                pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchantment().getKey().getKey();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()) + " Required Level: " + offer.getCost(), NamedTextColor.GREEN));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
                if(slotptr== 5) slotptr+=4;
                else if(slotptr== 14) slotptr+=4;
                else if(slotptr== 23) slotptr+=4;
                else if(slotptr== 32) slotptr+=4;
                else if(slotptr== 41) slotptr+=4;
                else if(slotptr== 50) break;
                else {
                    slotptr++;
                }
            }
        } else if(selectedOfferCustom != null) {
            if(selectedOfferCustom.getCost() > 0) {
                // Apply the enchantment and return
                UtilityMethods.applyCustomEnchant(player, selectedOfferCustom, item);
                displayEnchantmentOptions(inv, item);
                return;
            }
            int slotptr = 0;
            int maxEnchantmentLevel = selectedOfferCustom.getEnchantmentLevel();
            for (int i = 1; i <= maxEnchantmentLevel; i++) {
                // Display offers by increasing level up to max level as well as their prices
                CustomEnchantmentOffer offer = new CustomEnchantmentOffer(selectedOfferCustom.getEnchant(), i, i * experienceLevelStep);
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()) + " Required Level: " + offer.getCost(), NamedTextColor.GREEN));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
                if(slotptr== 5) slotptr+=4;
                else if(slotptr== 14) slotptr+=4;
                else if(slotptr== 23) slotptr+=4;
                else if(slotptr== 32) slotptr+=4;
                else if(slotptr== 41) slotptr+=4;
                else if(slotptr== 50) break;
                else {
                    slotptr++;
                }
            }
        } 
        // Fill options
        optionsFill(inv);

        // Set Previous page ptr
        ItemStack prevPagePtr = new ItemStack(Material.ARROW);
        ItemMeta prevPagePtrMeta = prevPagePtr.getItemMeta();
        if (prevPagePtrMeta != null) {
            prevPagePtrMeta.displayName(Component.text("Previous Page", NamedTextColor.GREEN));
            prevPagePtr.setItemMeta(prevPagePtrMeta);
        }
        inv.setItem(PREVIOUS_PAGE, prevPagePtr);
        // Set Next page ptr
        ItemStack nextPagePtr = new ItemStack(Material.ARROW);
        ItemMeta nextPagePtrMeta = nextPagePtr.getItemMeta();
        if (nextPagePtrMeta != null) {
            nextPagePtrMeta.displayName(Component.text("Next Page", NamedTextColor.GREEN));
            nextPagePtr.setItemMeta(nextPagePtrMeta);
        }
        inv.setItem(NEXT_PAGE, nextPagePtr);
    }

    public static void displayNextSelectedEnchantmentOptions(Player player, ItemStack item, Inventory inv, int slot) {
        if(item == null) return;
        if(PAGE_NUMBER + 1 > MAX_PAGES) return;
        PAGE_NUMBER++;
        EnchantmentOffer selectedOffer = selectedVanillaOffer;
        CustomEnchantmentOffer selectedOfferCustom = selectedCustomOffer;
        voidOptions(inv);

        if(selectedOffer != null) {
            if(selectedOffer.getCost() > 0) {
                // Apply the enchantment and return
                UtilityMethods.applyVanillaEnchant(player, selectedOffer, item);
                displayEnchantmentOptions(inv, item);
                return;
            }
            int slotptr = 0;
            HashMap<Integer, EnchantmentOffer> previousSlotData = pageDataVanillaEnchants.get(PAGE_NUMBER - 1);
            List<Integer> levels = new ArrayList<>();
            for(EnchantmentOffer offer : previousSlotData.values()) {
                levels.add(offer.getEnchantmentLevel());
            }
            int startLevel = Collections.max(levels) + 1;
            int maxEnchantmentLevel = selectedOffer.getEnchantmentLevel();
            for (int i = startLevel; i <= maxEnchantmentLevel; i++) {
                // Display offers by increasing level up to max level as well as their prices
                EnchantmentOffer offer = new EnchantmentOffer(selectedOffer.getEnchantment(), i, i * experienceLevelStep);
                pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchantment().getKey().getKey();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()) + " Required Level: " + offer.getCost(), NamedTextColor.GREEN));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
                if(slotptr== 5) slotptr+=4;
                else if(slotptr== 14) slotptr+=4;
                else if(slotptr== 23) slotptr+=4;
                else if(slotptr== 32) slotptr+=4;
                else if(slotptr== 41) slotptr+=4;
                else if(slotptr== 50) break;
                else {
                    slotptr++;
                }
            }
        } else if(selectedOfferCustom != null) {
            if(selectedOfferCustom.getCost() > 0) {
                // Apply the enchantment and return
                UtilityMethods.applyCustomEnchant(player, selectedOfferCustom, item);
                displayEnchantmentOptions(inv, item);
                return;
            }
            int slotptr = 0;
            HashMap<Integer, CustomEnchantmentOffer> previousSlotData = pageDataCustomEnchants.get(PAGE_NUMBER - 1);
            List<Integer> levels = new ArrayList<>();
            for(CustomEnchantmentOffer offer : previousSlotData.values()) {
                levels.add(offer.getEnchantmentLevel());
            }
            int startLevel = Collections.max(levels) + 1;
            int maxEnchantmentLevel = selectedOfferCustom.getEnchantmentLevel();
            for (int i = startLevel; i <= maxEnchantmentLevel; i++) {
                // Display offers by increasing level up to max level as well as their prices
                CustomEnchantmentOffer offer = new CustomEnchantmentOffer(selectedOfferCustom.getEnchant(), i, i * experienceLevelStep);
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()) + " Required Level: " + offer.getCost(), NamedTextColor.GREEN));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
                if(slotptr== 5) slotptr+=4;
                else if(slotptr== 14) slotptr+=4;
                else if(slotptr== 23) slotptr+=4;
                else if(slotptr== 32) slotptr+=4;
                else if(slotptr== 41) slotptr+=4;
                else if(slotptr== 50) break;
                else {
                    slotptr++;
                }
            }
        } 
        // Fill options
        optionsFill(inv);

        // Set Previous page ptr
        ItemStack prevPagePtr = new ItemStack(Material.ARROW);
        ItemMeta prevPagePtrMeta = prevPagePtr.getItemMeta();
        if (prevPagePtrMeta != null) {
            prevPagePtrMeta.displayName(Component.text("Previous Page", NamedTextColor.GREEN));
            prevPagePtr.setItemMeta(prevPagePtrMeta);
        }
        inv.setItem(PREVIOUS_PAGE, prevPagePtr);
        // Set Next page ptr
        ItemStack nextPagePtr = new ItemStack(Material.ARROW);
        ItemMeta nextPagePtrMeta = nextPagePtr.getItemMeta();
        if (nextPagePtrMeta != null) {
            nextPagePtrMeta.displayName(Component.text("Next Page", NamedTextColor.GREEN));
            nextPagePtr.setItemMeta(nextPagePtrMeta);
        }
        inv.setItem(NEXT_PAGE, nextPagePtr);
    }


    public static void displayPreviousSelectedEnchantmentOptions(Player player, ItemStack item, Inventory inv, int slot) {
        if(item == null) return;
        if(PAGE_NUMBER == 1) {
            displayEnchantmentOptions(inv, item);
            return;
        }
        PAGE_NUMBER--;
        EnchantmentOffer selectedOffer = selectedVanillaOffer;
        CustomEnchantmentOffer selectedOfferCustom = selectedCustomOffer;
        voidOptions(inv);

        if(selectedOffer != null) {
            if(selectedOffer.getCost() > 0) {
                // Apply the enchantment and return
                UtilityMethods.applyVanillaEnchant(player, selectedOffer, item);
                displayEnchantmentOptions(inv, item);
                return;
            }
            int slotptr = 0;
            HashMap<Integer, EnchantmentOffer> previousSlotData = pageDataVanillaEnchants.get(PAGE_NUMBER + 1);
            List<Integer> levels = new ArrayList<>();
            for(EnchantmentOffer offer : previousSlotData.values()) {
                levels.add(offer.getEnchantmentLevel());
            }
            int startLevel = Collections.min(levels) - 1;
            int maxEnchantmentLevel = startLevel;
            for (int i = startLevel - 36; i <= maxEnchantmentLevel; i++) {
                // Display offers by increasing level up to max level as well as their prices
                EnchantmentOffer offer = new EnchantmentOffer(selectedOffer.getEnchantment(), i, i * experienceLevelStep);
                pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchantment().getKey().getKey();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()) + " Required Level: " + offer.getCost(), NamedTextColor.GREEN));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
                if(slotptr== 5) slotptr+=4;
                else if(slotptr== 14) slotptr+=4;
                else if(slotptr== 23) slotptr+=4;
                else if(slotptr== 32) slotptr+=4;
                else if(slotptr== 41) slotptr+=4;
                else if(slotptr== 50) break;
                else {
                    slotptr++;
                }
            }
        } else if(selectedOfferCustom != null) {
            if(selectedOfferCustom.getCost() > 0) {
                // Apply the enchantment and return
                UtilityMethods.applyCustomEnchant(player, selectedOfferCustom, item);
                displayEnchantmentOptions(inv, item);
                return;
            }
            int slotptr = 0;
            HashMap<Integer, CustomEnchantmentOffer> previousSlotData = pageDataCustomEnchants.get(PAGE_NUMBER + 1);
            List<Integer> levels = new ArrayList<>();
            for(CustomEnchantmentOffer offer : previousSlotData.values()) {
                levels.add(offer.getEnchantmentLevel());
            }
            int startLevel = Collections.min(levels) - 1;
            int maxEnchantmentLevel = startLevel;
            for (int i = startLevel - 36; i <= maxEnchantmentLevel; i++) {
                // Display offers by increasing level up to max level as well as their prices
                CustomEnchantmentOffer offer = new CustomEnchantmentOffer(selectedOfferCustom.getEnchant(), i, i * experienceLevelStep);
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()) + " Required Level: " + offer.getCost(), NamedTextColor.GREEN));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
                if(slotptr== 5) slotptr+=4;
                else if(slotptr== 14) slotptr+=4;
                else if(slotptr== 23) slotptr+=4;
                else if(slotptr== 32) slotptr+=4;
                else if(slotptr== 41) slotptr+=4;
                else if(slotptr== 50) break;
                else {
                    slotptr++;
                }
            }
        } 
        // Fill options
        optionsFill(inv);

        // Set Previous page ptr
        ItemStack prevPagePtr = new ItemStack(Material.ARROW);
        ItemMeta prevPagePtrMeta = prevPagePtr.getItemMeta();
        if (prevPagePtrMeta != null) {
            prevPagePtrMeta.displayName(Component.text("Previous Page", NamedTextColor.GREEN));
            prevPagePtr.setItemMeta(prevPagePtrMeta);
        }
        inv.setItem(PREVIOUS_PAGE, prevPagePtr);
        // Set Next page ptr
        ItemStack nextPagePtr = new ItemStack(Material.ARROW);
        ItemMeta nextPagePtrMeta = nextPagePtr.getItemMeta();
        if (nextPagePtrMeta != null) {
            nextPagePtrMeta.displayName(Component.text("Next Page", NamedTextColor.GREEN));
            nextPagePtr.setItemMeta(nextPagePtrMeta);
        }
        inv.setItem(NEXT_PAGE, nextPagePtr);
    }

    public static void displayNextEnchantmentOptionsPage(Inventory inv, ItemStack item) {
        if(item == null) return;
        if(PAGE_NUMBER + 1 > MAX_PAGES) return;
        PAGE_NUMBER++;
        if(pageDataVanillaEnchants.get(PAGE_NUMBER).isEmpty() && pageDataCustomEnchants.get(PAGE_NUMBER).isEmpty()) {
            displayNextEmptyEnchantmentOptionsPage(inv, item);
        }
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        for (int i = 0; i < 51; i++) {
            if(pageDataVanillaEnchants.get(PAGE_NUMBER) == null || pageDataVanillaEnchants.get(PAGE_NUMBER).isEmpty()) break;
            EnchantmentOffer offer = pageDataVanillaEnchants.get(PAGE_NUMBER).get(slotptr);
            if(offer == null) continue;
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = offer.getEnchantment().getKey().getKey();
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.AQUA));
                enchantOption.setItemMeta(metaOffer);
            }
            inv.setItem(slotptr, enchantOption);
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
            for (int i = 0; i < 51; i++) {
                if(pageDataCustomEnchants.get(PAGE_NUMBER) == null || pageDataCustomEnchants.get(PAGE_NUMBER).isEmpty()) break;
                CustomEnchantmentOffer offer = pageDataCustomEnchants.get(PAGE_NUMBER).get(slotptr);
                if(offer == null) continue;
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GOLD));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
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

        // Fill options
        optionsFill(inv);
    }

    public static void displayNextEmptyEnchantmentOptionsPage(Inventory inv, ItemStack item) {
        if(item == null) return;
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        List<EnchantmentOffer> applicableEnchants = getEnchants(item);
        List<CustomEnchantmentOffer> applicableCustomEnchants = getCustomEnchants(item);
        for (EnchantmentOffer offer : applicableEnchants) {
            boolean exists = false;
            for (int i = 1; i < PAGE_NUMBER; i++) {
                HashMap<Integer, EnchantmentOffer> data = pageDataVanillaEnchants.get(i);
                if(data == null) continue;
                for (EnchantmentOffer precedentOffer : data.values()) {
                    if(offer.getEnchantment().getKey().equals(precedentOffer.getEnchantment().getKey())) {
                        exists = true;
                        break;
                    }
                }
            }
            if(exists) continue;
            pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = offer.getEnchantment().getKey().getKey();
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.AQUA));
                enchantOption.setItemMeta(metaOffer);
            }
            inv.setItem(slotptr, enchantOption);
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
            for (CustomEnchantmentOffer offer : applicableCustomEnchants) {
                boolean exists = false;
                for (int i = 0; i < PAGE_NUMBER; i++) {
                    HashMap<Integer, CustomEnchantmentOffer> data = pageDataCustomEnchants.get(i);
                    if(data == null) continue;
                    for (CustomEnchantmentOffer precedentOffer : data.values()) {
                        if(offer.getEnchant().getName().equals(precedentOffer.getEnchant().getName())) {
                            exists = true;
                            break;
                        }
                    }
                }
                if(exists) continue;
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GOLD));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
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

        // Fill options
        optionsFill(inv);
    }

    public static void displayPreviousEnchantmentOptionsPage(Inventory inv, ItemStack item) {
        if(item == null) return;
        if(PAGE_NUMBER == 1) return;
        PAGE_NUMBER--;
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        for (int i = 0; i < 51; i++) {
            if(pageDataVanillaEnchants.get(PAGE_NUMBER) == null || pageDataVanillaEnchants.get(PAGE_NUMBER).isEmpty()) continue;
            EnchantmentOffer offer = pageDataVanillaEnchants.get(PAGE_NUMBER).get(slotptr);
            if(offer == null) continue;
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = offer.getEnchantment().getKey().getKey();
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.AQUA));
                enchantOption.setItemMeta(metaOffer);
            }
            inv.setItem(slotptr, enchantOption);
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
            for (int i = 0; i < 51; i++) {
                if(pageDataVanillaEnchants.get(PAGE_NUMBER) == null || pageDataVanillaEnchants.get(PAGE_NUMBER).isEmpty()) break;
                CustomEnchantmentOffer offer = pageDataCustomEnchants.get(PAGE_NUMBER).get(slotptr);
                if(offer == null) continue;
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GOLD));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
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

        // Fill options
        optionsFill(inv);
    }

    public static void displayItemFrame(Inventory inv) {
        List<Integer> frame = Arrays.asList(6,7,8,15,16,17,24,26,33,35,42,43,44,51,52,53);
        for (int slot : frame) {
            ItemStack frameTiles = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
            ItemMeta frameMeta = frameTiles.getItemMeta();
            if (frameMeta != null) { // Check for null to prevent potential NullPointerException
                frameMeta.displayName(Component.text("Unavailable", NamedTextColor.DARK_PURPLE));
                frameTiles.setItemMeta(frameMeta); // Set the modified meta back on the ItemStack
            }
            inv.setItem(slot, frameTiles);
        }
        
        ItemStack nextPagePtr = new ItemStack(Material.ARROW);
        ItemMeta nextPagePtrMeta = nextPagePtr.getItemMeta();
        if (nextPagePtrMeta != null) {
            nextPagePtrMeta.displayName(Component.text("Next Page", NamedTextColor.AQUA));
            nextPagePtr.setItemMeta(nextPagePtrMeta);
        }

        ItemStack prevPagePtr = new ItemStack(Material.ARROW);
        ItemMeta prevPagePtrMeta = prevPagePtr.getItemMeta();
        if (prevPagePtrMeta != null) {
            prevPagePtrMeta.displayName(Component.text("Previous Page", NamedTextColor.AQUA));
            prevPagePtr.setItemMeta(prevPagePtrMeta);
        }

        ItemStack enchantTablePlaceholder = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta enchantTablePlaceholderMeta = enchantTablePlaceholder.getItemMeta();
        if (enchantTablePlaceholderMeta != null) {
            enchantTablePlaceholderMeta.displayName(Component.text("Enchantment Slot", NamedTextColor.GREEN));
            enchantTablePlaceholder.setItemMeta(enchantTablePlaceholderMeta);
        }

        inv.setItem(PLACEHOLDER_SLOT, enchantTablePlaceholder);
        inv.setItem(PREVIOUS_PAGE, prevPagePtr);
        inv.setItem(NEXT_PAGE, nextPagePtr);
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

    public static void clearOptions(Inventory inv) {
        int slotptr = START_SLOT;
        if(pageDataCustomEnchants.get(PAGE_NUMBER) != null) pageDataCustomEnchants.get(PAGE_NUMBER).clear();
        if(pageDataVanillaEnchants.get(PAGE_NUMBER) != null) pageDataVanillaEnchants.get(PAGE_NUMBER).clear();
        PAGE_NUMBER = 1;
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

    public static void voidOptions(Inventory inv) {
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

    private static boolean isEnchantable(ItemStack item, Enchantment enchant) {
        final Set<Enchantment> ALLOWED_CROSSBOW_ENCHANTS = new HashSet<>(Arrays.asList(
            Enchantment.ARROW_DAMAGE
        ));

        if(item.getType() == Material.CROSSBOW && ALLOWED_CROSSBOW_ENCHANTS.contains(enchant)) {
            return true;
        } else if(item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) {
            return true;
        } else {
            return enchant.canEnchantItem(item);
        }
    }

    private static boolean isCustomEnchantable(ItemStack item, CustomEnchantment enchant) {
        return enchant.canEnchant(item);
    }

    public static List<EnchantmentOffer> getEnchants(ItemStack item) {
        HashMap<NamespacedKey, Integer> EnchantmentRegistry = Constants.getEnchantments();
        List<EnchantmentOffer> applicableEnchants = new ArrayList<>();

        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            boolean enchantable = isEnchantable(item, enchantment);
            if (enchantable) {
                // Get the level from applicableEnchants
                NamespacedKey key = enchantment.getKey();
                int level = EnchantmentRegistry.get(key);
                if(level > 0) {
                    EnchantmentOffer offer = new EnchantmentOffer(enchantment, level, 0);
                    applicableEnchants.add(offer);
                }
            }
        }
        return applicableEnchants;
    }

    public static List<CustomEnchantmentOffer> getCustomEnchants(ItemStack item) {
        HashMap<String, Integer> customEnchantments= Constants.getCustomEnchantments();
        List<CustomEnchantment> customEnchantmentRegistry = Database.getCustomEnchantmentRegistry();
        List<CustomEnchantmentOffer> applicableEnchants = new ArrayList<>();

        for (CustomEnchantment enchantment : customEnchantmentRegistry) {
            boolean enchantable = isCustomEnchantable(item, enchantment);
            if (enchantable) {
                // Get the level from applicableEnchants
                String key = enchantment.getName();
                int level = customEnchantments.get(key);
                if(level > 0) {
                    CustomEnchantmentOffer offer = new CustomEnchantmentOffer(enchantment, level, 0);
                    applicableEnchants.add(offer);
                }
            }
        }
        return applicableEnchants;
    }
}
