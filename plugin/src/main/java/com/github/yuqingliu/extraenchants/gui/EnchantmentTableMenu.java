package com.github.yuqingliu.extraenchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentOffer;
import com.github.yuqingliu.extraenchants.utils.MathUtils;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

public class EnchantmentTableMenu {
    private final int START_SLOT = 0;
    private final int PREVIOUS_PAGE = 6;
    private final int NEXT_PAGE = 51;
    private final int ITEM_SLOT = 25;
    private final int PLACEHOLDER_SLOT = 34;
    private final int FINAL_SLOT = 50;
    private final int CAPACITY = 36;
    private final List<Integer> BORDER_SLOTS = Arrays.asList(5,14,23,32,41,50); 
    private int PAGE_NUMBER = 1;
    private int MAX_PAGES = 25;
    private HashMap<Integer, HashMap<Integer, EnchantmentOffer>> pageData= new HashMap<>();
    private Map<String, Enchantment> enchantmentRegistry;
    private EnchantmentOffer selectedOffer = null;

    public EnchantmentTableMenu(Map<String, Enchantment> registry) {
        this.enchantmentRegistry = registry;
    }
    
    private void initializePages() {
        for (int i = 1; i < 25; i++) {
            pageData.put(i, new HashMap<>());
        }
    }

    private int incrementPtr(int slotptr) {
        if(BORDER_SLOTS.contains(slotptr)) {
            return slotptr + 4;
        } else {
            return slotptr + 1;
        }
    }

    private ItemStack enchantmentOption(EnchantmentOffer offer) {
        ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
        Enchantment enchantment = offer.getEnchantment();
        ItemMeta metaOffer = enchantOption.getItemMeta();
        if (metaOffer != null) {
            List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
            Component name = enchantment.getName();
            Component description = enchantment.getDescription();
            existingLore.add(description);
            metaOffer.lore(existingLore);
            metaOffer.displayName(name);
            enchantOption.setItemMeta(metaOffer);
        }
        return enchantOption;
    }

    private ItemStack enchantmentOptionLeveled(EnchantmentOffer offer) {
        ItemStack enchantOption = new ItemStack(Material.ENCHANTED_BOOK);
        Enchantment enchantment = offer.getEnchantment();
        ItemMeta metaOffer = enchantOption.getItemMeta();
        if (metaOffer != null) {
            List<Component> existingLore = metaOffer.lore() != null ? metaOffer.lore() : new ArrayList<>();
            Component enchantmentName = enchantment.getName();
            Component name = enchantmentName.append(Component.text(" " + TextUtils.toRoman(offer.getLevel()), enchantment.getNameColor()));
            Component lvlReq = Component.text("Required Level: " + offer.getRequiredLevel(), NamedTextColor.GREEN);
            Component expCost = Component.text("Experience Cost: " + offer.getCost(), NamedTextColor.DARK_GREEN);
            Component description = enchantment.getLeveledDescription(offer.getLevel());
            existingLore.add(lvlReq);
            existingLore.add(expCost);
            existingLore.add(description);
            metaOffer.lore(existingLore);
            metaOffer.displayName(name);
            enchantOption.setItemMeta(metaOffer);
        }
        return enchantOption;
    }

    private void applyOffer(Inventory inv, EnchantmentOffer selectedOffer, Player player, ItemStack item) {
        // Apply the enchantment and return
        if(player.getLevel() >= selectedOffer.getRequiredLevel()) {
            ItemStack enchantedItem = selectedOffer.applyOffer(player, item);
            if(enchantedItem != null) {
                inv.setItem(ITEM_SLOT, enchantedItem);
                displayEnchantmentOptions(inv, enchantedItem);
                return;
            } 
        } else {
            player.sendMessage(Component.text("Not enough experience", NamedTextColor.RED));
        }
        displayEnchantmentOptions(inv, item);
    }

    private void setPagePointers(Inventory inv) {
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

    private void setPagePointersLeveled(Inventory inv) {
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

    private EnchantmentOffer generateLeveledOffer(Enchantment enchantment, int level) {
        String levelFormula = enchantment.getRequiredLevelFormula();
        String costFormula = enchantment.getCostFormula();
        int levelRequired = MathUtils.evaluateExpression(levelFormula, level);
        int cost = MathUtils.evaluateExpression(costFormula, level);
        return new EnchantmentOffer(enchantment, level, levelRequired, cost);
    }

    private void displayItemFrame(Inventory inv) {
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
        
        ItemStack enchantTablePlaceholder = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta enchantTablePlaceholderMeta = enchantTablePlaceholder.getItemMeta();
        if (enchantTablePlaceholderMeta != null) {
            enchantTablePlaceholderMeta.displayName(Component.text("Enchantment Slot", NamedTextColor.GREEN));
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
        while(slotptr <= FINAL_SLOT) {
            if(inv.getItem(slotptr) == null || inv.getItem(slotptr).getType() != Material.ENCHANTED_BOOK) {
                inv.setItem(slotptr, Placeholder);
            }
            slotptr = incrementPtr(slotptr);
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

    private List<EnchantmentOffer> getEnchants(ItemStack item) {
        List<EnchantmentOffer> applicableEnchants = new ArrayList<>();
        for (Enchantment enchantment : enchantmentRegistry.values()) {
            if (enchantment.canEnchant(item)) {
                int maxLevel = enchantment.getMaxLevel();
                int itemEnchantLevel = enchantment.getEnchantmentLevel(item);
                if(maxLevel > 0 && itemEnchantLevel < maxLevel) {
                    EnchantmentOffer offer = new EnchantmentOffer(enchantment, maxLevel, 0, 0);
                    applicableEnchants.add(offer);
                }
            }
        }
        return applicableEnchants;
    }

    public void openEnchantmentTableMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE));
        displayItemFrame(inv);
        optionsFill(inv);
        initializePages();
        player.openInventory(inv);
    }

    public void displayEnchantmentOptions(Inventory inv, ItemStack item) {
        if(item == null) {
            return;
        }
        // Clear options
        clearOptions(inv);
        // Display all enchantments that are applicable for the item
        int slotptr = START_SLOT;
        List<EnchantmentOffer> applicableEnchants = getEnchants(item);
        MAX_PAGES = (int) Math.ceil((double) applicableEnchants.size() / 36.0);
        for (EnchantmentOffer offer : applicableEnchants) {
            if(slotptr > FINAL_SLOT) {
                break;
            }
            pageData.get(PAGE_NUMBER).put(slotptr, offer);
            inv.setItem(slotptr, enchantmentOption(offer));
            slotptr = incrementPtr(slotptr);
        }
        // Fill options
        optionsFill(inv);
        setPagePointers(inv);
    }

    public void displayNextEnchantmentOptionsPage(Inventory inv, ItemStack item) {
        if(item == null || (PAGE_NUMBER + 1 > MAX_PAGES)) {
            return;
        } 
        PAGE_NUMBER++;
        if(pageData.get(PAGE_NUMBER).isEmpty()) {
            displayNextEmptyEnchantmentOptionsPage(inv, item);
        }
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        for (int i = 0; i <= FINAL_SLOT; i++) {
            if(pageData.get(PAGE_NUMBER) == null || pageData.get(PAGE_NUMBER).isEmpty()) break;
            EnchantmentOffer offer = pageData.get(PAGE_NUMBER).get(slotptr);
            if(offer == null) {
                continue;
            } 
            inv.setItem(slotptr, enchantmentOption(offer));
            slotptr = incrementPtr(slotptr);
        }
        // Fill options
        optionsFill(inv);
    }

    public void displayNextEmptyEnchantmentOptionsPage(Inventory inv, ItemStack item) {
        if(item == null) {
            return;
        } 
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        List<EnchantmentOffer> applicableEnchants = getEnchants(item);
        for (EnchantmentOffer offer : applicableEnchants) {
            if(slotptr > FINAL_SLOT) {
                break;
            }
            boolean exists = false;
            for (int i = 1; i < PAGE_NUMBER; i++) {
                HashMap<Integer, EnchantmentOffer> data = pageData.get(i);
                if(data == null) continue;
                for (EnchantmentOffer precedentOffer : data.values()) {
                    if(offer.getEnchantment().getName().equals(precedentOffer.getEnchantment().getName())) {
                        exists = true;
                        break;
                    }
                }
            }
            if(exists) {
                continue;
            } 
            pageData.get(PAGE_NUMBER).put(slotptr, offer);
            inv.setItem(slotptr, enchantmentOption(offer));
            slotptr = incrementPtr(slotptr);
        }
        // Fill options
        optionsFill(inv);
    }

    public void displayPreviousEnchantmentOptionsPage(Inventory inv, ItemStack item) {
        if(item == null || PAGE_NUMBER == 1) {
            return;
        } 
        PAGE_NUMBER--;
        voidOptions(inv);
        // Display the rest of enchantments that are applicable for the item
        int slotptr = START_SLOT;
        for (int i = 0; i <= FINAL_SLOT; i++) {
            if(pageData.get(PAGE_NUMBER) == null || pageData.get(PAGE_NUMBER).isEmpty()) continue;
            EnchantmentOffer offer = pageData.get(PAGE_NUMBER).get(slotptr);
            if(offer == null) {
                continue;
            } 
            inv.setItem(slotptr, enchantmentOption(offer));
            slotptr = incrementPtr(slotptr);
        }
        // Fill options
        optionsFill(inv);
    }
    

    public void displaySelectedEnchantmentOptions(JavaPlugin plugin, Player player, ItemStack item, Inventory inv, int slot) {
        if(item == null) {
            return;
        } 
        EnchantmentOffer selectedOffer = pageData.get(PAGE_NUMBER).get(slot);
        this.selectedOffer = selectedOffer;
        clearOptions(inv);
        if(selectedOffer != null) {
            if(selectedOffer.getCost() > 0) {
                applyOffer(inv, selectedOffer, player, item);
                return;
            }
            int slotptr = 0;
            Enchantment enchantment = selectedOffer.getEnchantment();
            int maxEnchantmentLevel = enchantment.getMaxLevel();
            MAX_PAGES = (int) Math.ceil((double) maxEnchantmentLevel / 36.0);
            for (int i = 1; i <= maxEnchantmentLevel; i++) {
                if(slotptr > FINAL_SLOT) {
                    break;
                }
                EnchantmentOffer offer = generateLeveledOffer(enchantment, i);
                pageData.get(PAGE_NUMBER).put(slotptr, offer);
                inv.setItem(slotptr, enchantmentOptionLeveled(offer));
                slotptr = incrementPtr(slotptr);
            }
        } 
        // Fill options
        optionsFill(inv);
        setPagePointersLeveled(inv);
    }

    public void displayNextSelectedEnchantmentOptions(JavaPlugin plugin, Player player, ItemStack item, Inventory inv, int slot) {
        if(item == null || (PAGE_NUMBER + 1 > MAX_PAGES)) {
            return;
        }
        PAGE_NUMBER++;
        EnchantmentOffer selectedOffer = this.selectedOffer;
        voidOptions(inv);
        if(selectedOffer != null) {
            if(selectedOffer.getCost() > 0) {
                applyOffer(inv, selectedOffer, player, item);
                return;
            }
            int slotptr = 0;
            HashMap<Integer, EnchantmentOffer> previousSlotData = pageData.get(PAGE_NUMBER - 1);
            List<Integer> levels = new ArrayList<>();
            for(EnchantmentOffer offer : previousSlotData.values()) {
                levels.add(offer.getLevel());
            }
            int startLevel = Collections.max(levels) + 1;
            Enchantment enchantment = selectedOffer.getEnchantment();
            int maxEnchantmentLevel = enchantment.getMaxLevel();
            for (int i = startLevel; i <= maxEnchantmentLevel; i++) {
                if(slotptr > FINAL_SLOT) {
                    break;
                }
                EnchantmentOffer offer = generateLeveledOffer(enchantment, i);
                pageData.get(PAGE_NUMBER).put(slotptr, offer);
                inv.setItem(slotptr, enchantmentOptionLeveled(offer));
                slotptr = incrementPtr(slotptr);
            }
        } 
        // Fill options
        optionsFill(inv);
        setPagePointersLeveled(inv);
    }

    public void displayPreviousSelectedEnchantmentOptions(JavaPlugin plugin, Player player, ItemStack item, Inventory inv, int slot) {
        if(item == null) {
            return;
        } 
        if(PAGE_NUMBER == 1) {
            displayEnchantmentOptions(inv, item);
            return;
        }
        PAGE_NUMBER--;
        EnchantmentOffer selectedOffer = this.selectedOffer;
        voidOptions(inv);
        if(selectedOffer != null) {
            if(selectedOffer.getCost() > 0) {
                applyOffer(inv, selectedOffer, player, item);
                return;
            }
            int slotptr = 0;
            HashMap<Integer, EnchantmentOffer> previousSlotData = pageData.get(PAGE_NUMBER + 1);
            List<Integer> levels = new ArrayList<>();
            for(EnchantmentOffer offer : previousSlotData.values()) {
                levels.add(offer.getLevel());
            }
            int startLevel = Collections.min(levels) - 1;
            Enchantment enchantment = selectedOffer.getEnchantment();
            int maxEnchantmentLevel = startLevel;
            for (int i = startLevel - CAPACITY; i <= maxEnchantmentLevel; i++) {
                if(slotptr > FINAL_SLOT) {
                    break;
                }
                EnchantmentOffer offer = generateLeveledOffer(enchantment, i);
                pageData.get(PAGE_NUMBER).put(slotptr, offer);
                inv.setItem(slotptr, enchantmentOptionLeveled(offer));
                slotptr = incrementPtr(slotptr);
            }
        } 
        // Fill options
        optionsFill(inv);
        setPagePointersLeveled(inv);
    }
}
