package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;
import com.github.yuqingliu.extraenchants.weapon.implementations.RangedWeapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Power extends VanillaEnchantment {
    public Power(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.POWER,
            Component.text("Power", nameColor),
            Component.text("Increases arrow damage.", descriptionColor),
            Enchantment.POWER.getMaxLevel(),
            itemRepository.getItems().get(ItemCategory.CROSSBOW),
            new HashSet<>(),
            "x^2",
            "x",
            Enchantment.POWER
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof LivingEntity) {
                LivingEntity shooter = (LivingEntity) arrow.getShooter();
                ItemStack handItem = shooter.getEquipment().getItemInMainHand();
                if (handItem.getType() == Material.CROSSBOW && this.getEnchantmentLevel(handItem) > 0) {
                    RangedWeapon crossbow = new RangedWeapon(handItem);
                    double originalDamage = crossbow.getDamage();
                    event.setDamage(originalDamage);
                }
            }
        }
    }
}
