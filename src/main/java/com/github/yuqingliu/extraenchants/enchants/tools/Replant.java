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

import com.github.yuqingliu.extraenchants.utils.UtilityMethods;

public class Replant implements Listener {
    
    private final JavaPlugin plugin;

    public Replant(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool.getType().toString().endsWith("_HOE") && UtilityMethods.hasEnchantment(tool, "Replant", 1)) {
            Block block = event.getBlock();
            Material cropType = block.getType();
            Material seed = getSeedForCrop(cropType);
            
            if (seed != null && block.getBlockData() instanceof Ageable) {
                Ageable age = (Ageable) block.getBlockData();
                if (age.getAge() == age.getMaximumAge()) {
                    event.setCancelled(true);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(cropType, 1));
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
    
    private Material getSeedForCrop(Material cropType) {
        switch (cropType) {
            case WHEAT:
                return Material.WHEAT_SEEDS;
            case CARROTS:
                return Material.CARROT;
            case POTATOES:
                return Material.POTATO;
            case NETHER_WART:
                return Material.NETHER_WART;
            case BEETROOT:
                return Material.BEETROOT_SEEDS;
            default:
                return null;
        }
    }
}

