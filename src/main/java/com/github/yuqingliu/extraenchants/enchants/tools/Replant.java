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
import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;

public class Replant implements Listener {
    private final JavaPlugin plugin;

    public Replant(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool.getType().toString().endsWith("_HOE") && UtilityMethods.getEnchantmentLevel(tool, "Replant") > 0) {
            Block block = event.getBlock();
            Material cropType = block.getType();
            Material seed = getSeedForCrop(cropType);
            
            if (seed != null && block.getBlockData() instanceof Ageable) {
                Ageable age = (Ageable) block.getBlockData();
                if (age.getAge() == age.getMaximumAge()) {
                    event.setCancelled(true);
                    ItemStack crop = getCrop(tool, cropType);
                    block.getWorld().dropItemNaturally(block.getLocation(), crop);
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

    private ItemStack getCrop(ItemStack tool, Material cropType) {
        int dropCount = UtilityMethods.RandomIntBetween(1,2);
        // Check for Fortune enchantment on the tool
        if (tool.hasItemMeta()) {
            ItemMeta meta = tool.getItemMeta();
            if (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                int fortuneLevel = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
                // Adjust drop count based on fortune level (example calculation, adjust as needed)
                dropCount += UtilityMethods.RandomIntBetween(0, fortuneLevel);
            }
        }

        switch (cropType) {
            case WHEAT:
                return new ItemStack(Material.WHEAT, dropCount);
            case CARROTS:
                return new ItemStack(Material.CARROT, dropCount);
            case POTATOES:
                return new ItemStack(Material.POTATO, dropCount);
            case NETHER_WART:
                return new ItemStack(Material.NETHER_WART, dropCount);
            case BEETROOTS:
                return new ItemStack(Material.BEETROOT, dropCount);
            default:
                return null;
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
            case BEETROOTS:
                return Material.BEETROOT_SEEDS;
            default:
                return null;
        }
    }
}

