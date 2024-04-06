package com.github.yuqingliu.extraenchants.enchants.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.util.RayTraceResult;
import org.bukkit.Material;

import com.github.yuqingliu.extraenchants.utils.UtilityMethods;

public class Homing implements Listener {
    private final JavaPlugin plugin;

    public Homing(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack bow = event.getBow();
        if (bow == null || bow.getType() != Material.BOW) return;
        if (UtilityMethods.hasEnchantment(bow, "Homing", 1)) {
            Arrow arrow = (Arrow) event.getProjectile();
            setHomingArrow(player, arrow);
        }
    }

    public void setHomingArrow(Player player, Arrow arrow) {
        arrow.setGravity(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!arrow.isValid() || arrow.isOnGround()) {
                    this.cancel();
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
            }
        }.runTaskTimer(this.plugin, 0L, 1L); // Update the direction every tick for smooth tracking
    }
}
