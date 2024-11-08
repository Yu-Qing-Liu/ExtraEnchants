package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Replant extends CustomEnchantment {
    private AutoLooting autoLooting;

    public Replant(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.REPLANT,
            Component.text("Replant", nameColor),
            Component.text("Replants mature crops", descriptionColor),
            1,
            itemRepository.getItems().get(ItemCategory.HOE),
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
            plant(event, player, tool, block);
        }
    }

    public void plant(BlockBreakEvent event, Player player, ItemStack tool, Block block) {
        if (block.getBlockData() instanceof Ageable) {
            Ageable age = (Ageable) block.getBlockData();
            if (age.getAge() == age.getMaximumAge()) {
                event.setCancelled(true);
                Collection<ItemStack> drops = block.getDrops(tool);
                int totalCropCount = drops.stream().mapToInt(ItemStack::getAmount).sum();
                if (totalCropCount > 0) {
                    if(autoLooting.getEnchantmentLevel(tool) == 0) {
                        for(ItemStack drop : drops) {
                            block.getWorld().dropItemNaturally(block.getLocation(), drop);
                        }
                    } else {
                        for (ItemStack drop : drops) {
                            Map<Integer, ItemStack> unadded = player.getInventory().addItem(drop);
                            if (!unadded.isEmpty()) {
                                for (ItemStack remaining : unadded.values()) {
                                    block.getWorld().dropItemNaturally(block.getLocation(), remaining);
                                }
                            }
                        }
                    }
                }
                age.setAge(0);
                block.setBlockData(age);
                if(tool.getItemMeta() instanceof Damageable) {
                    Damageable damageable = (Damageable) tool.getItemMeta();
                    if(damageable.getDamage() < tool.getType().getMaxDurability() - 1) {
                        damageable.setDamage(damageable.getDamage() + 1);
                        tool.setItemMeta((ItemMeta) damageable);
                    }
                }
            }
        } else {
            if(autoLooting.getEnchantmentLevel(tool) > 0) {
                autoLooting.autoloot(event, player, block, tool);
            }
        }
    }
}
