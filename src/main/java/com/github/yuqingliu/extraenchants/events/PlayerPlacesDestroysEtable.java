package com.github.yuqingliu.extraenchants.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.github.yuqingliu.extraenchants.blocks.*;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class PlayerPlacesDestroysEtable implements Listener {
    private JavaPlugin plugin;

    public PlayerPlacesDestroysEtable(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block placedBlock = event.getBlockPlaced();
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        // Check if the placed block is an etable
        if (placedBlock.getType() == Material.ENCHANTING_TABLE) {
            // Check if the ItemStack has the custom anvil NBT tag key
            if (hasCustomEtableTag(itemInHand)) {
                // Store block data in block database
                CustomBlockUtils.addCustomBlock(placedBlock, "custom-etable");
            }
            // Otherwise, normal behavior
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block breakedBlock = event.getBlock();
        if(breakedBlock.getType() == Material.ENCHANTING_TABLE) {
            CustomBlockUtils.deleteCustomBlock(breakedBlock, "custom-etable");
        }
    }

    private boolean hasCustomEtableTag(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() != Material.ENCHANTING_TABLE) {
            return false;
        }
        // Check if the ItemStack has the custom etable NBT tag key
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasTag("extra-enchants-custom-etable");
    }
}
