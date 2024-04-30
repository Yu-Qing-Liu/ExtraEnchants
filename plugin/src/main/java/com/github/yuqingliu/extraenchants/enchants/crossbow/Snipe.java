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

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;
import com.github.yuqingliu.extraenchants.enchants.weapons.Weapon;

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
                    double speedPerTick = 63 / 20.0;
                    Vector terminalVelocity = new Vector(0, -speedPerTick, 0);
                    double originalDamage = Weapon.calculateBaseDamage(bow, terminalVelocity, true);
                    // Set the event's damage to the original damage
                    event.setDamage(originalDamage);
                }
            }
        }
    }

    public void setArrowSpeed(Arrow arrow, int level) {
        double speedMultiplier = 1.0 + (0.25 * level); // 25% increase per level
        Vector velocity = arrow.getVelocity();
        velocity.multiply(speedMultiplier);
        arrow.setVelocity(velocity); // Apply the new velocity to the arrow
    }

    public static Vector cloneVector(Vector original) {
        // Create a new vector object with the same components as the original vector
        return new Vector(original.getX(), original.getY(), original.getZ());
    }
}
