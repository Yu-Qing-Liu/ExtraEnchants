package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
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

public class Delicate extends CustomEnchantment {
    public Delicate(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.DELICATE,
            Component.text("Delicate", nameColor),
            Component.text("Avoid breaking immature crops", descriptionColor),
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
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (this.getEnchantmentLevel(tool) > 0) {
            Block block = event.getBlock();
            BlockData data = block.getBlockData();

            if (data instanceof Ageable) {
                Ageable ageable = (Ageable) data;
                if (ageable.getAge() < ageable.getMaximumAge()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
