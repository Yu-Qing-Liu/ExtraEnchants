package com.github.yuqingliu.extraenchants.enchants.tools;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Ageable;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Delicate implements Listener {
    private final Enchantment enchant;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (enchant.getEnchantmentLevel(tool) > 0) {
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


