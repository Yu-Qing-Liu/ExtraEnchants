package com.github.yuqingliu.extraenchants.enchants.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.bukkit.util.RayTraceResult;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.time.Duration;

import com.github.yuqingliu.extraenchants.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.ExtraEnchantsScheduler;
import com.github.yuqingliu.extraenchants.item.weapon.implementations.RangedWeapon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Homing implements Listener {
    private final Enchantment enchant;

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack bow = event.getBow();
        if (bow == null || bow.getType() != Material.BOW) return;
        if (enchant.getEnchantmentLevel(bow) > 0) {
            Arrow arrow = (Arrow) event.getProjectile();
            setHomingArrow(player, arrow);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof LivingEntity) {
                LivingEntity shooter = (LivingEntity) arrow.getShooter();
                ItemStack handItem = shooter.getEquipment().getItemInMainHand();

                // Check if the hand item is a crossbow with Homing enchantment
                if (handItem.getType() == Material.BOW && enchant.getEnchantmentLevel(handItem) > 0) {
                    // Calculate additional damage based on Power level
                    RangedWeapon bow = new RangedWeapon(handItem);
                    double originalDamage = bow.getDamage();

                    // Apply the original damage
                    event.setDamage(originalDamage);
                }
            }
        }
    }

    public void setHomingArrow(Player player, Arrow arrow) {
        arrow.setGravity(false);
        BukkitTask task = ExtraEnchantsScheduler.runTimer(() -> {
            if (!arrow.isValid() || arrow.isOnGround()) {
                return;
            }

            // Define the maximum distance to check for entities
            double maxDistance = 256;
            RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                maxDistance,
                1.0,
                entity -> !entity.equals(player) && !entity.equals(arrow) // Make sure not to target the player or the arrow itself
            );

            Location targetLocation;
            if (rayTraceResult != null && rayTraceResult.getHitEntity() != null) {
                // An entity was hit by the ray trace
                targetLocation = rayTraceResult.getHitEntity().getLocation();
            } else {
                // No entity was found, fall back to targeting the block location
                targetLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(maxDistance));
            }

            Vector direction = targetLocation.toVector().subtract(arrow.getLocation().toVector()).normalize();

            // Set the arrow's new velocity towards the target
            arrow.setVelocity(direction);
        }, Duration.ofMillis(50), Duration.ofSeconds(0));
        // Cancel the task after a certain duration
        ExtraEnchantsScheduler.runLater(() -> {
            task.cancel();
        }, Duration.ofSeconds(15));
    }
}
