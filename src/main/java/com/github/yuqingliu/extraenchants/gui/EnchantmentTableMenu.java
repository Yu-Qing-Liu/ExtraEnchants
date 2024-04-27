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
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import com.github.yuqingliu.extraenchants.enchants.*;
import com.github.yuqingliu.extraenchants.enchants.utils.*;

public class EnchantmentTableMenu {
    private static final int START_SLOT = 0;
    private static final int PREVIOUS_PAGE = 6;
    private static final int NEXT_PAGE = 51;
    private static final int ITEM_SLOT = 25;
    private static final int PLACEHOLDER_SLOT = 34;
    private static int PAGE_NUMBER = 1;
    private static int MAX_PAGES = 25;
    private static HashMap<Integer, HashMap<Integer, EnchantmentOffer>> pageDataVanillaEnchants = new HashMap<>();
    private static HashMap<Integer, HashMap<Integer, CustomEnchantmentOffer>> pageDataCustomEnchants = new HashMap<>();
    private static HashMap<NamespacedKey, List<Object>> EnchantmentRegistry = Constants.getEnchantments();
    private static HashMap<String, List<Object>> CustomEnchantmentRegistry = Constants.getCustomEnchantments();
    private static EnchantmentOffer selectedVanillaOffer = null;
    private static CustomEnchantmentOffer selectedCustomOffer = null;
    
    public static void openEnchantmentTableMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE));
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
        MAX_PAGES = (int) Math.ceil((double) applicableEnchants.size() / 36.0 + (double) applicableCustomEnchants.size() / 36.0);
        for (EnchantmentOffer offer : applicableEnchants) {
            pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = offer.getEnchantment().getKey().getKey();
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GRAY));
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
        if(slotptr < 50 || inv.getItem(50) == null || inv.getItem(50).getType() == Material.AIR) {
            for (CustomEnchantmentOffer offer : applicableCustomEnchants) {
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    Component description = Component.text(offer.getEnchant().getDescription(), NamedTextColor.GRAY);
                    existingLore.add(description);
                    metaOffer.lore(existingLore);
                    String enchantmentName = offer.getEnchant().getName();
                    TextColor color = offer.getEnchant().getColor();
                    TextComponent textComponent = Component.text(UtilityMethods.formatString(enchantmentName)).color(color);
                    metaOffer.displayName(textComponent);
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
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GRAY));
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
        if(slotptr < 50 || inv.getItem(50) == null || inv.getItem(50).getType() == Material.AIR) {
            for (int i = 0; i < 51; i++) {
                if(pageDataCustomEnchants.get(PAGE_NUMBER) == null || pageDataCustomEnchants.get(PAGE_NUMBER).isEmpty()) break;
                CustomEnchantmentOffer offer = pageDataCustomEnchants.get(PAGE_NUMBER).get(slotptr);
                if(offer == null) continue;
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    Component description = Component.text(offer.getEnchant().getDescription(), NamedTextColor.GRAY);
                    existingLore.add(description);
                    metaOffer.lore(existingLore);
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), offer.getEnchant().getColor()));
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
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GRAY));
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
        if(slotptr < 50 || inv.getItem(50) == null || inv.getItem(50).getType() == Material.AIR) {
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
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    Component description = Component.text(offer.getEnchant().getDescription(), NamedTextColor.GRAY);
                    existingLore.add(description);
                    metaOffer.lore(existingLore);
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), offer.getEnchant().getColor()));
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
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GRAY));
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
        if(slotptr < 50 || inv.getItem(50) == null || inv.getItem(50).getType() == Material.AIR) {
            for (int i = 0; i < 51; i++) {
                if(pageDataVanillaEnchants.get(PAGE_NUMBER) == null || pageDataVanillaEnchants.get(PAGE_NUMBER).isEmpty()) break;
                CustomEnchantmentOffer offer = pageDataCustomEnchants.get(PAGE_NUMBER).get(slotptr);
                if(offer == null) continue;
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    Component description = Component.text(offer.getEnchant().getDescription(), NamedTextColor.GRAY);
                    existingLore.add(description);
                    metaOffer.lore(existingLore);
                    String enchantmentName = offer.getEnchant().getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), offer.getEnchant().getColor()));
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

    public static void displaySelectedEnchantmentOptions(JavaPlugin plugin, Player player, ItemStack item, Inventory inv, int slot) {
        if(item == null) return;
        EnchantmentOffer selectedOffer = pageDataVanillaEnchants.get(PAGE_NUMBER).get(slot);
        CustomEnchantmentOffer selectedOfferCustom = pageDataCustomEnchants.get(PAGE_NUMBER).get(slot);
        selectedVanillaOffer = selectedOffer;
        selectedCustomOffer = selectedOfferCustom;
        clearOptions(inv);

        if(selectedOffer != null) {
            if(selectedOffer.getCost() > 0) {
                // Apply the enchantment and return
                if(player.getLevel() >= selectedOffer.getCost()) {
                    UtilityMethods.applyVanillaEnchant(player, selectedOffer, item);
                } else {
                    player.sendMessage("Not enough experience");
                }
                displayEnchantmentOptions(inv, item);
                return;
            }
            int slotptr = 0;
            int maxEnchantmentLevel = selectedOffer.getEnchantmentLevel();
            MAX_PAGES = (int) Math.ceil((double) maxEnchantmentLevel / 36.0);
            for (int i = 1; i <= maxEnchantmentLevel; i++) {
                // Display offers by increasing level up to max level as well as their prices
                String expression = (String) EnchantmentRegistry.get(selectedOffer.getEnchantment().getKey()).get(1);
                int result = UtilityMethods.evaluateExpression(expression, i);
                EnchantmentOffer offer = new EnchantmentOffer(selectedOffer.getEnchantment(), i, result);
                pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    String enchantmentName = offer.getEnchantment().getKey().getKey();
                    Component name = Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()), NamedTextColor.GRAY);
                    existingLore.add(name);
                    metaOffer.lore(existingLore);
                    metaOffer.displayName(Component.text("Required Level: " + offer.getCost(), NamedTextColor.GREEN));
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
                TextColor color = selectedOfferCustom.getEnchant().getColor();
                String cmd = selectedOfferCustom.getEnchant().getAddCmd();
                if(cmd != null && !cmd.isEmpty() && player.getLevel() >= selectedOfferCustom.getCost()) {
                    ItemStack enchantedItem = UtilityMethods.applyExtraEnchant(plugin, inv, ITEM_SLOT, player, selectedOfferCustom, item, color);
                    if(enchantedItem != null) {
                        displayEnchantmentOptions(inv, enchantedItem);
                        return;
                    } 
                } else if(player.getLevel() >= selectedOfferCustom.getCost()) {
                    ItemStack enchantedItem = UtilityMethods.applyCustomEnchant(player, selectedOfferCustom, item, color);
                    if(enchantedItem != null) {
                        inv.setItem(ITEM_SLOT, enchantedItem);
                        displayEnchantmentOptions(inv, enchantedItem);
                        return;
                    } 
                }
                player.sendMessage("Not enough experience");
                displayEnchantmentOptions(inv, item);
                return;
            }
            int slotptr = 0;
            int maxEnchantmentLevel = selectedOfferCustom.getEnchantmentLevel();
            for (int i = 1; i <= maxEnchantmentLevel; i++) {
                // Display offers by increasing level up to max level as well as their prices
                String expression = (String) CustomEnchantmentRegistry.get(selectedOfferCustom.getEnchant().getName()).get(1);
                int result = UtilityMethods.evaluateExpression(expression, i);
                CustomEnchantmentOffer offer = new CustomEnchantmentOffer(selectedOfferCustom.getEnchant(), i, result);
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    String enchantmentName = offer.getEnchant().getName();
                    Component name = Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()), offer.getEnchant().getColor());
                    Component description = Component.text(offer.getEnchant().getLeveledDescription(offer.getEnchantmentLevel()), NamedTextColor.GRAY);
                    existingLore.add(name);
                    existingLore.add(description);
                    metaOffer.lore(existingLore);
                    metaOffer.displayName(Component.text("Required Level: " + offer.getCost(), NamedTextColor.GREEN));
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

    public static void displayNextSelectedEnchantmentOptions(JavaPlugin plugin, Player player, ItemStack item, Inventory inv, int slot) {
        if(item == null) return;
        if(PAGE_NUMBER + 1 > MAX_PAGES) return;
        PAGE_NUMBER++;
        EnchantmentOffer selectedOffer = selectedVanillaOffer;
        CustomEnchantmentOffer selectedOfferCustom = selectedCustomOffer;
        voidOptions(inv);

        if(selectedOffer != null) {
            if(selectedOffer.getCost() > 0) {
                // Apply the enchantment and return
                if(player.getLevel() >= selectedOffer.getCost()) {
                    UtilityMethods.applyVanillaEnchant(player, selectedOffer, item);
                } else {
                    player.sendMessage("Not enough experience");
                }
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
                String expression = (String) EnchantmentRegistry.get(selectedOffer.getEnchantment().getKey()).get(1);
                int result = UtilityMethods.evaluateExpression(expression, i);
                EnchantmentOffer offer = new EnchantmentOffer(selectedOffer.getEnchantment(), i, result);
                pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    String enchantmentName = offer.getEnchantment().getKey().getKey();
                    Component name = Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()), NamedTextColor.GRAY);
                    existingLore.add(name);
                    metaOffer.lore(existingLore);
                    metaOffer.displayName(Component.text("Required Level: " + offer.getCost(), NamedTextColor.GREEN));
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
                TextColor color = selectedOfferCustom.getEnchant().getColor();
                String cmd = selectedOfferCustom.getEnchant().getAddCmd();
                if(cmd != null && !cmd.isEmpty() && player.getLevel() >= selectedOfferCustom.getCost()) {
                    ItemStack enchantedItem = UtilityMethods.applyExtraEnchant(plugin, inv, ITEM_SLOT, player, selectedOfferCustom, item, color);
                    if(enchantedItem != null) {
                        displayEnchantmentOptions(inv, enchantedItem);
                        return;
                    } 
                } else if(player.getLevel() >= selectedOfferCustom.getCost()) {
                    ItemStack enchantedItem = UtilityMethods.applyCustomEnchant(player, selectedOfferCustom, item, color);
                    if(enchantedItem != null) {
                        inv.setItem(ITEM_SLOT, enchantedItem);
                        displayEnchantmentOptions(inv, enchantedItem);
                        return;
                    } 
                }
                player.sendMessage("Not enough experience");
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
                String expression = (String) CustomEnchantmentRegistry.get(selectedOfferCustom.getEnchant().getName()).get(1);
                int result = UtilityMethods.evaluateExpression(expression, i);
                CustomEnchantmentOffer offer = new CustomEnchantmentOffer(selectedOfferCustom.getEnchant(), i, result);
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    String enchantmentName = offer.getEnchant().getName();
                    Component name = Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()), offer.getEnchant().getColor());
                    Component description = Component.text(offer.getEnchant().getLeveledDescription(offer.getEnchantmentLevel()), NamedTextColor.GRAY);
                    existingLore.add(name);
                    existingLore.add(description);
                    metaOffer.lore(existingLore);
                    metaOffer.displayName(Component.text("Required Level: " + offer.getCost(), NamedTextColor.GREEN));
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


    public static void displayPreviousSelectedEnchantmentOptions(JavaPlugin plugin, Player player, ItemStack item, Inventory inv, int slot) {
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
                if(player.getLevel() >= selectedOffer.getCost()) {
                    UtilityMethods.applyVanillaEnchant(player, selectedOffer, item);
                } else {
                    player.sendMessage("Not enough experience");
                }
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
                String expression = (String) EnchantmentRegistry.get(selectedOffer.getEnchantment().getKey()).get(1);
                int result = UtilityMethods.evaluateExpression(expression, i);
                EnchantmentOffer offer = new EnchantmentOffer(selectedOffer.getEnchantment(), i, result);
                pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    String enchantmentName = offer.getEnchantment().getKey().getKey();
                    Component name = Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()), NamedTextColor.GRAY);
                    existingLore.add(name);
                    metaOffer.lore(existingLore);
                    metaOffer.displayName(Component.text("Required Level: " + offer.getCost(), NamedTextColor.GREEN));
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
                TextColor color = selectedOfferCustom.getEnchant().getColor();
                String cmd = selectedOfferCustom.getEnchant().getAddCmd();
                if(cmd != null && !cmd.isEmpty() && player.getLevel() >= selectedOfferCustom.getCost()) {
                    ItemStack enchantedItem = UtilityMethods.applyExtraEnchant(plugin, inv, ITEM_SLOT, player, selectedOfferCustom, item, color);
                    if(enchantedItem != null) {
                        displayEnchantmentOptions(inv, enchantedItem);
                        return;
                    } 
                } else if(player.getLevel() >= selectedOfferCustom.getCost()) {
                    ItemStack enchantedItem = UtilityMethods.applyCustomEnchant(player, selectedOfferCustom, item, color);
                    if(enchantedItem != null) {
                        inv.setItem(ITEM_SLOT, enchantedItem);
                        displayEnchantmentOptions(inv, enchantedItem);
                        return;
                    } 
                }
                player.sendMessage("Not enough experience");
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
                String expression = (String) CustomEnchantmentRegistry.get(selectedOfferCustom.getEnchant().getName()).get(1);
                int result = UtilityMethods.evaluateExpression(expression, i);
                CustomEnchantmentOffer offer = new CustomEnchantmentOffer(selectedOfferCustom.getEnchant(), i, result);
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
                    String enchantmentName = offer.getEnchant().getName();
                    Component name = Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()), offer.getEnchant().getColor());
                    Component description = Component.text(offer.getEnchant().getLeveledDescription(offer.getEnchantmentLevel()), NamedTextColor.GRAY);
                    existingLore.add(name);
                    existingLore.add(description);
                    metaOffer.lore(existingLore);
                    metaOffer.displayName(Component.text("Required Level: " + offer.getCost(), NamedTextColor.GREEN));
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

    public static void optionsFill(Inventory inv) {
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
        pageDataCustomEnchants.clear();
        pageDataVanillaEnchants.clear();
        initializePages();
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
            Enchantment.ARROW_DAMAGE,
            Enchantment.ARROW_FIRE
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
        List<EnchantmentOffer> applicableEnchants = new ArrayList<>();
        Map<Enchantment, Integer> itemEnchants = item.getEnchantments();

        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            boolean enchantable = isEnchantable(item, enchantment);
            if (enchantable) {
                // Get the level from applicableEnchants
                NamespacedKey key = enchantment.getKey();
                int level = (int) EnchantmentRegistry.get(key).get(0);
                int itemEnchantLevel = 0;
                if(itemEnchants.get(enchantment) != null) itemEnchantLevel = itemEnchants.get(enchantment);
                if(itemEnchantLevel == level) continue;
                if(level > 0) {
                    EnchantmentOffer offer = new EnchantmentOffer(enchantment, level, 0);
                    applicableEnchants.add(offer);
                }
            }
        }
        return applicableEnchants;
    }

    public static List<CustomEnchantmentOffer> getCustomEnchants(ItemStack item) {
        List<CustomEnchantment> customEnchantmentRegistry = Database.getCustomEnchantmentRegistry();
        List<CustomEnchantmentOffer> applicableEnchants = new ArrayList<>();
        Map<CustomEnchantment, Integer> itemEnchants = UtilityMethods.getEnchantments(item);

        for (CustomEnchantment enchantment : customEnchantmentRegistry) {
            boolean enchantable = isCustomEnchantable(item, enchantment);
            if (enchantable) {
                // Get the level from applicableEnchants
                String key = enchantment.getName();
                int level = (int) CustomEnchantmentRegistry.get(key).get(0);
                int itemEnchantLevel = 0;
                if(itemEnchants.get(enchantment) != null) itemEnchantLevel = itemEnchants.get(enchantment);
                if(itemEnchantLevel == level) continue;
                if(level > 0) {
                    CustomEnchantmentOffer offer = new CustomEnchantmentOffer(enchantment, level, 0);
                    applicableEnchants.add(offer);
                }
            }
        }
        return applicableEnchants;
    }
}
