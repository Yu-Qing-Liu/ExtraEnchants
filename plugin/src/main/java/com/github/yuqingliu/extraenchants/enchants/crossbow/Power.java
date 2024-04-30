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
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.enchants.weapons.Weapon;

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
                    // Calculate the original damage based on the level of Power enchantment
                    double speedPerTick = 63 / 20.0;
                    Vector terminalVelocity = new Vector(0, -speedPerTick, 0);
                    double originalDamage = Weapon.calculateBaseDamage(handItem, terminalVelocity, true);
                    // Set the event's damage to the original damage
                    event.setDamage(originalDamage);
                }
            }
        }
    }
}

