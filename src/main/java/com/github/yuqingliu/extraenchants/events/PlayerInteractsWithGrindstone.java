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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.gui.GrindstoneMenu;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerInteractsWithGrindstone implements Listener {
    private JavaPlugin plugin;
    private static final int ITEM_SLOT = 25;
    private static final int PREVIOUS_PAGE = 6;
    private static final int NEXT_PAGE = 51;
    List<Integer> frame = Arrays.asList(7,8,15,16,17,24,26,33,35,42,43,44,52,53);
    List<Integer> options = Arrays.asList(0,1,2,3,4,5,9,10,11,12,13,14,18,19,20,21,22,23,27,28,29,30,31,32,36,37,38,39,40,41,45,46,47,48,49,50);

    public PlayerInteractsWithGrindstone(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        // Check if the event is a right-click on a block
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.GRINDSTONE) {
            event.setCancelled(true); // Prevent the default enchantment table GUI from opening
            GrindstoneMenu.openGrindstoneMenu(player);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getView().title().equals(Component.text("Grindstone", NamedTextColor.GOLD))) {
            wait(event.getInventory());
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        Inventory actionInventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();

        if (clickedInventory == null || currentItem == null || !event.getView().title().equals(Component.text("Grindstone", NamedTextColor.GOLD))) {
            return;
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            // Handle the shift-click case
            ItemStack itemInTargetSlot = actionInventory.getItem(ITEM_SLOT);
            if (itemInTargetSlot == null || itemInTargetSlot.getType() == Material.AIR) {
                // If the ITEM_SLOT is empty, move the current item there
                actionInventory.setItem(ITEM_SLOT, currentItem.clone());
                clickedInventory.clear(event.getSlot()); // Clear the slot from where the item was moved
                event.setCancelled(true); // Prevent default behavior
                GrindstoneMenu.displayOptions(actionInventory, currentItem);
            } else {
                GrindstoneMenu.clearOptions(actionInventory);
                GrindstoneMenu.optionsFill(actionInventory);
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
                GrindstoneMenu.clearOptions(actionInventory);
                GrindstoneMenu.optionsFill(actionInventory);
                wait(clickedInventory);
                return;
            } else if(slot == NEXT_PAGE) {
                ItemStack ptr = clickedInventory.getItem(slot);
                ItemMeta ptrMeta = ptr.getItemMeta();
                if(ptrMeta != null) {
                    GrindstoneMenu.displayNextOptionsPage(clickedInventory, item);
                }
                event.setCancelled(true);
            } else if(slot == PREVIOUS_PAGE) {
                ItemStack ptr = clickedInventory.getItem(slot);
                ItemMeta ptrMeta = ptr.getItemMeta();
                if(ptrMeta != null) {
                    GrindstoneMenu.displayPreviousOptionsPage(clickedInventory, item);
                }
                event.setCancelled(true);
            } else if(options.contains(slot)) {
                if(itemClicked != null && itemClicked.getType() != Material.GLASS_PANE) {
                    GrindstoneMenu.applyOption(player, clickedInventory, slot, item);
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
        if (event.getView().title().equals(Component.text("Grindstone", NamedTextColor.GOLD))) {
            ItemStack item = closedInventory.getItem(ITEM_SLOT);

            // If there is an item in the enchantment slot, return it to the player's inventory
            if (item != null && item.getType() != Material.AIR) {
                Inventory playerInventory = player.getInventory();

                // Try to add the item back to the player's inventory
                HashMap<Integer, ItemStack> unaddedItems = playerInventory.addItem(item);

                // If the player's inventory is full and the item cannot be added back, drop it in the world at the player's location
                if (!unaddedItems.isEmpty()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
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
                        GrindstoneMenu.displayOptions(inv, itemInTargetSlot);
                        cancel(); // Stop this task from running again
                    }
                }
        }.runTaskTimer(plugin, 0L, 10L); // Run this task every half second (10 ticks)
    }
}
