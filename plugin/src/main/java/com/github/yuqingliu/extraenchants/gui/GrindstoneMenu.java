package com.github.yuqingliu.extraenchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.item.ItemUtils;

public class GrindstoneMenu {
    private ItemUtils itemUtils;
    private final int START_SLOT = 0;
    private final int PLACEHOLDER_SLOT = 34;
    private final int PREVIOUS_PAGE = 6;
    private final int NEXT_PAGE = 51;
    private final int FINAL_SLOT = 50;
    private final int ITEM_SLOT = 25;
    private final List<Integer> BORDER_SLOTS = Arrays.asList(5,14,23,32,41,50); 
    private int PAGE_NUMBER = 1;
    private int MAX_PAGES = 25;
    private final List<Integer> frame = Arrays.asList(6,7,8,15,16,17,24,26,33,35,42,43,44,51,52,53);
    private HashMap<Integer,HashMap<Integer,Enchantment>> pageData = new HashMap<>();
    
    public GrindstoneMenu(ItemUtils itemUtils) {
        this.itemUtils = itemUtils;
    }
    
    private void initializePages() {
        for (int i = 1; i < 25; i++) {
            pageData.put(i, new HashMap<>());
        }
    }

    private ItemStack enchantmentOption(Enchantment enchant) {
        ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta metaOffer = enchantOption.getItemMeta();
        if (metaOffer != null) {
            Component enchantmentName = enchant.getName();
            metaOffer.displayName(enchantmentName);
            enchantOption.setItemMeta(metaOffer);
        }
        return enchantOption;
    }

    private int incrementPtr(int slotptr) {
        if(BORDER_SLOTS.contains(slotptr)) {
            return slotptr + 4;
        } else {
            return slotptr + 1;
        }
    }

    public void clearOptions(Inventory inv) {
        int slotptr = START_SLOT;
        pageData.clear();
        initializePages();
        PAGE_NUMBER = 1;
        while(slotptr <= FINAL_SLOT) {
            inv.setItem(slotptr, new ItemStack(Material.AIR));
            slotptr = incrementPtr(slotptr);
        }
    }

    private void voidOptions(Inventory inv) {
        int slotptr = START_SLOT;
        while(slotptr <= FINAL_SLOT) {
            inv.setItem(slotptr, new ItemStack(Material.AIR));
            slotptr = incrementPtr(slotptr);
        }
    }

    private void setPagePointers(Inventory inv) {
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
        inv.setItem(PREVIOUS_PAGE, prevPagePtr);
        inv.setItem(NEXT_PAGE, nextPagePtr);
    }

    private void displayItemFrame(Inventory inv) {
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
            enchantTablePlaceholderMeta.displayName(Component.text("Grindstone Slot", NamedTextColor.GREEN));
            enchantTablePlaceholder.setItemMeta(enchantTablePlaceholderMeta);
        }

        inv.setItem(PLACEHOLDER_SLOT, enchantTablePlaceholder); 
        setPagePointers(inv);
    }

    public void optionsFill(Inventory inv) {
        int slotptr = START_SLOT;
        ItemStack Placeholder = new ItemStack(Material.GLASS_PANE);
        ItemMeta PlaceholderMeta = Placeholder.getItemMeta();
        if (PlaceholderMeta != null) {
            PlaceholderMeta.displayName(Component.text("Unavailable", NamedTextColor.DARK_PURPLE));
            Placeholder.setItemMeta(PlaceholderMeta);
        }
        while(slotptr < FINAL_SLOT) {
            if(inv.getItem(slotptr) == null || inv.getItem(slotptr).getType() != Material.ENCHANTED_BOOK) {
                inv.setItem(slotptr, Placeholder);
            }
            slotptr = incrementPtr(slotptr);
        }
    }

    public void openGrindstoneMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Grindstone", NamedTextColor.GOLD));
        displayItemFrame(inv);
        optionsFill(inv);
        player.openInventory(inv);
    }

    public void displayOptions(Inventory inv, ItemStack item) {
        if(item == null) return;
        clearOptions(inv);
        Map<Enchantment, Integer> enchants = itemUtils.getEnchantments(item); 
        int slotptr = START_SLOT;
        MAX_PAGES = (int) Math.ceil((double) enchants.size() / 36.0);
        for(Enchantment offer : enchants.keySet()) {
            if(slotptr > FINAL_SLOT) {
                break;
            }
            inv.setItem(slotptr, enchantmentOption(offer));
            pageData.get(PAGE_NUMBER).put(slotptr, offer);
            slotptr = incrementPtr(slotptr);
        }
        optionsFill(inv);
    }

    public void displayNextOptionsPage(Inventory inv, ItemStack item) {
        if(item == null || (PAGE_NUMBER + 1 > MAX_PAGES)) {
            return;
        } 
        PAGE_NUMBER++;
        if(pageData.get(PAGE_NUMBER).isEmpty()) {
            displayNextEmptyOptionsPage(inv, item);
        }
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        for (int i = 0; i <= FINAL_SLOT; i++) {
            if(pageData.get(PAGE_NUMBER) == null || pageData.get(PAGE_NUMBER).isEmpty()) {
                 break;
            }
            Enchantment offer = pageData.get(PAGE_NUMBER).get(slotptr);
            if(offer == null) {
                continue;
            } 
            inv.setItem(slotptr, enchantmentOption(offer));
            slotptr = incrementPtr(slotptr);
        }
        // Fill options
        optionsFill(inv);
    }

    public void displayNextEmptyOptionsPage(Inventory inv, ItemStack item) {
        if(item == null) {
            return;
        } 
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        Map<Enchantment, Integer> enchants = itemUtils.getEnchantments(item);
        for (Enchantment offer : enchants.keySet()) {
            if(slotptr > FINAL_SLOT) {
                break;
            }
            boolean exists = false;
            for (int i = 1; i < PAGE_NUMBER; i++) {
                HashMap<Integer, Enchantment> data = pageData.get(i);
                if(data == null) {
                    continue;
                } 
                for (Enchantment precedentOffer : data.values()) {
                    if(offer.getName().equals(precedentOffer.getName())) {
                        exists = true;
                        break;
                    }
                }
            }
            if(exists) {
                continue;
            } 
            inv.setItem(slotptr, enchantmentOption(offer));
            pageData.get(PAGE_NUMBER).put(slotptr, offer);
            slotptr = incrementPtr(slotptr);
        }
        // Fill options
        optionsFill(inv);
    }

    public void displayPreviousOptionsPage(Inventory inv, ItemStack item) {
        if(item == null || PAGE_NUMBER == 1) {
            return;
        } 
        PAGE_NUMBER--;
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        for (int i = 0; i <= FINAL_SLOT; i++) {
            if(pageData.get(PAGE_NUMBER) == null || pageData.get(PAGE_NUMBER).isEmpty()) {
                continue;
            } 
            Enchantment offer = pageData.get(PAGE_NUMBER).get(slotptr);
            if(offer == null) {
                continue;
            } 
            inv.setItem(slotptr, enchantmentOption(offer));
            slotptr = incrementPtr(slotptr);
        }
        // Fill options
        optionsFill(inv);
    }

    public void applyOption(JavaPlugin plugin, Player player, Inventory inv, int slot, ItemStack item) {
        if(item == null) return;
        Enchantment enchant = pageData.get(PAGE_NUMBER).get(slot);
        if(enchant != null) {
            ItemStack newItem = enchant.removeEnchantment(item); 
            inv.setItem(ITEM_SLOT, newItem);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
            displayOptions(inv, newItem);
        } 
    }
}
