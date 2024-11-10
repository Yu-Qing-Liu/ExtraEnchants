package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Smelting extends CustomEnchantment {
    private AutoLooting autoLooting;

    public Smelting(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.SMELTING,
            Component.text("Smelting", nameColor),
            Component.text("Smelts mined ores", descriptionColor),
            1,
            itemRepository.getItems().get(ItemCategory.PICKAXE),
            new HashSet<>(),
            "x^2",
            "x"
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
        this.autoLooting = (AutoLooting) enchantmentRepository.getEnchantment(EnchantID.AUTO_LOOTING);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (this.getEnchantmentLevel(tool) > 0 &&
            tool.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.SILK_TOUCH) == 0) {
            Block block = event.getBlock();
            Material blockType = block.getType();
            smelt(event, player, tool, block, blockType);
        }
    }

    public void smelt(BlockBreakEvent event, Player player, ItemStack tool, Block block, Material blockType) {
        ItemStack smeltedItem = getSmeltedItem(blockType);
        if (smeltedItem != null) {
            event.setDropItems(false);
            Collection<ItemStack> drops = block.getDrops(tool);
            int smeltedItemCount = drops.stream().mapToInt(ItemStack::getAmount).sum();
            if(smeltedItemCount > 0) {
                smeltedItem.setAmount(smeltedItemCount);
                if(autoLooting.getEnchantmentLevel(tool) > 0) {
                    Map<Integer, ItemStack> unadded = player.getInventory().addItem(smeltedItem);
                    if (!unadded.isEmpty()) {
                        for (ItemStack remaining : unadded.values()) {
                            block.getWorld().dropItemNaturally(block.getLocation(), remaining);
                        }
                    }
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), smeltedItem);
                }
            }
        } else {
            if(autoLooting.getEnchantmentLevel(tool) > 0) {
                autoLooting.autoloot(event, player, block, tool);
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
            String baseName = typeName.substring(0, typeName.length() - 4);
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
                return null;
            }
        }
        return null;
    }
}
