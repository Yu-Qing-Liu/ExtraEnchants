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

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.database.Constants;
import com.github.yuqingliu.extraenchants.utils.UtilityMethods;

public class EnchantmentTableMenu {
    private static final int START_SLOT = 0;
    private static final int CUSTOM_ENCHANTS_PAGE = 6;
    private static final int NEXT_PAGE = 51;
    private static final int PLACEHOLDER_SLOT = 34;
    private static final int MAX_ENCHANTMENT_LEVEL = Constants.getMaxEnchantLevel();
    private static final int BOOKSHELF_MULTIPLIER = Constants.getBookshelfMultiplier();
    private static final int MAX_BOOKSHELVES = Constants.getMaxBookshelves();
    private static HashMap<Integer,EnchantmentOffer> slotData = new HashMap<>();
    private static double maxEnchantmentLevel;
    private static int experienceLevelStep;

    
    public static void openEnchantmentTableMenu(Player player, int bookshelves) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE));
        maxEnchantmentLevel = Math.ceil(Math.min((double) bookshelves / MAX_BOOKSHELVES, 1) * MAX_ENCHANTMENT_LEVEL); 
        experienceLevelStep = (bookshelves * BOOKSHELF_MULTIPLIER) / MAX_ENCHANTMENT_LEVEL;
        displayItemFrame(inv);
        player.openInventory(inv);
    }

    public static void displayEnchantmentOptions(Inventory inv, ItemStack item) {
        if(item == null) return;
        clearOptions(inv);
        // Display all enchantments that are applicable for the item
        int slotptr = START_SLOT;
        List<EnchantmentOffer> applicableEnchants = getEnchants(item);
        for (EnchantmentOffer offer : applicableEnchants) {
            slotData.put(slotptr, offer);
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
        // Set ptr to previous page
        ItemStack nextPagePtr = new ItemStack(Material.ARROW);
        ItemMeta nextPagePtrMeta = nextPagePtr.getItemMeta();
        if (nextPagePtrMeta != null) {
            nextPagePtrMeta.displayName(Component.text("Next Page", NamedTextColor.RED));
            nextPagePtr.setItemMeta(nextPagePtrMeta);
        }
        inv.setItem(NEXT_PAGE, nextPagePtr);
    }

    public static void clearOptions(Inventory inv) {
        int slotptr = START_SLOT;
        slotData.clear();
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

    public static void displaySelectedEnchantmentOptions(Player player, ItemStack item, Inventory inv, int slot) {
        if(item == null) return;
        EnchantmentOffer selectedOffer = slotData.get(slot);
        if(selectedOffer == null) return;
        clearOptions(inv);
        if(selectedOffer.getCost() > 0) {
            // Apply the enchantment and return
            int requiredLevel = selectedOffer.getCost();
            int playerLevel = player.getLevel();
            if(playerLevel >= requiredLevel) {
                Map<Enchantment,Integer> itemEnchants = item.getEnchantments();
                int prevEnchantLevel = 0;
                if(itemEnchants != null) {
                    if(itemEnchants.containsKey(selectedOffer.getEnchantment())) {
                        prevEnchantLevel = itemEnchants.get(selectedOffer.getEnchantment());
                    }
                }
                if(prevEnchantLevel > selectedOffer.getEnchantmentLevel()) {
                    inv.close();
                    return;
                } else if(prevEnchantLevel == selectedOffer.getEnchantmentLevel() && prevEnchantLevel < MAX_ENCHANTMENT_LEVEL) {
                    item.addUnsafeEnchantment(selectedOffer.getEnchantment(), selectedOffer.getEnchantmentLevel() + 1);
                    player.setLevel(playerLevel - 1);
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                } else {
                    item.addUnsafeEnchantment(selectedOffer.getEnchantment(), selectedOffer.getEnchantmentLevel());
                    player.setLevel(playerLevel - 1);
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                }
            }
            inv.close();
            return;
        }
        int slotptr = 0;
        for (int i = 1; i <= maxEnchantmentLevel; i++) {
            // Display offers by increasing level up to max level as well as their prices
            EnchantmentOffer offer = new EnchantmentOffer(selectedOffer.getEnchantment(), i, i * experienceLevelStep);
            slotData.put(slotptr, offer);
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = offer.getEnchantment().getKey().getKey();
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(offer.getEnchantmentLevel()) + " Required Level: " + offer.getCost(), NamedTextColor.GRAY));
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
        // Set ptr to previous page
        ItemStack nextPagePtr = new ItemStack(Material.ARROW);
        ItemMeta nextPagePtrMeta = nextPagePtr.getItemMeta();
        if (nextPagePtrMeta != null) {
            nextPagePtrMeta.displayName(Component.text("Previous Page", NamedTextColor.RED));
            nextPagePtr.setItemMeta(nextPagePtrMeta);
        }
        inv.setItem(NEXT_PAGE, nextPagePtr);
    }

    public static void displayNextEnchantmentOptionsPage(Inventory inv, ItemStack item) {
        if(item == null) return;
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        List<EnchantmentOffer> applicableEnchants = getEnchants(item);
        for (EnchantmentOffer offer : applicableEnchants) {
            boolean exists = false;
            for (EnchantmentOffer precedentOffer : slotData.values()) {
                if(offer.getEnchantment().getKey().equals(precedentOffer.getEnchantment().getKey())) {
                    exists = true;
                    break;
                }
            }
            if(exists) continue;
            slotData.put(slotptr, offer);
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
        // Set ptr to previous page
        ItemStack nextPagePtr = new ItemStack(Material.ARROW);
        ItemMeta nextPagePtrMeta = nextPagePtr.getItemMeta();
        if (nextPagePtrMeta != null) {
            nextPagePtrMeta.displayName(Component.text("Previous Page", NamedTextColor.RED));
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
            nextPagePtrMeta.displayName(Component.text("Next Page", NamedTextColor.RED));
            nextPagePtr.setItemMeta(nextPagePtrMeta);
        }

        ItemStack customEnchantPagePtr = new ItemStack(Material.ARROW);
        ItemMeta customEnchantPagePtrMeta = customEnchantPagePtr.getItemMeta();
        if (customEnchantPagePtrMeta != null) {
            customEnchantPagePtrMeta.displayName(Component.text("Custom Enchants", NamedTextColor.RED));
            customEnchantPagePtr.setItemMeta(customEnchantPagePtrMeta);
        }
        
        ItemStack enchantTablePlaceholder = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta enchantTablePlaceholderMeta = enchantTablePlaceholder.getItemMeta();
        if (enchantTablePlaceholderMeta != null) {
            enchantTablePlaceholderMeta.displayName(Component.text("Enchantment Slot", NamedTextColor.GREEN));
            enchantTablePlaceholder.setItemMeta(enchantTablePlaceholderMeta);
        }

        inv.setItem(PLACEHOLDER_SLOT, enchantTablePlaceholder); // Assuming PLACEHOLDER_SLOT is correctly defined
        inv.setItem(NEXT_PAGE, nextPagePtr); // Assuming NEXT_PAGE is correctly defined
        inv.setItem(CUSTOM_ENCHANTS_PAGE, customEnchantPagePtr); // Assuming CUSTOM_ENCHANTS_PAGE is correctly defined
    }

    private static boolean isEnchantable(ItemStack item, Enchantment enchant) {
        final Set<Enchantment> ALLOWED_CROSSBOW_ENCHANTS = new HashSet<>(Arrays.asList(
            Enchantment.ARROW_DAMAGE
        ));

        if(item.getType() == Material.CROSSBOW && ALLOWED_CROSSBOW_ENCHANTS.contains(enchant)) {
            return true;
        } else {
            return enchant.canEnchantItem(item);
        }
    }

    public static List<EnchantmentOffer> getEnchants(ItemStack item) {
        final Set<Enchantment> BANNED_ENCHANTS = new HashSet<>(Arrays.asList(
            Enchantment.VANISHING_CURSE, 
            Enchantment.MENDING,
            Enchantment.BINDING_CURSE
        ));

        List<EnchantmentOffer> applicableEnchants = new ArrayList<>();
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            boolean enchantable = isEnchantable(item, enchantment);
            if ((enchantable || item.getType() == Material.BOOK) && !BANNED_ENCHANTS.contains(enchantment)) {
                EnchantmentOffer offer = new EnchantmentOffer(enchantment, 1, 0);
                applicableEnchants.add(offer);
            }
        }
        return applicableEnchants;
    }
}
