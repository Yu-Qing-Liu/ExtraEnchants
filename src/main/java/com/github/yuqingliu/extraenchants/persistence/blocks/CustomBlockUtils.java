package com.github.yuqingliu.extraenchants.persistence.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;
import java.util.HashSet;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.api.Keys;
import com.github.yuqingliu.extraenchants.persistence.blocks.threads.*;

public class CustomBlockUtils extends CustomBlockDatabase {
    // Add a custom block to the database
    public static void addCustomBlock(Block block, String blockType) {
        CustomBlock b = new CustomBlock(block, blockType);
        String w = block.getWorld().getName();
        if(blocks.containsKey(w)) {
            blocks.get(w).add(b);
        } else {
            Set<CustomBlock> blockList = new HashSet<>();
            blockList.add(b);
            blocks.put(w,blockList);
        }
        threadPool.execute(new AddCustomBlockTask(b, customBlocksFile));
    }
    // Remove a custom block from the the database
    public static void deleteCustomBlock(Block block, String blockType) {
        CustomBlock b = new CustomBlock(block, blockType);
        String w = block.getWorld().getName();
        if(blocks.containsKey(w)) {
            blocks.get(w).remove(b);
        } 
        threadPool.execute(new DeleteCustomBlockTask(b, customBlocksFile));
    }

    // Method to give the player a custom anvil
    public static void giveCustomAnvil(Player player) {
        ItemStack customAnvil = new ItemStack(Material.ANVIL);
        
        // Set display name
        ItemMeta meta = customAnvil.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Custom Anvil", NamedTextColor.GOLD));
            customAnvil.setItemMeta(meta);
        }
        
        // Add custom data to the ItemStack
        meta = customAnvil.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(Keys.getCustomUIBlock(), PersistentDataType.BOOLEAN, true);
        customAnvil.setItemMeta(meta);

        // Give the custom item to the player
        player.getInventory().addItem(customAnvil);
    }

    // Method to give the player a custom etable
    public static void giveCustomEtable(Player player) {
        ItemStack customEtable= new ItemStack(Material.ENCHANTING_TABLE);
        
        // Set display name
        ItemMeta meta = customEtable.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Custom Etable", NamedTextColor.GOLD));
            customEtable.setItemMeta(meta);
        }
        
        // Add custom data to the ItemStack
        meta = customEtable.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(Keys.getCustomUIBlock(), PersistentDataType.BOOLEAN, true);
        customEtable.setItemMeta(meta);

        // Give the custom item to the player
        player.getInventory().addItem(customEtable);
    }

    // Method to give the player a custom grindstone
    public static void giveCustomGrindstone(Player player) {
        ItemStack customGrindstone = new ItemStack(Material.GRINDSTONE);
        
        // Set display name
        ItemMeta meta = customGrindstone.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Custom Grindstone", NamedTextColor.GOLD));
            customGrindstone.setItemMeta(meta);
        }
        
        // Add custom data to the ItemStack
        meta = customGrindstone.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(Keys.getCustomUIBlock(), PersistentDataType.BOOLEAN, true);
        customGrindstone.setItemMeta(meta);

        // Give the custom item to the player
        player.getInventory().addItem(customGrindstone);
    }

    // Method to check if a block is a custom anvil
    public static boolean isCustomAnvil(Block block) {
        CustomBlock cblock = new CustomBlock(block, "custom-anvil");
        Set<CustomBlock> blockList = blocks.get(block.getWorld().getName());
        if(blockList == null || blockList.isEmpty()) return false;
        return blockList.contains(cblock);
    }

    // Method to check if a block is a custom etable
    public static boolean isCustomEtable(Block block) {
        CustomBlock cblock = new CustomBlock(block, "custom-etable");
        Set<CustomBlock> blockList = blocks.get(block.getWorld().getName());
        if(blockList == null || blockList.isEmpty()) return false;
        return blockList.contains(cblock);
    }

    // Method to check if a block is a custom grindstone
    public static boolean isCustomGrindstone(Block block) {
        CustomBlock cblock = new CustomBlock(block, "custom-grindstone");
        Set<CustomBlock> blockList = blocks.get(block.getWorld().getName());
        if(blockList == null || blockList.isEmpty()) return false;
        return blockList.contains(cblock);
    }
}
