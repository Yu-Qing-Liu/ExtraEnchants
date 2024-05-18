package com.github.yuqingliu.extraenchants.item.weapon.implementations;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.item.weapon.AbstractWeapon;

public class RangedWeapon extends AbstractWeapon {
    private double projectileBaseDamage = 2.0;
    private double speedPerTick = 63 / 20.0;
    private Vector terminalVelocity = new Vector(0, -speedPerTick, 0);

    public RangedWeapon(ItemStack rangedWeapon) {
        super(rangedWeapon);
        damage = calculateBaseDamage();
    }

    private void applyConditions(LivingEntity victim) {
        int flameLevel = weapon.getEnchantmentLevel(Enchantment.ARROW_FIRE);
        int duration = flameLevel * 5;
        if(duration > 0) {
            victim.setFireTicks(duration * 20);
        }
    }
    
    public double calculateBaseDamage() {
        int powerLevel = weapon.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
        double damageIncrease = 0.25 * (powerLevel + 1);

        double xMotion = terminalVelocity.getX();
        double yMotion = terminalVelocity.getY();
        double zMotion = terminalVelocity.getZ();
        double totalSpeed = Math.sqrt(xMotion * xMotion + yMotion * yMotion + zMotion * zMotion);
        
        double initialDamage = totalSpeed + projectileBaseDamage;
        
        double enchantedDamage = initialDamage * (1 + damageIncrease);
        double finalDamage = enchantedDamage * 0.5 + enchantedDamage;
        return finalDamage;
    }

    public void applyHit(Player user, LivingEntity victim) {
        victim.damage(damage, user);
        applyConditions(victim);
    }
}
