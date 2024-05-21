package com.github.yuqingliu.extraenchants.enchants.tools;

import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.HashMap;

import lombok.RequiredArgsConstructor;

import com.github.yuqingliu.extraenchants.enchantment.Enchantment;

@RequiredArgsConstructor
public class SilkTouch implements Listener {
    private final Enchantment silkTouch;
    private final Enchantment autoLooting;
    private final Enchantment smelting;
    private final Enchantment replant;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();
        Material blockType = block.getType();

        if (silkTouch.getEnchantmentLevel(tool) > 0 && !isDelicate(blockType)) {
            // Silk Touch is on the tool and the block is not delicate
            ItemStack clonedTool = cloneToolWithoutSilkTouch(tool);
            // Prevent Silk Touch from applying its effect
            // Simulate drops as if Silk Touch were not applied using the cloned tool
            if(autoLooting.getEnchantmentLevel(tool) == 0 && 
               smelting.getEnchantmentLevel(tool) == 0 &&
               replant.getEnchantmentLevel(tool) == 0){
                event.setDropItems(false);
                Collection<ItemStack> drops = block.getDrops(clonedTool);
                drops.forEach(drop -> block.getWorld().dropItemNaturally(block.getLocation(), drop));
            } else if(smelting.getEnchantmentLevel(tool) == 0 &&
                      replant.getEnchantmentLevel(tool) == 0) {
                event.setDropItems(false);
                // AutoLooting is active, move items directly to the player's inventory
                Collection<ItemStack> drops = block.getDrops(clonedTool);
                HashMap<Integer, ItemStack> unadded = player.getInventory().addItem(drops.toArray(new ItemStack[0]));
                
                // Check if there are any items that couldn't be added to the inventory
                if (!unadded.isEmpty()) {
                    // Drop any overflow items naturally at the block location
                    unadded.values().forEach(item -> block.getWorld().dropItemNaturally(block.getLocation(), item));
                }
            } else if(smelting.getEnchantmentLevel(tool) > 0) {
                Smelting Smelting = new Smelting(smelting, autoLooting);
                Smelting.smelt(event, player, clonedTool, block, blockType);
            } else if(replant.getEnchantmentLevel(tool) > 0) {
                Replant Replant = new Replant(replant, autoLooting);
                Replant.plant(event, player, clonedTool, block);
            }
        } 
        // If the block is delicate or the tool does not have Silk Touch, let normal processing occur
    }

    private boolean isDelicate(Material blockType) {
        // Define delicate blocks where Silk Touch is allowed
        String typeName = blockType.name();
        // Check for any kind of glass or glass pane using string matching
        if (typeName.contains("GLASS") || typeName.endsWith("PANE")) {
            return true;
        }

        switch (blockType) {
            case ICE:
            case PACKED_ICE:
            case BLUE_ICE:
            case SPAWNER:
            case AMETHYST_CLUSTER:
            case BEE_NEST:
            case BOOKSHELF:
            case BUDDING_AMETHYST:
            case CAKE:
            case CAMPFIRE:
            case CHISELED_BOOKSHELF:
            case ENDER_CHEST:
            case GLOWSTONE:
            case SCULK:
            case SCULK_VEIN:
            case SCULK_SENSOR:
            case SCULK_CATALYST:
            case SCULK_SHRIEKER:
            case CALIBRATED_SCULK_SENSOR:
            case SEA_LANTERN:
            case SNOW:
            case SNOW_BLOCK:
            case SOUL_CAMPFIRE:
            case TURTLE_EGG:
            case TWISTING_VINES:
            case WEEPING_VINES:
                return true;
            default:
                return false;
        }
    }

    private ItemStack cloneToolWithoutSilkTouch(ItemStack tool) {
        ItemStack clone = tool.clone();
        ItemMeta meta = clone.getItemMeta(); // Get the meta data of the cloned item

        if (meta != null && meta.hasEnchant(org.bukkit.enchantments.Enchantment.SILK_TOUCH)) {
            meta.removeEnchant(org.bukkit.enchantments.Enchantment.SILK_TOUCH); // Remove the Silk Touch enchantment
            clone.setItemMeta(meta); // Set the modified meta back to the cloned item
        } 

        return clone;
    }
}



