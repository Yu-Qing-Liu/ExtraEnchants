package com.github.yuqingliu.extraenchants.enchants.crossbow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.Material;

import com.github.yuqingliu.extraenchants.utils.UtilityMethods;

public class Snipe implements Listener {
    private final JavaPlugin plugin;

    public Snipe(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCrossBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        ItemStack bow = event.getBow();
        if (bow == null || bow.getType() != Material.CROSSBOW) return;
        
        // Check for "Snipe" enchantment presence and level
        int level = UtilityMethods.getEnchantmentLevel(bow, "Snipe");
        if (level > 0 && event.getProjectile() instanceof Arrow) { // Check specifically for Arrow
            Arrow arrow = (Arrow) event.getProjectile();
            setArrowSpeed(arrow, level);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                ItemStack bow = shooter.getInventory().getItemInMainHand();
                
                // Ensure the shooter is using a crossbow with the Snipe enchantment
                if (bow.getType() == Material.CROSSBOW && UtilityMethods.getEnchantmentLevel(bow, "Snipe") > 0) {
                    // Calculate the original damage based on the level of Snipe enchantment
                    // This is a placeholder for your damage calculation logic
                    double originalDamage = calculateOriginalDamage(event.getFinalDamage(), UtilityMethods.getEnchantmentLevel(bow, "Snipe"));
                    
                    // Set the event's damage to the original damage
                    event.setDamage(originalDamage);
                }
            }
        }
    }

    private double calculateOriginalDamage(double currentDamage, int snipeLevel) {
        // This should inversely apply the damage increase caused by the speed boost
        return currentDamage / (1.0 + (0.4 * snipeLevel));
    }

    public void setArrowSpeed(Arrow arrow, int level) {
        double speedMultiplier = 1.0 + (0.4 * level); // 40% increase per level
        Vector velocity = arrow.getVelocity();
        velocity.multiply(speedMultiplier);
        arrow.setVelocity(velocity); // Apply the new velocity to the arrow
    }
}
