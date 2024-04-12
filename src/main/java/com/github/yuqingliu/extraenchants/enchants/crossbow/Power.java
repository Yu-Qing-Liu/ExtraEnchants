package com.github.yuqingliu.extraenchants.enchants.crossbow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;

public class Power implements Listener {
    private final JavaPlugin plugin;

    public Power(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof LivingEntity) {
                LivingEntity shooter = (LivingEntity) arrow.getShooter();
                ItemStack handItem = shooter.getEquipment().getItemInMainHand();

                // Check if the hand item is a crossbow with Power enchantment
                if (handItem.getType() == Material.CROSSBOW && handItem.containsEnchantment(Enchantment.ARROW_DAMAGE)) {
                    int powerLevel = handItem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);

                    // Calculate additional damage based on Power level
                    double additionalDamage = calculateAdditionalDamage(powerLevel, event.getFinalDamage());

                    // Apply the additional damage
                    event.setDamage(event.getDamage() + additionalDamage);
                }
            }
        }
    }

    private double calculateAdditionalDamage(int powerLevel, double baseDamage) {
        // This is a simplified formula and might need adjustment for balance
        return baseDamage * (0.25 * (powerLevel + 1));
    }
}

