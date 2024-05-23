package com.github.yuqingliu.extraenchants.enchants.crossbow;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.block.Action;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.Sound;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.Scheduler;

import com.github.yuqingliu.extraenchants.item.weapon.implementations.RangedWeapon;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
public class RapidFire implements Listener {
    private final Enchantment enchant;
    private final int cooldown;
    private HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        // Check for left-click block or air with a crossbow in hand
        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && player.getInventory().getItemInMainHand().getType() == Material.CROSSBOW) {
            ItemStack crossbow = player.getInventory().getItemInMainHand();
            if (enchant.getEnchantmentLevel(crossbow) > 0) {
                long remainingTime = getRemainingCooldownTime(player);
                if (remainingTime <= 0) {
                    fireProjectiles(player, crossbow);
                    // Reset the cooldown
                    cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                } else {
                    int secondsLeft = (int) (remainingTime / 1000);
                    player.sendMessage("Rapid Fire is on cooldown. Please wait " + secondsLeft + " more seconds.");
                }
            }
        }
    }

    private long getRemainingCooldownTime(Player player) {
        long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        long elapsed = System.currentTimeMillis() - lastUsed;
        long cooldownDuration = cooldown * 1000;
        return cooldownDuration - elapsed;
    }

    private void fireProjectiles(Player player, ItemStack weapon) {
        BukkitTask task = Scheduler.runTimer(() -> {
            fireParticleBeam(player, weapon);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.0f);
        }, Duration.ofMillis(100), Duration.ofSeconds(0));
        Scheduler.runLater(() -> {
            task.cancel();
        }, Duration.ofMillis(500));
    }

    public static Location getRightSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    private Location calculateFinalBeamLocation(Player player, Location startLocation, Vector direction, double beamRange) {
        for (double i = 0; i <= beamRange; i += 0.1) {
            Location point = startLocation.clone().add(direction.clone().multiply(i));
            if (point.getBlock().getType().isSolid()) { // Check if the block is solid
                return point; // Return this location as the beam hit a solid block
            }
        }
        return startLocation.clone().add(direction.multiply(beamRange)); // Return the maximum range location
    }

    private void fireParticleBeam(Player player, ItemStack weapon) {
        Location startLocation = getRightSide(player.getEyeLocation(), 0.45).subtract(0, 0.2, 0);
        Vector direction = getRandomizedDirection(player.getEyeLocation().getDirection(), 0.05);
        double beamRange = 30.0;

        Location finalLocation = calculateFinalBeamLocation(player, startLocation, direction, beamRange);

        // Perform a ray trace between startLocation and finalLocation
        RayTraceResult result = player.getWorld().rayTraceEntities(startLocation, direction, beamRange, 0.5, entity ->
            (entity instanceof LivingEntity && entity != player));

        // Draw the particle beam from start to final location
        Vector finalDirection = finalLocation.toVector().subtract(startLocation.toVector()).normalize();
        for (double i = 0; i <= finalLocation.distance(startLocation); i += 0.1) {
            Location point = startLocation.clone().add(finalDirection.clone().multiply(i));
            player.getWorld().spawnParticle(Particle.CRIT, point, 1, 0.01, 0.01, 0.01, 0.01);
        }

        // Apply damage if the ray trace hits an entity
        if (result != null && result.getHitEntity() != null) {
            LivingEntity target = (LivingEntity) result.getHitEntity();
            RangedWeapon crossbow = new RangedWeapon(weapon);
            crossbow.applyHit(player, target);
            target.setNoDamageTicks(0);
        }
    }

    private Vector getRandomizedDirection(Vector originalDirection, double spread) {
        Random random = new Random();

        // Create a randomized vector
        double offsetX = (random.nextDouble() - 0.5) * spread;
        double offsetY = (random.nextDouble() - 0.5) * spread;
        double offsetZ = (random.nextDouble() - 0.5) * spread;

        // Add the random offsets to the original direction vector
        return originalDirection.add(new Vector(offsetX, offsetY, offsetZ)).normalize();
    }
}
