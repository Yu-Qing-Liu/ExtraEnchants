package com.github.yuqingliu.extraenchants.item.weapon.implementations;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.item.weapon.AbstractWeapon;

public class MeleeWeapon extends AbstractWeapon {
    public MeleeWeapon(ItemStack meleeWeapon) {
        super(meleeWeapon);
        damage = calculateBaseDamage();
    }

    private void applyConditions(LivingEntity victim) {
        int fireAspectLevel = weapon.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
        int duration = fireAspectLevel * 3;
        if(duration > 0) {
            victim.setFireTicks(duration * 20);
        }
    }
    
    public double getItemDamage() {
        Material type = weapon.getType();
        return switch (type) {
            case WOODEN_SWORD -> 4.0;
            case GOLDEN_SWORD -> 4.0;
            case STONE_SWORD -> 5.0;
            case IRON_SWORD -> 6.0;
            case DIAMOND_SWORD -> 7.0;
            case NETHERITE_SWORD -> 8.0;
            case WOODEN_AXE -> 4.0;
            case GOLDEN_AXE -> 4.0;
            case STONE_AXE -> 5.0;
            case IRON_AXE -> 6.0;
            case DIAMOND_AXE -> 7.0;
            case NETHERITE_AXE -> 8.0;
            default -> 1.0;
        };
    }

    public double calculateBaseDamage() {
        int sharpnessLevel = weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        double sharpnessDamage = 0.5 * sharpnessLevel + 0.5;
        return getItemDamage() + sharpnessDamage;
    }

    public void applyHit(Player user, LivingEntity victim) {
        victim.damage(damage, user);
        applyConditions(victim);
    }
}
