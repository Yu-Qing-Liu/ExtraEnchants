package com.github.yuqingliu.extraenchants.enchants.tools;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;
import com.github.yuqingliu.extraenchants.enchants.ApplicableItemsRegistry;

public class Replant implements Listener {
    private final JavaPlugin plugin;

    public Replant(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Material toolType = tool.getType();

        if (ApplicableItemsRegistry.hoe_applicable.contains(toolType) && UtilityMethods.getEnchantmentLevel(tool, "Replant") > 0) {
            Block block = event.getBlock();
            Material cropType = block.getType();

            if (block.getBlockData() instanceof Ageable) {
                Ageable age = (Ageable) block.getBlockData();
                if (age.getAge() == age.getMaximumAge()) {
                    event.setCancelled(true);
                    
                    // Calculating the total crop drops considering Fortune
                    Collection<ItemStack> drops = block.getDrops(tool);
                    int totalCropCount = drops.stream().mapToInt(ItemStack::getAmount).sum();
                    
                    // Manually drop each crop and replant seed
                    if (totalCropCount > 0) {
                        if(UtilityMethods.getEnchantmentLevel(tool, "AutoLooting") == 0) {
                            for(ItemStack drop : drops) {
                                block.getWorld().dropItemNaturally(block.getLocation(), drop);
                            }
                        } else {
                            // If AutoLooting is active, add the drops directly to the player's inventory.
                            for (ItemStack drop : drops) {
                                HashMap<Integer, ItemStack> unadded = player.getInventory().addItem(drop);

                                // Check for any items that couldn't be added to the inventory (i.e., inventory was full)
                                if (!unadded.isEmpty()) {
                                    // Drop any overflow items naturally at the block location.
                                    for (ItemStack remaining : unadded.values()) {
                                        block.getWorld().dropItemNaturally(block.getLocation(), remaining);
                                    }
                                }
                            }
                        }
                    }
                    
                    age.setAge(0);
                    block.setBlockData(age);

                    // Check and apply damage using Damageable interface
                    if(tool.getItemMeta() instanceof Damageable) {
                        Damageable damageable = (Damageable) tool.getItemMeta();
                        if(damageable.getDamage() < tool.getType().getMaxDurability() - 1) {
                            damageable.setDamage(damageable.getDamage() + 1);
                            tool.setItemMeta((ItemMeta) damageable);
                        }
                    }
                }
            }
        }
    }
}

