package com.github.yuqingliu.extraenchants.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.Sound;

import java.util.Random;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Collections;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.database.Constants;
import com.github.yuqingliu.extraenchants.utils.Enchantment;
import com.github.yuqingliu.extraenchants.utils.EnchantmentOffer;
import com.github.yuqingliu.extraenchants.utils.UtilityMethods;

public class CustomEnchantmentTableMenu {
    public static final int ITEM_SLOT = 16;
    private static final int START_SLOT = 37;
    private static final int CONFIRM_SLOT = 46;
    private static final int MAX_ENCHANTMENT_LEVEL = Constants.getMaxEnchantLevel();
    private static final int BOOKSHELF_MULTIPLIER = Constants.getBookshelfMultiplier();
    private static final int MAX_BOOKSHELVES = Constants.getMaxBookshelves();
    private static final int NUM_OFFERS = 5;
    private static List<EnchantmentOffer[]> tableOffers;
    private static double maxEnchantmentLevel;
    private static int step;

    public static void openEnchantmentTableMenu(Player player, int bookshelves, Inventory inv) {
        maxEnchantmentLevel = Math.ceil(Math.min((double) bookshelves / MAX_BOOKSHELVES, 1) * MAX_ENCHANTMENT_LEVEL); 
        step = (bookshelves * BOOKSHELF_MULTIPLIER) / NUM_OFFERS;
        displayEmptyEnchantmentOptions(inv);
    }

    public static void displayEnchantmentOptions(Inventory inv, ItemStack item) {
        int slot = START_SLOT; // Starting slot for offers in the GUI
        int confirmSlot = CONFIRM_SLOT; // Starting slot for offers in the GUI
        int offerCounter = 1;
        tableOffers = generateTableOffers(NUM_OFFERS, maxEnchantmentLevel, item);

        if(tableOffers.get(0).length == 0) {
            displayEmptyEnchantmentOptions(inv);
            return;
        }

        for (EnchantmentOffer[] offerList : tableOffers) {
            int experienceLevel = step * offerCounter;
            if (offerList.length > 0) {
                EnchantmentOffer offer = offerList[UtilityMethods.RandomIntBetween(0, offerList.length - 1)];

                ItemStack offerItem = new ItemStack(Material.ENCHANTED_BOOK);
                ItemStack confirmItem = new ItemStack(Material.ENCHANTING_TABLE);
                ItemMeta metaOffer = offerItem.getItemMeta();
                ItemMeta metaConfirm = confirmItem.getItemMeta();
                if (metaOffer != null) {
                    String enchantmentName = offer.getEnchantment().getName();
                    int level = offer.getEnchantmentLevel();

                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(level), NamedTextColor.GOLD));
                    offerItem.setItemMeta(metaOffer);
                }
                if (metaConfirm != null) {
                    metaConfirm.displayName(Component.text(experienceLevel, NamedTextColor.GREEN));
                    confirmItem.setItemMeta(metaConfirm);
                }

                inv.setItem(slot++, offerItem); // Place the item in the GUI and move to the next slot
                inv.setItem(confirmSlot++, confirmItem); // Place the item in the GUI and move to the next slot
                //
            }
            offerCounter++;
        }
    }

    public static void displayEmptyEnchantmentOptions(Inventory inv) {
        int slot = START_SLOT; // Starting slot for offers in the GUI
        int confirmSlot = CONFIRM_SLOT;
        for (int i = 0; i < NUM_OFFERS; i++) {
            ItemStack offerItem = new ItemStack(Material.ENCHANTED_BOOK);
            ItemStack confirmItem = new ItemStack(Material.ENCHANTING_TABLE);

            inv.setItem(slot++, offerItem); // Place the item in the GUI and move to the next slot
            inv.setItem(confirmSlot++, confirmItem); // Place the item in the GUI and move to the next slot
        }
    }

    public static void updateEnchantmentTableMenu(Player player) {
        Inventory inv = player.getOpenInventory().getTopInventory();
        if (inv == null || !inv.getViewers().contains(player)) return; // Ensure the inventory is still open

        // Clear previous offers
        for (int i = START_SLOT; i <= START_SLOT + 4; i++) {
            inv.clear(i);
        }

        for (int i = CONFIRM_SLOT; i <= CONFIRM_SLOT + 4; i++) {
            inv.clear(i);
        }

        // Check for an item in the enchantment slot
        ItemStack itemInSlot = inv.getItem(ITEM_SLOT);
        if (itemInSlot != null && itemInSlot.getType() != Material.AIR) {
            // Item present, generate and display new offers
            displayEnchantmentOptions(inv, itemInSlot);
        } else {
            // No item, display empty options or placeholders
            displayEmptyEnchantmentOptions(inv);
        }
    }

    public static void applyEnchants(int offerCounter, ItemStack item, Player player) {
        if(tableOffers == null) return;
        if(item == null) return;
        if (offerCounter < 1 || offerCounter > tableOffers.size()) {
            // Invalid offer counter, handle appropriately
            player.sendMessage(Component.text("Invalid enchantment selection.", NamedTextColor.RED));
            return;
        }

        EnchantmentOffer[] offers = tableOffers.get(offerCounter - 1); // Adjust for zero-based indexing
        int playerLevel = player.getLevel();
        int requiredLevel = offerCounter * step; // Assuming 'step' is defined and accessible

        if (playerLevel >= requiredLevel) {
            for (EnchantmentOffer offer : offers) {
                if (offer != null) {
                    if (!UtilityMethods.addEnchantment(item, offer.getEnchantment().getName(), offer.getEnchantmentLevel())) {
                        player.sendMessage(Component.text("You cannot add the same custom enchantment"));
                        return;
                    }
                }
            }
            player.setLevel(playerLevel - requiredLevel); // Deduct the required levels from the player
            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
            player.sendMessage(Component.text("Enchantments applied successfully!", NamedTextColor.GREEN));
        } else {
            player.sendMessage(Component.text("Not enough experience levels.", NamedTextColor.RED));
        }
    }

    private static List<Enchantment> getEnchants(ItemStack item) {
        List<Enchantment> applicableEnchants = new ArrayList<>();
        // Bows
        Enchantment homing = new Enchantment("Homing", 1);
        // Armor
        Enchantment mitigation = new Enchantment("Mitigation", 5);
        // Sword
        
        if(item.getType() == Material.BOW) {
            applicableEnchants.add(homing);
        }
        if(UtilityMethods.isArmor(item)) {
            applicableEnchants.add(mitigation);
        }

        return applicableEnchants;
    }
    
    private static EnchantmentOffer[] generateEnchantmentOffers(int numOffers, double enchantmentLevel, ItemStack item) {
        Random random = new Random();
        List<Enchantment> availableEnchants = getEnchants(item);
        numOffers = Math.min(random.nextInt(numOffers) + 1, availableEnchants.size());
        EnchantmentOffer[] offers = new EnchantmentOffer[numOffers];

        Collections.shuffle(availableEnchants);
        Set<Enchantment> usedEnchantments = new HashSet<>();

        for (int i = 0; i < offers.length && !availableEnchants.isEmpty(); i++) {
            Enchantment selectedEnchantment = null;
            List<Enchantment> incompatibleEnchants = new ArrayList<>();

            for (Enchantment enchantment : availableEnchants) {
                if (!usedEnchantments.contains(enchantment)) {
                    boolean isCompatible = usedEnchantments.stream().noneMatch(used -> enchantment.conflictsWith(used) || used.conflictsWith(enchantment));

                    if (isCompatible) {
                        selectedEnchantment = enchantment;
                        break;
                    } else {
                        incompatibleEnchants.add(enchantment);
                    }
                }
            }

            if (selectedEnchantment != null) {
                usedEnchantments.add(selectedEnchantment);
                offers[i] = new EnchantmentOffer(selectedEnchantment, Math.min(UtilityMethods.RandomIntBetween((int) enchantmentLevel - 1, (int) enchantmentLevel + 1), selectedEnchantment.getMaxLevel()), (int) ((i + 1) * enchantmentLevel));
                // Now remove the selected and incompatible enchantments from the list
                availableEnchants.remove(selectedEnchantment);
                availableEnchants.removeAll(incompatibleEnchants);
            }
        }

        return Arrays.stream(offers).filter(Objects::nonNull).toArray(EnchantmentOffer[]::new);
    }

    private static List<EnchantmentOffer[]> generateTableOffers(int numOffers, double maxEnchantmentLevel, ItemStack item) {
        List<EnchantmentOffer[]> offers = new ArrayList<>();
        double step = maxEnchantmentLevel / (double) numOffers;

        for (int i = 0; i < numOffers; i++) {
            EnchantmentOffer[] levelOffers = generateEnchantmentOffers(Math.max(Constants.getHiddenEnchantsCount(), 0), Math.max((i+1) * step, 1), item);
            offers.add(levelOffers);
        }

        return offers;
    }
    
}
