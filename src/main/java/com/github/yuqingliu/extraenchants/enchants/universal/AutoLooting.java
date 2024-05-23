package com.github.yuqingliu.extraenchants.enchants.universal;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutoLooting implements Listener {
    private final Enchantment autoLooting;
    private final Enchantment smelting;
    private final Enchantment replant;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        if (autoLooting.getEnchantmentLevel(tool) > 0 &&
            smelting.getEnchantmentLevel(tool) == 0 &&
            replant.getEnchantmentLevel(tool) == 0 && 
            tool.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.SILK_TOUCH) == 0) {
            autoloot(event, player, block, tool);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        if (killer != null) {
            ItemStack weapon = killer.getInventory().getItemInMainHand();
            if (autoLooting.getEnchantmentLevel(weapon) > 0) {
                for (ItemStack drop : event.getDrops()) {
                    killer.getInventory().addItem(drop);
                }
                event.getDrops().clear();
            }
        }
    }

    public static void autoloot(BlockBreakEvent event, Player player, Block block, ItemStack tool) {
        for (ItemStack drop : block.getDrops(tool)) {
            event.setDropItems(false);
            player.getInventory().addItem(drop);
        }
    }
}


