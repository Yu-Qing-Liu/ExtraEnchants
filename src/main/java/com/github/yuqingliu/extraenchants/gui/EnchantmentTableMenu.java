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
import com.github.yuqingliu.extraenchants.utils.UtilityMethods;

public class EnchantmentTableMenu {
    private static final int ITEM_SLOT = 16;
    private static final int START_SLOT = 10;
    private static final int CONFIRM_SLOT = 19;
    private static final int MAX_ENCHANTMENT_LEVEL = Constants.getMaxEnchantLevel();
    private static final int BOOKSHELF_MULTIPLIER = Constants.getBookshelfMultiplier();
    private static final int MAX_BOOKSHELVES = Constants.getMaxBookshelves();
    private static final int NUM_OFFERS = 5;
    private static List<EnchantmentOffer[]> tableOffers;
    private static double maxEnchantmentLevel;
    private static int step;

    
    public static void openEnchantmentTableMenu(Player player, int bookshelves) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE));
        maxEnchantmentLevel = Math.ceil(Math.min((double) bookshelves / MAX_BOOKSHELVES, 1) * MAX_ENCHANTMENT_LEVEL); 
        step = (bookshelves * BOOKSHELF_MULTIPLIER) / NUM_OFFERS;
        initializeEnchantmentTableMenu(inv);
        displayEmptyEnchantmentOptions(inv);
        CustomEnchantmentTableMenu.openEnchantmentTableMenu(player, bookshelves, inv);
        player.openInventory(inv);
    }

    public static void displayEnchantmentOptions(Inventory inv, ItemStack item) {
        int slot = START_SLOT; // Starting slot for offers in the GUI
        int confirmSlot = CONFIRM_SLOT; // Starting slot for offers in the GUI
        int offerCounter = 1;
        Random random = new Random();
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
                    String enchantmentName = offer.getEnchantment().getKey().getKey();
                    int level = offer.getEnchantmentLevel();

                    metaOffer.displayName(Component.text(UtilityMethods.formatString(enchantmentName) + " " + UtilityMethods.toRoman(level), NamedTextColor.AQUA));
                    offerItem.setItemMeta(metaOffer);
                }
                if (metaConfirm != null) {
                    metaConfirm.displayName(Component.text(experienceLevel, NamedTextColor.GREEN));
                    confirmItem.setItemMeta(metaConfirm);
                }

                inv.setItem(slot++, offerItem); // Place the item in the GUI and move to the next slot
                inv.setItem(confirmSlot++, confirmItem); // Place the item in the GUI and move to the next slot
            }
            offerCounter++;
        }
        CustomEnchantmentTableMenu.displayEnchantmentOptions(inv, item);
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
        CustomEnchantmentTableMenu.displayEmptyEnchantmentOptions(inv);
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

        // Return if the item is already enchanted
        if(!item.getEnchantments().isEmpty() && !Constants.getAllowReEnchanting()) {
            player.sendMessage(Component.text("Item is already enchanted.", NamedTextColor.RED));
            return;
        }

        EnchantmentOffer[] offers = tableOffers.get(offerCounter - 1); // Adjust for zero-based indexing
        int playerLevel = player.getLevel();
        int requiredLevel = offerCounter * step; // Assuming 'step' is defined and accessible

        if (playerLevel >= requiredLevel) {
            for (EnchantmentOffer offer : offers) {
                if (offer != null) {
                    item.addUnsafeEnchantment(offer.getEnchantment(), offer.getEnchantmentLevel());
                }
            }
            player.setLevel(playerLevel - requiredLevel); // Deduct the required levels from the player
            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
            player.sendMessage(Component.text("Enchantments applied successfully!", NamedTextColor.GREEN));
        } else {
            player.sendMessage(Component.text("Not enough experience levels.", NamedTextColor.RED));
        }
    }

    public static void initializeEnchantmentTableMenu(Inventory inv) {
        ItemStack purpleGlassPane = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta meta = purpleGlassPane.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Unavailable", NamedTextColor.DARK_PURPLE));
            purpleGlassPane.setItemMeta(meta);
        }
        
        for (int i = 0; i < inv.getSize(); i++) {
            // Correctly exclude the item and lapis slots from being filled with glass panes
            if ((i != ITEM_SLOT) && (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR)) {
                inv.setItem(i, purpleGlassPane);
            }
        }
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

    private static List<Enchantment> getEnchants(ItemStack item) {
        final Set<Enchantment> BANNED_ENCHANTS = new HashSet<>(Arrays.asList(
            Enchantment.VANISHING_CURSE, 
            Enchantment.MENDING,
            Enchantment.BINDING_CURSE
        ));

        List<Enchantment> applicableEnchants = new ArrayList<>();
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            boolean enchantable = isEnchantable(item, enchantment);
            if ((enchantable || item.getType() == Material.BOOK) && !BANNED_ENCHANTS.contains(enchantment)) {
                applicableEnchants.add(enchantment);
            }
        }
        return applicableEnchants;
    }

    private static int calculateEnchantmentLevel(Enchantment selectedEnchantment, double enchantmentLevel, double maxEnchantmentLevel) {
        final Set<Enchantment> UNCAPPED_ENCHANTS = new HashSet<>(Arrays.asList(
            Enchantment.PROTECTION_ENVIRONMENTAL, 
            Enchantment.PROTECTION_FALL,
            Enchantment.DAMAGE_ALL,
            Enchantment.ARROW_DAMAGE,
            Enchantment.DAMAGE_UNDEAD,
            Enchantment.DAMAGE_ARTHROPODS,
            Enchantment.DURABILITY,
            Enchantment.DIG_SPEED
        ));

        if(UNCAPPED_ENCHANTS.contains(selectedEnchantment)) {
            return (int) Math.min(maxEnchantmentLevel, UtilityMethods.RandomIntBetween((int)(enchantmentLevel - 1), (int)(enchantmentLevel + 1)));
        } else {
            return (int) Math.min(selectedEnchantment.getMaxLevel(), maxEnchantmentLevel);
        }
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
                int selectedEnchantmentLevel = calculateEnchantmentLevel(selectedEnchantment, enchantmentLevel, maxEnchantmentLevel);
                offers[i] = new EnchantmentOffer(selectedEnchantment, selectedEnchantmentLevel, (int) ((i + 1) * enchantmentLevel));
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
