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
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

import com.github.yuqingliu.extraenchants.enchants.universal.AutoLooting;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Replant implements Listener {
    private final Enchantment replant;
    private final Enchantment autoLooting;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (replant.getEnchantmentLevel(tool) > 0 && 
            tool.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.SILK_TOUCH) == 0) {
            Block block = event.getBlock();
            plant(event, player, tool, block);
        }
    }

    public void plant(BlockBreakEvent event, Player player, ItemStack tool, Block block) {
        if (block.getBlockData() instanceof Ageable) {
            Ageable age = (Ageable) block.getBlockData();
            if (age.getAge() == age.getMaximumAge()) {
                event.setCancelled(true);
                
                // Calculating the total crop drops considering Fortune
                Collection<ItemStack> drops = block.getDrops(tool);
                int totalCropCount = drops.stream().mapToInt(ItemStack::getAmount).sum();
                
                // Manually drop each crop and replant seed
                if (totalCropCount > 0) {
                    if(autoLooting.getEnchantmentLevel(tool) == 0) {
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
        } else {
            if(autoLooting.getEnchantmentLevel(tool) > 0) {
                AutoLooting.autoloot(event, player, block, tool);
            }
            // Noraml behavior
        }
    }
}

