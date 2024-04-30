package com.github.yuqingliu.extraenchants.enchants.tools;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Ageable;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;
import com.github.yuqingliu.extraenchants.enchants.ApplicableItemsRegistry;

public class Delicate implements Listener {
    private final JavaPlugin plugin;

    public Delicate (JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (ApplicableItemsRegistry.hoe_applicable.contains(tool.getType()) &&
            UtilityMethods.getEnchantmentLevel(tool, "Delicate") > 0) {
            Block block = event.getBlock();
            BlockData data = block.getBlockData();

            if (data instanceof Ageable) {
                Ageable ageable = (Ageable) data;
                if (ageable.getAge() < ageable.getMaximumAge()) {
                    event.setCancelled(true); // Cancel the event to prevent breaking immature plants
                }
            }
        }
    }
}


