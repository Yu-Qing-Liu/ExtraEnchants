package com.github.yuqingliu.extraenchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Sound;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.enchants.utils.*;

public class GrindstoneMenu {
    private static final int START_SLOT = 0;
    private static final int PLACEHOLDER_SLOT = 34;
    private static final int PREVIOUS_PAGE = 6;
    private static final int NEXT_PAGE = 51;
    private static int PAGE_NUMBER = 1;
    private static int MAX_PAGES = 25;
    private static HashMap<Integer,HashMap<Integer,Enchantment>> pageDataVanillaEnchants = new HashMap<>();
    private static HashMap<Integer,HashMap<Integer,CustomEnchantment>> pageDataCustomEnchants = new HashMap<>();

    
    public static void openGrindstoneMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Grindstone", NamedTextColor.GOLD));
        displayItemFrame(inv);
        optionsFill(inv);
        player.openInventory(inv);
    }

    private static void initializePages() {
        for (int i = 1; i < 25; i++) {
            pageDataVanillaEnchants.put(i, new HashMap<>());
            pageDataCustomEnchants.put(i, new HashMap<>());
        }
    }

    public static void displayOptions(Inventory inv, ItemStack item) {
        if(item == null) return;
        clearOptions(inv);
        Map<Enchantment, Integer> vanillaEnchants = item.getEnchantments();
        Map<CustomEnchantment, Integer> customEnchants = UtilityMethods.getEnchantments(item);
        int slotptr = START_SLOT;
        MAX_PAGES = (int) Math.ceil((double) vanillaEnchants.size() / 36.0 + (double) customEnchants.size() / 36.0);
        for(Enchantment enchant : vanillaEnchants.keySet()) {
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = enchant.getKey().getKey();
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.AQUA));
                enchantOption.setItemMeta(metaOffer);
            }
            inv.setItem(slotptr, enchantOption);
            pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, enchant);
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
            for(CustomEnchantment enchant : customEnchants.keySet()) {
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = enchant.getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GOLD));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, enchant);
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

    public static void displayNextOptionsPage(Inventory inv, ItemStack item) {
        if(item == null) return;
        if(PAGE_NUMBER + 1 > MAX_PAGES) return;
        PAGE_NUMBER++;
        if(pageDataVanillaEnchants.get(PAGE_NUMBER).isEmpty() && pageDataCustomEnchants.get(PAGE_NUMBER).isEmpty()) {
            displayNextEmptyOptionsPage(inv, item);
        }
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        for (int i = 0; i < 51; i++) {
            if(pageDataVanillaEnchants.get(PAGE_NUMBER) == null || pageDataVanillaEnchants.get(PAGE_NUMBER).isEmpty()) break;
            Enchantment offer = pageDataVanillaEnchants.get(PAGE_NUMBER).get(slotptr);
            if(offer == null) continue;
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = offer.getKey().getKey();
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
        if(slotptr < 50 || inv.getItem(50) == null || inv.getItem(50).getType() == Material.AIR) {
            for (int i = 0; i < 51; i++) {
                if(pageDataCustomEnchants.get(PAGE_NUMBER) == null || pageDataCustomEnchants.get(PAGE_NUMBER).isEmpty()) break;
                CustomEnchantment offer = pageDataCustomEnchants.get(PAGE_NUMBER).get(slotptr);
                if(offer == null) continue;
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getName();
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

    public static void displayNextEmptyOptionsPage(Inventory inv, ItemStack item) {
        if(item == null) return;
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        int itemcount = 0;
        Map<Enchantment, Integer> vanillaEnchants = item.getEnchantments();
        Map<CustomEnchantment, Integer> customEnchants = UtilityMethods.getEnchantments(item);
        for (Enchantment offer : vanillaEnchants.keySet()) {
            boolean exists = false;
            for (int i = 1; i < PAGE_NUMBER; i++) {
                HashMap<Integer, Enchantment> data = pageDataVanillaEnchants.get(i);
                if(data == null) continue;
                for (Enchantment precedentOffer : data.values()) {
                    if(offer.getKey().equals(precedentOffer.getKey())) {
                        exists = true;
                        break;
                    }
                }
            }
            if(exists) continue;
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = offer.getKey().getKey();
                metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.AQUA));
                enchantOption.setItemMeta(metaOffer);
            }
            inv.setItem(slotptr, enchantOption);
            pageDataVanillaEnchants.get(PAGE_NUMBER).put(slotptr, offer);
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
            for (CustomEnchantment offer : customEnchants.keySet()) {
                boolean exists = false;
                for (int i = 0; i < PAGE_NUMBER; i++) {
                    HashMap<Integer, CustomEnchantment> data = pageDataCustomEnchants.get(i);
                    if(data == null) continue;
                    for (CustomEnchantment precedentOffer : data.values()) {
                        if(offer.getName().equals(precedentOffer.getName())) {
                            exists = true;
                            break;
                        }
                    }
                }
                if(exists) continue;
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getName();
                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName), NamedTextColor.GOLD));
                    enchantOption.setItemMeta(metaOffer);
                }
                inv.setItem(slotptr, enchantOption);
                pageDataCustomEnchants.get(PAGE_NUMBER).put(slotptr, offer);
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

    public static void displayPreviousOptionsPage(Inventory inv, ItemStack item) {
        if(item == null) return;
        if(PAGE_NUMBER == 1) return;
        PAGE_NUMBER--;
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        for (int i = 0; i < 51; i++) {
            if(pageDataVanillaEnchants.get(PAGE_NUMBER) == null || pageDataVanillaEnchants.get(PAGE_NUMBER).isEmpty()) continue;
            Enchantment offer = pageDataVanillaEnchants.get(PAGE_NUMBER).get(slotptr);
            if(offer == null) continue;
            ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta metaOffer = enchantOption.getItemMeta();
            if (metaOffer != null) {
                String enchantmentName = offer.getKey().getKey();
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
        if(slotptr < 50 || inv.getItem(50) == null || inv.getItem(50).getType() == Material.AIR) {
            for (int i = 0; i < 51; i++) {
                if(pageDataVanillaEnchants.get(PAGE_NUMBER) == null || pageDataVanillaEnchants.get(PAGE_NUMBER).isEmpty()) break;
                CustomEnchantment offer = pageDataCustomEnchants.get(PAGE_NUMBER).get(slotptr);
                if(offer == null) continue;
                ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta metaOffer = enchantOption.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getName();
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

    public static void applyOption(Player player, Inventory inv, int slot, ItemStack item) {
        if(item == null) return;
        Enchantment enchant = pageDataVanillaEnchants.get(PAGE_NUMBER).get(slot);
        CustomEnchantment customEnchant = pageDataCustomEnchants.get(PAGE_NUMBER).get(slot);

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
        
        ItemStack enchantTablePlaceholder = new ItemStack(Material.GRINDSTONE);
        ItemMeta enchantTablePlaceholderMeta = enchantTablePlaceholder.getItemMeta();
        if (enchantTablePlaceholderMeta != null) {
            enchantTablePlaceholderMeta.displayName(Component.text("Grindstone Slot", NamedTextColor.GREEN));
            enchantTablePlaceholder.setItemMeta(enchantTablePlaceholderMeta);
        }

        inv.setItem(PLACEHOLDER_SLOT, enchantTablePlaceholder); // Assuming PLACEHOLDER_SLOT is correctly defined
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
}

