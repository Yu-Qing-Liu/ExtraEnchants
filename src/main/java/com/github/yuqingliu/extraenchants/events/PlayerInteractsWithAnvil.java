package com.github.yuqingliu.extraenchants.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.gui.AnvilMenu;
import com.github.yuqingliu.extraenchants.persistence.blocks.CustomBlockUtils;
import com.github.yuqingliu.extraenchants.ExtraEnchantsImpl;
import com.github.yuqingliu.extraenchants.configuration.implementations.AnvilConstants;
import com.github.yuqingliu.extraenchants.configuration.implementations.GlobalConstants;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerInteractsWithAnvil implements Listener {
    private ExtraEnchantsImpl plugin;
    private GlobalConstants globalConstants;
    private AnvilMenu anvilMenu;
    private static final int LEFT_SLOT = 11;
    private static final int RIGHT_SLOT = 13;
    private static final int RESULT_SLOT = 15;
    private static final List<Integer> allowed = Arrays.asList(11,13);
    private static final List<Integer> frame = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,12,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35);
    private static boolean hasResult = false;

    public PlayerInteractsWithAnvil(ExtraEnchantsImpl plugin) {
        this.plugin = plugin;
        this.globalConstants = (GlobalConstants) plugin.getConfigurationManager().getConstants().get("GlobalConstants");
        AnvilConstants anvilConstants = (AnvilConstants) plugin.getConfigurationManager().getConstants().get("AnvilConstants");
        this.anvilMenu = new AnvilMenu(plugin.getItemUtils(), plugin.getAnvilManager(), anvilConstants);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        // Player interacts with an anvil
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.ANVIL) {
            if(!globalConstants.getApplyVanillaAnvilBehavior()) {
                event.setCancelled(true);
                anvilMenu.openAnvilMenu(player);
            } else if(CustomBlockUtils.isCustomAnvil(block)) {
                event.setCancelled(true);
                anvilMenu.openAnvilMenu(player);
            }
            // Normal behavior
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().title().equals(Component.text("Anvil", NamedTextColor.DARK_GRAY))) {
            wait(event.getInventory(), player);
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
                if(frame.contains(event.getRawSlot())) {
                    event.setCancelled(true);
                    return;
                }
                if(allowed.contains(event.getRawSlot())) {
                    return;
                }
                else if (itemInLeftSlot == null || itemInLeftSlot.getType() == Material.AIR)  {
                    // If the ITEM_SLOT is empty, move the current item there
                    actionInventory.setItem(LEFT_SLOT, currentItem.clone());
                    clickedInventory.clear(event.getSlot()); // Clear the slot from where the item was moved
                } else if (itemInRightSlot == null || itemInRightSlot.getType() == Material.AIR && itemInLeftSlot != null) {
                    // If the ITEM_SLOT is empty, move the current item there
                    actionInventory.setItem(RIGHT_SLOT, currentItem.clone());
                    clickedInventory.clear(event.getSlot()); // Clear the slot from where the item was moved
                } 
                event.setCancelled(true);
            } else if(validResult(clickedInventory.getItem(RESULT_SLOT))) {
                // Shift-click logic
                if (event.getSlot() == RESULT_SLOT) {
                    // The slot clicked is the result slot, and it was a shift-click
                    if (validResult(clickedInventory.getItem(RESULT_SLOT))) {
                        if (anvilMenu.applyCost(player)) {
                            clickedInventory.setItem(LEFT_SLOT, new ItemStack(Material.AIR));
                            clickedInventory.setItem(RIGHT_SLOT, new ItemStack(Material.AIR));
                        } else {
                            event.setCancelled(true);
                        }
                        wait(clickedInventory, player);
                    }
                } else {
                    setBarrier(clickedInventory);
                    wait(clickedInventory, player);
                    event.setCancelled(true);
                }
            } else {
                // Player is removing an item from left or right, cancel the transaction
                setBarrier(clickedInventory);
                wait(clickedInventory, player);
                event.setCancelled(true);
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
                    setBarrier(clickedInventory);
                    wait(clickedInventory, player);
                } else if(slot == RESULT_SLOT && validResult(clickedInventory.getItem(slot))) {
                    if(anvilMenu.applyCost(player)) {
                        clickedInventory.setItem(LEFT_SLOT, new ItemStack(Material.AIR));
                        clickedInventory.setItem(RIGHT_SLOT, new ItemStack(Material.AIR));
                    } else {
                        event.setCancelled(true);
                    }
                    wait(clickedInventory, player);
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

    private void setBarrier(Inventory inv) {
        ItemStack resultPlaceholder = new ItemStack(Material.BARRIER);
        ItemMeta resultPlaceholderMeta = resultPlaceholder.getItemMeta();
        if (resultPlaceholderMeta != null) {
            resultPlaceholderMeta.displayName(Component.text("Unavailable", NamedTextColor.RED));
            resultPlaceholder.setItemMeta(resultPlaceholderMeta);
        }
        inv.setItem(RESULT_SLOT, resultPlaceholder);
    }

    private void wait(Inventory inv, Player player) {
        hasResult = false;
        new BukkitRunnable() {
            @Override
            public void run() {
                setBarrier(inv);
                ItemStack leftItem = inv.getItem(LEFT_SLOT);
                ItemStack rightItem = inv.getItem(RIGHT_SLOT);
                if (leftItem != null && rightItem != null && leftItem.getType() != Material.AIR && rightItem.getType() != Material.AIR) {
                    // Item detected in both left and right slots, update the result
                    anvilMenu.updateResult(plugin, player, inv, leftItem, rightItem, false);
                    hasResult = true;
                    cancel(); // Stop this task from running again
                }
            }
        }.runTaskTimer(plugin, 0L, 10L); // Run this task every half second (10 ticks)
    }
}
