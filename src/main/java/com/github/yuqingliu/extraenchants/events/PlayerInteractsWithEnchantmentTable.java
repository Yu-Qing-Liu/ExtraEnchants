package com.github.yuqingliu.extraenchants.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.inventory.InventoryAction;

import com.github.yuqingliu.extraenchants.gui.EnchantmentTableMenu;
import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerInteractsWithEnchantmentTable implements Listener {
    private JavaPlugin plugin;
    private static final int ITEM_SLOT = 25;
    private static final int PREVIOUS_PAGE = 6;
    private static final int NEXT_PAGE = 51;
    private static int BOOKSHELVES = 1;
    List<Integer> frame = Arrays.asList(7,8,15,16,17,24,26,33,34,35,42,43,44,52,53);
    List<Integer> options = Arrays.asList(0,1,2,3,4,5,9,10,11,12,13,14,18,19,20,21,22,23,27,28,29,30,31,32,36,37,38,39,40,41,45,46,47,48,49,50);

    public PlayerInteractsWithEnchantmentTable(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        // Check if the event is a right-click on a block
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.ENCHANTING_TABLE) {
            event.setCancelled(true); // Prevent the default enchantment table GUI from opening

            BOOKSHELVES = UtilityMethods.countSurroundingEffectiveBlocks(block, Material.BOOKSHELF);
            EnchantmentTableMenu.openEnchantmentTableMenu(player, BOOKSHELVES);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getView().title().equals(Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE))) {
            wait(event.getInventory());
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        Inventory actionInventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();

        if (clickedInventory == null || currentItem == null || !event.getView().title().equals(Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE))) {
            return;
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if(frame.contains(event.getRawSlot()) || options.contains(event.getRawSlot()) || event.getRawSlot() == NEXT_PAGE || event.getRawSlot() == PREVIOUS_PAGE) {
                event.setCancelled(true);
                return;
            } 
            // Handle the shift-click case
            ItemStack itemInTargetSlot = actionInventory.getItem(ITEM_SLOT);
            if (itemInTargetSlot == null || itemInTargetSlot.getType() == Material.AIR) {
                // If the ITEM_SLOT is empty, move the current item there
                actionInventory.setItem(ITEM_SLOT, currentItem.clone());
                clickedInventory.clear(event.getSlot()); // Clear the slot from where the item was moved
                event.setCancelled(true); // Prevent default behavior
                EnchantmentTableMenu.displayEnchantmentOptions(actionInventory, currentItem);
            } else {
                EnchantmentTableMenu.clearOptions(actionInventory);
                EnchantmentTableMenu.optionsFill(actionInventory);
                wait(actionInventory);
            }
            return;
        }

        if (clickedInventory.equals(player.getOpenInventory().getTopInventory())) {
            int slot = event.getSlot();
            ItemStack item = clickedInventory.getItem(ITEM_SLOT);
            ItemStack itemClicked = clickedInventory.getItem(slot);
            if (frame.contains(slot)) {
                event.setCancelled(true);
                return;
            }
            if(slot == ITEM_SLOT) {
                EnchantmentTableMenu.clearOptions(clickedInventory);
                EnchantmentTableMenu.optionsFill(clickedInventory);
                wait(clickedInventory);
                return;
            } else if(slot == NEXT_PAGE) {
                ItemStack ptr = clickedInventory.getItem(slot);
                ItemMeta ptrMeta = ptr.getItemMeta();
                if(ptrMeta != null) {
                    if(ptrMeta.displayName().equals(Component.text("Next Page", NamedTextColor.AQUA))) {
                        EnchantmentTableMenu.displayNextEnchantmentOptionsPage(clickedInventory, item);
                    }
                    else if(ptrMeta.displayName().equals(Component.text("Next Page", NamedTextColor.GREEN))) {
                        EnchantmentTableMenu.displayNextSelectedEnchantmentOptions(player, item, clickedInventory, slot);
                    }
                }
                event.setCancelled(true);
            } else if(slot == PREVIOUS_PAGE) {
                ItemStack ptr = clickedInventory.getItem(slot);
                ItemMeta ptrMeta = ptr.getItemMeta();
                if(ptrMeta != null) {
                    if(ptrMeta.displayName().equals(Component.text("Previous Page", NamedTextColor.AQUA))) {
                        EnchantmentTableMenu.displayPreviousEnchantmentOptionsPage(clickedInventory, item);
                    }
                    else if(ptrMeta.displayName().equals(Component.text("Previous Page", NamedTextColor.GREEN))) {
                        EnchantmentTableMenu.displayPreviousSelectedEnchantmentOptions(player, item, clickedInventory, slot);
                    }
                }
                event.setCancelled(true);
            } else if(options.contains(slot)) {
                if(itemClicked != null && itemClicked.getType() != Material.GLASS_PANE) {
                    EnchantmentTableMenu.displaySelectedEnchantmentOptions(player, item, clickedInventory, slot);
                }
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory closedInventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        // Check if the closed inventory is the custom enchantment table GUI
        if (event.getView().title().equals(Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE))) {
            ItemStack itemInEnchantSlot = closedInventory.getItem(ITEM_SLOT);

            // If there is an item in the enchantment slot, return it to the player's inventory
            if (itemInEnchantSlot != null && itemInEnchantSlot.getType() != Material.AIR) {
                Inventory playerInventory = player.getInventory();

                // Try to add the item back to the player's inventory
                HashMap<Integer, ItemStack> unaddedItems = playerInventory.addItem(itemInEnchantSlot);

                // If the player's inventory is full and the item cannot be added back, drop it in the world at the player's location
                if (!unaddedItems.isEmpty()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemInEnchantSlot);
                }

                // Clear the slot in the custom GUI to prevent duplication
                closedInventory.setItem(ITEM_SLOT, new ItemStack(Material.AIR));
            }
        }
    }

    private void wait(Inventory inv) {
        new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack itemInTargetSlot = inv.getItem(ITEM_SLOT);
                    if (itemInTargetSlot != null && itemInTargetSlot.getType() != Material.AIR) {
                        // Item detected in the target slot, update the UI accordingly
                        EnchantmentTableMenu.displayEnchantmentOptions(inv, itemInTargetSlot);
                        cancel(); // Stop this task from running again
                    }
                }
        }.runTaskTimer(plugin, 0L, 10L); // Run this task every half second (10 ticks)
    }
}
