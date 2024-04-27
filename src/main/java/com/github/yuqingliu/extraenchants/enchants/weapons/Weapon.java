package com.github.yuqingliu.extraenchants.enchants.weapons;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.util.Vector;

import java.util.Random;

import com.github.yuqingliu.extraenchants.enchants.ApplicableItemsRegistry;

public class Weapon {
    
    public static double calculateBaseDamage(ItemStack weapon, Vector arrow, boolean isCriticalHit) {
        // Melee weapons
        if(ApplicableItemsRegistry.melee_applicable.contains(weapon.getType())) {
            // Calculate melee weapon damage
            return calculateMeleeDamage(weapon, isCriticalHit);
        } else if(ApplicableItemsRegistry.ranged_applicable.contains(weapon.getType())) {
            // Calculate arrow damage based on Power enchant 
            if (weapon != null && weapon.hasItemMeta()) {
                // Retrieve the level of the Power enchantment
                int powerLevel = 0;
                if(weapon.getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE)) {
                    powerLevel = weapon.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
                }
                return calculateArrowDamage(powerLevel, isCriticalHit, arrow);
            }
        } 
        return 0;
    }

    public static void applyHit(Player player, LivingEntity entity, ItemStack weapon, Vector arrow, boolean isCriticalHit) {
        double baseDamage = calculateBaseDamage(weapon, arrow, isCriticalHit);   
        entity.damage(baseDamage, player);
        // Check if weapon has flame enchant, and set the target on fire for 5 seconds
        if (weapon != null && weapon.hasItemMeta()) {
            int flameLevel = 0;
            int fireAspectLevel = 0;
            if(weapon.getItemMeta().hasEnchant(Enchantment.ARROW_FIRE)) {
                flameLevel = weapon.getEnchantmentLevel(Enchantment.ARROW_FIRE);
            }
            if(weapon.getItemMeta().hasEnchant(Enchantment.FIRE_ASPECT)) {
                fireAspectLevel = weapon.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
            }
            int flameDuration = flameLevel*5;
            int fireDuration = fireAspectLevel*4;
            int totalDuration = flameDuration + fireDuration;
            if(totalDuration > 0) {
                // Set target on fire
                entity.setFireTicks(totalDuration * 20);
            }
        }
    }

    public static double calculateArrowDamage(int powerLevel, boolean isCriticalHit, Vector arrow) {
        // Calculate damage increase based on Power enchantment
        double damageIncrease = 0.25 * (powerLevel + 1);

        // Arrow base damage (defaults to 2)
        double baseDamage = 2.0;

        // Calculate total speed of the arrow
        double xMotion = arrow.getX();
        double yMotion = arrow.getY();
        double zMotion = arrow.getZ();
        double totalSpeed = Math.sqrt(xMotion * xMotion + yMotion * yMotion + zMotion * zMotion);

        // Calculate initial damage
        double initialDamage = totalSpeed * baseDamage;

        // Round up to the nearest integer
        int roundedDamage = (int) Math.ceil(initialDamage);

        // If it's a critical hit, add a random whole number between 0 and half of the damage
        if (isCriticalHit) {
            Random random = new Random();
            roundedDamage += random.nextInt(roundedDamage / 2 + 1);
        }

        // Apply damage increase from Power enchantment
        double finalDamage = roundedDamage * (1 + damageIncrease);

        return finalDamage;
    }

    public static double calculateMeleeDamage(ItemStack weapon, boolean isCriticalHit) {
        double baseDamage = getBaseDamage(weapon);
        double sharpnessDamage = getSharpnessDamage(weapon);
        double totalDamage = baseDamage + sharpnessDamage;

        if (isCriticalHit) {
            totalDamage *= 1.5; // Apply critical hit (50% more damage)
        }

        return totalDamage;
    }

    private static double getSharpnessDamage(ItemStack weapon) {
        // Retrieve extra damage from Sharpness enchantment
        int sharpnessLevel = weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        double sharpnessDamage = 0.5 * sharpnessLevel + 0.5;
        return sharpnessDamage;
    }

    private static double getBaseDamage(ItemStack weapon) {
        // Retrieve base damage of the weapon
        // Implement your logic to get the base damage based on item type
        Material type = weapon.getType();
        double baseDamage = 0.0; // default base damage

        // Example: define base damage for each material type
        switch (type) {
            case WOODEN_SWORD:
                baseDamage = 4.0;
                break;
            case GOLDEN_SWORD:
                baseDamage = 4.0;
                break;
            case STONE_SWORD:
                baseDamage = 5.0;
                break;
            case IRON_SWORD:
                baseDamage = 6.0;
                break;
            case DIAMOND_SWORD:
                baseDamage = 7.0;
                break;
            case NETHERITE_SWORD:
                baseDamage = 8.0;
                break;            // Add cases for other weapon types as needed
            default:
                baseDamage = 1.0;
                break;
        }

        return baseDamage;
    }
}
