package com.github.yuqingliu.extraenchants.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.github.yuqingliu.extraenchants.Keys;
import com.github.yuqingliu.extraenchants.persistence.blocks.*;

public class PlayerPlacesDestroysGrindstone implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block placedBlock = event.getBlockPlaced();
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        // Check if the placed block is an anvil
        if (placedBlock.getType() == Material.GRINDSTONE) {
            // Check if the ItemStack has the custom anvil NBT tag key
            if (hasCustomGrindstoneTag(itemInHand)) {
                // Store block data in block database
                CustomBlockUtils.addCustomBlock(placedBlock, "custom-grindstone");
            }
            // Otherwise, normal behavior
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block breakedBlock = event.getBlock();
        if(breakedBlock.getType() == Material.GRINDSTONE) {
            CustomBlockUtils.deleteCustomBlock(breakedBlock, "custom-grindstone");
        }
    }

    private boolean hasCustomGrindstoneTag(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() != Material.GRINDSTONE) {
            return false;
        }
        // Check if the ItemStack has the custom anvil NBT tag key
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(Keys.getCustomUIBlock(), PersistentDataType.BOOLEAN);
    }
}
