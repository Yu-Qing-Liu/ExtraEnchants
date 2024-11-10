package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;
import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.AutoLooting;
import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.Replant;
import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.Smelting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class SilkTouch extends VanillaEnchantment {
    private AutoLooting autoLooting;
    private Smelting smelting;
    private Replant replant;

    public SilkTouch(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.SILK_TOUCH,
            Component.text("Silk Touch", nameColor),
            Component.text("Mined blocks will drop as blocks instead of breaking into other items/blocks.", descriptionColor),
            Enchantment.SILK_TOUCH.getMaxLevel(),
            new HashSet<>(),
            new HashSet<>(),
            "x^2",
            "x",
            Enchantment.SILK_TOUCH
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
        this.autoLooting = (AutoLooting) enchantmentRepository.getEnchantment(EnchantID.AUTO_LOOTING);
        this.smelting = (Smelting) enchantmentRepository.getEnchantment(EnchantID.SMELTING);
        this.replant = (Replant) enchantmentRepository.getEnchantment(EnchantID.REPLANT);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (this.getEnchantmentLevel(tool) > 0 && !isDelicate(blockType)) {
            ItemStack clonedTool = cloneToolWithoutSilkTouch(tool);
            if(autoLooting.getEnchantmentLevel(tool) == 0 && smelting.getEnchantmentLevel(tool) == 0 && replant.getEnchantmentLevel(tool) == 0) {
                event.setDropItems(false);
                Collection<ItemStack> drops = block.getDrops(clonedTool);
                drops.forEach(drop -> block.getWorld().dropItemNaturally(block.getLocation(), drop));
            } else if(smelting.getEnchantmentLevel(tool) == 0 && replant.getEnchantmentLevel(tool) == 0) {
                event.setDropItems(false);
                Collection<ItemStack> drops = block.getDrops(clonedTool);
                Map<Integer, ItemStack> unadded = player.getInventory().addItem(drops.toArray(new ItemStack[0]));
                if (!unadded.isEmpty()) {
                    unadded.values().forEach(item -> block.getWorld().dropItemNaturally(block.getLocation(), item));
                }
            } else if(smelting.getEnchantmentLevel(tool) > 0) {
                smelting.smelt(event, player, clonedTool, block, blockType);
            } else if(replant.getEnchantmentLevel(tool) > 0) {
                replant.plant(event, player, clonedTool, block);
            }
        } 
    }

    private boolean isDelicate(Material blockType) {
        String typeName = blockType.name();
        if (typeName.contains("GLASS") || typeName.endsWith("PANE")) {
            return true;
        }
        switch (blockType) {
            case ICE:
            case PACKED_ICE:
            case BLUE_ICE:
            case SPAWNER:
            case AMETHYST_CLUSTER:
            case BEE_NEST:
            case BOOKSHELF:
            case BUDDING_AMETHYST:
            case CAKE:
            case CAMPFIRE:
            case CHISELED_BOOKSHELF:
            case ENDER_CHEST:
            case GLOWSTONE:
            case SCULK:
            case SCULK_VEIN:
            case SCULK_SENSOR:
            case SCULK_CATALYST:
            case SCULK_SHRIEKER:
            case CALIBRATED_SCULK_SENSOR:
            case SEA_LANTERN:
            case SNOW:
            case SNOW_BLOCK:
            case SOUL_CAMPFIRE:
            case TURTLE_EGG:
            case TWISTING_VINES:
            case WEEPING_VINES:
                return true;
            default:
                return false;
        }
    }

    private ItemStack cloneToolWithoutSilkTouch(ItemStack tool) {
        ItemStack clone = tool.clone();
        ItemMeta meta = clone.getItemMeta();
        if (meta != null && meta.hasEnchant(org.bukkit.enchantments.Enchantment.SILK_TOUCH)) {
            meta.removeEnchant(org.bukkit.enchantments.Enchantment.SILK_TOUCH);
            clone.setItemMeta(meta);
        } 
        return clone;
    }
}
