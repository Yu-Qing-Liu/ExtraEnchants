package com.github.yuqingliu.extraenchants.enchants.crossbow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.item.weapon.implementations.RangedWeapon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Power implements Listener {
    private final Enchantment enchant;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof LivingEntity) {
                LivingEntity shooter = (LivingEntity) arrow.getShooter();
                ItemStack handItem = shooter.getEquipment().getItemInMainHand();
                // Check if the hand item is a crossbow with Power enchantment
                if (handItem.getType() == Material.CROSSBOW && enchant.getEnchantmentLevel(handItem) > 0) {
                    // Calculate the original damage based on the level of Power enchantment
                    RangedWeapon crossbow = new RangedWeapon(handItem);
                    double originalDamage = crossbow.getDamage();
                    // Set the event's damage to the original damage
                    event.setDamage(originalDamage);
                }
            }
        }
    }
}

