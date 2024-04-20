package com.github.yuqingliu.extraenchants.enchants.tools;

import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Collection;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;
import com.github.yuqingliu.extraenchants.enchants.ApplicableItemsRegistry;

public class Smelting implements Listener {
    private final JavaPlugin plugin;

    public Smelting(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (ApplicableItemsRegistry.pickaxe_applicable.contains(tool.getType()) &&
            UtilityMethods.getEnchantmentLevel(tool, "Smelting") > 0 &&
            !UtilityMethods.hasVanillaEnchantment(tool, Enchantment.SILK_TOUCH)) {
            Block block = event.getBlock();
            Material blockType = block.getType();
            smelt(event, player, tool, block, blockType);
        }
    }

    public static void smelt(BlockBreakEvent event, Player player, ItemStack tool, Block block, Material blockType) {
        // Get the smelted item equivalent for the block type
        ItemStack smeltedItemPrototype = getSmeltedItem(blockType);
        if (smeltedItemPrototype != null) {
            event.setDropItems(false); // Prevent the block from dropping items normally
            // Calculate the correct amount of smelted items considering Fortune
            Collection<ItemStack> drops = block.getDrops(tool); // This considers Fortune
            int smeltedItemCount = drops.stream().mapToInt(ItemStack::getAmount).sum();
            if(smeltedItemCount > 0) {
                ItemStack smeltedItem = smeltedItemPrototype.clone();
                smeltedItem.setAmount(smeltedItemCount); // Set the total count for the smelted item
                
                if(UtilityMethods.getEnchantmentLevel(tool, "AutoLooting") == 0) {
                    block.getWorld().dropItemNaturally(block.getLocation(), smeltedItem);
                } else {
                    // If AutoLooting is applied, try to add the item directly to the player's inventory.
                    HashMap<Integer, ItemStack> unadded = player.getInventory().addItem(smeltedItem);

                    // Check if there was any overflow (i.e., items that couldn't be added because the inventory was full)
                    if (!unadded.isEmpty()) {
                        // If there are overflow items, drop them naturally at the block location.
                        for (ItemStack remaining : unadded.values()) {
                            block.getWorld().dropItemNaturally(block.getLocation(), remaining);
                        }
                    }
                }
            }
        }
    }

    public static ItemStack getSmeltedItem(Material blockType) {
        String typeName = blockType.name();
        if (typeName.equals("ANCIENT_DEBRIS")) {
            return new ItemStack(Material.NETHERITE_SCRAP);
        } else if (typeName.equals("NETHER_QUARTZ_ORE")) {
            return new ItemStack(Material.QUARTZ);
        } else if (typeName.equals("NETHER_GOLD_ORE")) {
            return new ItemStack(Material.GOLD_INGOT);
        } else if (typeName.equals("SEA_LANTERN")) {
            return new ItemStack(Material.PRISMARINE_CRYSTALS);
        } else if (typeName.equals("GLOWSTONE")) {
            return new ItemStack(Material.GLOWSTONE_DUST);
        }

        if (typeName.endsWith("_ORE")) {
            // Attempt to construct the smelted material name
            String baseName = typeName.substring(0, typeName.length() - 4); // Remove "_ORE"
            String smeltedName;
            if(baseName.contains("DIAMOND") ||
               baseName.contains("EMERALD") ||
               baseName.contains("STONE")) {
                smeltedName = baseName;
            } else if(baseName.contains("LAPIS")) {
                smeltedName = "LAPIS_LAZULI";
            } else {
                smeltedName = baseName + "_INGOT";
            }
            smeltedName = smeltedName.replaceFirst("^DEEPSLATE_", "");
            
            try {
                Material smeltedMaterial = Material.valueOf(smeltedName);
                return new ItemStack(smeltedMaterial);
            } catch (IllegalArgumentException e) {
                // Handle special cases
                return null;
            }
        }
        return null; // Return null if no matching smelted material is found
    }
}


