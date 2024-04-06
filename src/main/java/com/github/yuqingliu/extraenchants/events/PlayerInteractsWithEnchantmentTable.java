package com.github.yuqingliu.extraenchants.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.Bukkit;

import com.github.yuqingliu.extraenchants.gui.EnchantmentTableMenu;
import com.github.yuqingliu.extraenchants.gui.CustomEnchantmentTableMenu;

import java.util.List;
import java.util.Arrays;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerInteractsWithEnchantmentTable implements Listener {
    private JavaPlugin plugin;

    public PlayerInteractsWithEnchantmentTable(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block != null && block.getType() == Material.ENCHANTING_TABLE) {
            event.setCancelled(true); // Prevent the default enchantment table GUI from opening

            int bookshelves = countEffectiveBookshelves(block);
            EnchantmentTableMenu.openEnchantmentTableMenu(player, bookshelves);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked(); // Cast to Player
        Inventory clickedInventory = event.getClickedInventory(); // The inventory that was clicked in
        if (clickedInventory == null) {
            return;
        }

        if (event.getView().title().equals(Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE))) {
            int slot = event.getRawSlot(); // Use getRawSlot() for the actual slot clicked
            
            boolean updateGUI = false;
            // Check if the action involves slot 16 or is a drag that could affect it
            if (slot == 16 || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                updateGUI = true;
            } else if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR && slot == 16) {
                updateGUI = true;
            }

            // If the item slot is involved, update the GUI accordingly
            if (updateGUI) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> EnchantmentTableMenu.updateEnchantmentTableMenu(player), 2L);
            }

            // Distinguish between the custom GUI (top inventory) and the player inventory (bottom inventory)
            if (clickedInventory.equals(player.getOpenInventory().getTopInventory())) {
                // Clicks in the custom GUI
                List<Integer> allowedSlots = Arrays.asList(10, 11, 12, 13, 14, 16, 19, 20, 21, 22, 23, 37, 38, 39, 40, 41, 43, 46, 47, 48, 49, 50); // List allowed slots
                List<Integer> confirmSlots = Arrays.asList(10, 11, 12, 13, 14, 19, 20, 21, 22, 23, 37, 38, 39, 40, 41, 46, 47, 48, 49, 50); // List confirm slots

                if (!allowedSlots.contains(slot)) {
                    event.setCancelled(true); // Cancel event if slot is not allowed
                } else {
                    if(confirmSlots.contains(slot)) {
                        ItemStack item = clickedInventory.getItem(16);
                        if(item == null) {
                            event.setCancelled(true);
                            return;
                        }
                        switch (slot) {
                            case 10:
                                EnchantmentTableMenu.applyEnchants(1, item, player);
                                break;
                            case 11:
                                EnchantmentTableMenu.applyEnchants(2, item, player);
                                break;
                            case 12:
                                EnchantmentTableMenu.applyEnchants(3, item, player);
                                break;
                            case 13:
                                EnchantmentTableMenu.applyEnchants(4, item, player);
                                break;
                            case 14:
                                EnchantmentTableMenu.applyEnchants(5, item, player);
                                break;
                            case 19:
                                EnchantmentTableMenu.applyEnchants(1, item, player);
                                break;
                            case 20:
                                EnchantmentTableMenu.applyEnchants(2, item, player);
                                break;
                            case 21:
                                EnchantmentTableMenu.applyEnchants(3, item, player);
                                break;
                            case 22:
                                EnchantmentTableMenu.applyEnchants(4, item, player);
                                break;
                            case 23:
                                EnchantmentTableMenu.applyEnchants(5, item, player);
                                break;
                            case 37:
                                CustomEnchantmentTableMenu.applyEnchants(1, item, player);
                                break;
                            case 38:
                                CustomEnchantmentTableMenu.applyEnchants(2, item, player);
                                break;
                            case 39:
                                CustomEnchantmentTableMenu.applyEnchants(3, item, player);
                                break;
                            case 40:
                                CustomEnchantmentTableMenu.applyEnchants(4, item, player);
                                break;
                            case 41:
                                CustomEnchantmentTableMenu.applyEnchants(5, item, player);
                                break;
                            case 46:
                                CustomEnchantmentTableMenu.applyEnchants(1, item, player);
                                break;
                            case 47:
                                CustomEnchantmentTableMenu.applyEnchants(2, item, player);
                                break;
                            case 48:
                                CustomEnchantmentTableMenu.applyEnchants(3, item, player);
                                break;
                            case 49:
                                CustomEnchantmentTableMenu.applyEnchants(4, item, player);
                                break;
                            case 50:
                                CustomEnchantmentTableMenu.applyEnchants(5, item, player);
                                break;

                            default:
                                break;
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public int countEffectiveBookshelves(Block enchantingTableBlock) {
        int count = 0;
        // Coordinates relative to the enchanting table for all 24 valid bookshelf positions
        int[][] relativePositions = {
            {-1, 0, 2}, {0, 0, 2}, {1, 0, 2},
            {2, 0, 1}, {2, 0, 0}, {2, 0, -1},
            {1, 0, -2}, {0, 0, -2}, {-1, 0, -2},
            {-2, 0, -1}, {-2, 0, 0}, {-2, 0, 1},
            {-1, 1, 2}, {0, 1, 2}, {1, 1, 2},
            {2, 1, 1}, {2, 1, 0}, {2, 1, -1},
            {1, 1, -2}, {0, 1, -2}, {-1, 1, -2},
            {-2, 1, -1}, {-2, 1, 0}, {-2, 1, 1}
        };

        for (int[] pos : relativePositions) {
            Block checkBlock = enchantingTableBlock.getRelative(pos[0], pos[1], pos[2]);
            if (checkBlock.getType() == Material.BOOKSHELF) {
                Block airCheck1 = enchantingTableBlock.getRelative(pos[0], 1, pos[2]); // Directly above enchanting table
                Block airCheck2 = enchantingTableBlock.getRelative(pos[0], 2, pos[2]); // One above the bookshelf level
                if ((airCheck1.getType() == Material.AIR || airCheck1.getType() == Material.BOOKSHELF) && 
                    airCheck2.getType() == Material.AIR) {
                    count++;
                }
            }
        }
        if(count == 0) return 1;
        return count;
    }
}
