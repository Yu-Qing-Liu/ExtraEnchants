package com.github.yuqingliu.extraenchants.enchants.universal;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;
import com.github.yuqingliu.extraenchants.enchants.ApplicableItemsRegistry;

public class AutoLooting implements Listener {
    private final JavaPlugin plugin;

    public AutoLooting(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        if (ApplicableItemsRegistry.tools_weapons_applicable.contains(tool.getType()) &&
            UtilityMethods.getEnchantmentLevel(tool, "AutoLooting") > 0 &&
            UtilityMethods.getEnchantmentLevel(tool, "Smelting") == 0 &&
            UtilityMethods.getEnchantmentLevel(tool, "Replant") == 0 && 
            !UtilityMethods.hasVanillaEnchantment(tool, Enchantment.SILK_TOUCH)) {
            event.setDropItems(false);
            autoloot(event, player, block, tool);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        if (killer != null) {
            ItemStack weapon = killer.getInventory().getItemInMainHand();
            if (ApplicableItemsRegistry.tools_weapons_applicable.contains(weapon.getType()) &&
                UtilityMethods.getEnchantmentLevel(weapon, "AutoLooting") > 0) {
                for (ItemStack drop : event.getDrops()) {
                    killer.getInventory().addItem(drop);
                }
                event.getDrops().clear();
            }
        }
    }

    public static void autoloot(BlockBreakEvent event, Player player, Block block, ItemStack tool) {
        for (ItemStack drop : block.getDrops(tool)) {
            player.getInventory().addItem(drop);
        }
    }
}


