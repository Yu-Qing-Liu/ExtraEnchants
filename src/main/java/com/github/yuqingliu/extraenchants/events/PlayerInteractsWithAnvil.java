package com.github.yuqingliu.extraenchants.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.NamespacedKey;

import com.github.yuqingliu.extraenchants.gui.AnvilMenu;
import com.github.yuqingliu.extraenchants.utils.*;
import com.github.yuqingliu.extraenchants.database.Constants;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerInteractsWithAnvil implements Listener {
    private JavaPlugin plugin;
    private static int IRON_BLOCKS = 1;
    private static final int LEFT_SLOT = 11;
    private static final int RIGHT_SLOT = 13;
    private static final int RESULT_SLOT = 15;
    private static final List<Integer> allowed = Arrays.asList(21,23);
    private static boolean hasResult = false;

    public PlayerInteractsWithAnvil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        // Check if event is a right-click on a block
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.ANVIL) {
            event.setCancelled(true); // Prevent the default enchantment table GUI from opening
            
            IRON_BLOCKS = UtilityMethods.countSurroundingEffectiveBlocks(block, Material.IRON_BLOCK);
            AnvilMenu.openAnvilMenu(player, IRON_BLOCKS);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getView().title().equals(Component.text("Anvil", NamedTextColor.DARK_GRAY))) {
            wait(event.getInventory());
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        Inventory actionInventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();

        if (clickedInventory == null || currentItem == null || !event.getView().title().equals(Component.text("Anvil", NamedTextColor.DARK_GRAY))) {
            return;
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            // Handle the shift-click case
            ItemStack itemInLeftSlot = actionInventory.getItem(LEFT_SLOT);
            ItemStack itemInRightSlot = actionInventory.getItem(RIGHT_SLOT);

            if(!hasResult) {
                if (itemInLeftSlot == null || itemInLeftSlot.getType() == Material.AIR) {
                    // If the ITEM_SLOT is empty, move the current item there
                    actionInventory.setItem(LEFT_SLOT, currentItem.clone());
                    clickedInventory.clear(event.getSlot()); // Clear the slot from where the item was moved
                    event.setCancelled(true); // Prevent default behavior
                } else if (itemInRightSlot == null || itemInRightSlot.getType() == Material.AIR) {
                    // If the ITEM_SLOT is empty, move the current item there
                    actionInventory.setItem(RIGHT_SLOT, currentItem.clone());
                    clickedInventory.clear(event.getSlot()); // Clear the slot from where the item was moved
                    event.setCancelled(true); // Prevent default behavior
                }
            } else {
                clickedInventory.setItem(LEFT_SLOT, new ItemStack(Material.AIR));
                clickedInventory.setItem(RIGHT_SLOT, new ItemStack(Material.AIR));
            }
            return;
        }

        if (clickedInventory.equals(player.getOpenInventory().getTopInventory())) {
            int slot = event.getSlot();
            if(!hasResult) {
                if (!allowed.contains(slot)) {
                    event.setCancelled(true);
                    return;
                }
            } else {
                // If player removes item from left or right slot, cancel the transaction
                if(slot == RIGHT_SLOT || slot == LEFT_SLOT) {
                    wait(clickedInventory);
                } else if(slot == RESULT_SLOT && validResult(clickedInventory.getItem(slot))) {
                    clickedInventory.setItem(LEFT_SLOT, new ItemStack(Material.AIR));
                    clickedInventory.setItem(RIGHT_SLOT, new ItemStack(Material.AIR));
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory closedInventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        // Check if the closed inventory is the custom enchantment table GUI
        if (event.getView().title().equals(Component.text("Anvil", NamedTextColor.DARK_GRAY))) {
            ItemStack leftItem = closedInventory.getItem(LEFT_SLOT);
            ItemStack rightItem = closedInventory.getItem(RIGHT_SLOT);

            // If there is an item in the left slot, return it to the player's inventory
            if (leftItem != null && leftItem.getType() != Material.AIR) {
                Inventory playerInventory = player.getInventory();

                // Try to add the item back to the player's inventory
                HashMap<Integer, ItemStack> unaddedItems = playerInventory.addItem(leftItem);

                // If the player's inventory is full and the item cannot be added back, drop it in the world at the player's location
                if (!unaddedItems.isEmpty()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), leftItem);
                }

                // Clear the slot in the custom GUI to prevent duplication
                closedInventory.setItem(LEFT_SLOT, new ItemStack(Material.AIR));
            }

            // If there is an item in the right slot, return it to the player's inventory
            if (rightItem != null && rightItem.getType() != Material.AIR) {
                Inventory playerInventory = player.getInventory();

                // Try to add the item back to the player's inventory
                HashMap<Integer, ItemStack> unaddedItems = playerInventory.addItem(rightItem);

                // If the player's inventory is full and the item cannot be added back, drop it in the world at the player's location
                if (!unaddedItems.isEmpty()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), rightItem);
                }

                // Clear the slot in the custom GUI to prevent duplication
                closedInventory.setItem(RIGHT_SLOT, new ItemStack(Material.AIR));
            }

        }
    }

    private boolean validResult(ItemStack item) {
        return item != null && item.getType() != Material.BARRIER && item.getType() != Material.AIR;
    }

    private void wait(Inventory inv) {
        hasResult = false;
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack leftItem = inv.getItem(LEFT_SLOT);
                ItemStack rightItem = inv.getItem(LEFT_SLOT);
                if (leftItem != null && rightItem != null && leftItem.getType() != Material.AIR && rightItem.getType() != Material.AIR) {
                    // Item detected in both left and right slots, update the result
                    AnvilMenu.updateResult(leftItem, rightItem);
                    hasResult = true;
                    cancel(); // Stop this task from running again
                }
            }
        }.runTaskTimer(plugin, 0L, 10L); // Run this task every half second (10 ticks)
    }
}
