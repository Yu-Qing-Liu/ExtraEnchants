package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class AutoLooting extends CustomEnchantment {
    private Enchantment smelting;
    private Enchantment replant;

    public AutoLooting(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.AUTO_LOOTING,
            Component.text("Auto Looting", nameColor),
            Component.text("Items go directly into your inventory", descriptionColor),
            1,
            Stream.concat(
                itemRepository.getItems().get(ItemCategory.WEAPON).stream(),
                itemRepository.getItems().get(ItemCategory.TOOL).stream()
            ).collect(Collectors.toSet()),
            new HashSet<>(),
            "x^2",
            "x"
        );
    }
    
    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
        this.smelting = enchantmentRepository.getEnchantment(EnchantID.SMELTING);
        this.replant = enchantmentRepository.getEnchantment(EnchantID.REPLANT);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        if (this.getEnchantmentLevel(tool) > 0 &&
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
            if (this.getEnchantmentLevel(weapon) > 0) {
                for (ItemStack drop : event.getDrops()) {
                    killer.getInventory().addItem(drop);
                }
                event.getDrops().clear();
            }
        }
    }

    public void autoloot(BlockBreakEvent event, Player player, Block block, ItemStack tool) {
        for (ItemStack drop : block.getDrops(tool)) {
            event.setDropItems(false);
            player.getInventory().addItem(drop);
        }
    }
}
