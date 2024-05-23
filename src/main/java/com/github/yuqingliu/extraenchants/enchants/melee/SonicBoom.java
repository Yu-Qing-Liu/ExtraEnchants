package com.github.yuqingliu.extraenchants.enchants.melee;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.bukkit.Sound;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.event.block.Action;
import org.bukkit.util.RayTraceResult;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.Scheduler;

import com.github.yuqingliu.extraenchants.item.weapon.implementations.MeleeWeapon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SonicBoom implements Listener {
    private final Enchantment enchant;
    private final int cooldown;
    private HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(item != null) {
                int SonicBoomLevel = enchant.getEnchantmentLevel(item);
                if(SonicBoomLevel > 0) {
                    long remainingTime = getRemainingCooldownTime(player);
                    if (remainingTime <= 0) {
                        fireSonicBoom(player);
                        // Reset the cooldown
                        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                    } else {
                        int secondsLeft = (int) (remainingTime / 1000);
                        player.sendMessage("Sonic Boom is on cooldown. Please wait " + secondsLeft + " more seconds.");
                    }
                }
            }
        }
    }

    public void fireSonicBoom(Player player) {
        Location eye = player.getEyeLocation(); // Player's eye location
        Vector direction = eye.getDirection(); // Direction player is looking

        int distance = 30; // How far the sonic boom goes
        int teleportDelay = 500;
        int damageDelay = 250;

        for (int i = 0; i < distance; i++) {
            Location step = eye.add(direction);
            player.getWorld().spawnParticle(Particle.SONIC_BOOM, step, 1);
            player.getWorld().playSound(step, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0F, 1.0F);

            // Check for entities in the beam path
            RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(eye, direction, i, 0.5);
            if (rayTraceResult != null && rayTraceResult.getHitEntity() != null) {
                Entity hitEntity = rayTraceResult.getHitEntity();
                Location hitEntityLocation = hitEntity.getLocation();
                delayedTeleportPlayer(player, hitEntityLocation, teleportDelay);
                delayedDamage(hitEntity, player, damageDelay);
                break;
            } else if (step.getBlock().getType().isSolid()) {
                Location hitLocation = step.getBlock().getLocation();
                delayedTeleportPlayer(player, hitLocation, teleportDelay);
                break; // Stop the beam if it hits a solid block
            } else if (i == distance - 1) {
                Location hitLocation = step.getBlock().getLocation();
                delayedTeleportPlayer(player, hitLocation, teleportDelay);
            }
        }
    }

    private void delayedTeleportPlayer(Player player, Location location, int delay) {
        if (location != null) {
            // Delay the teleportation by 1 second (20 ticks)
            float currentYaw = player.getLocation().getYaw();
            float currentPitch = player.getLocation().getPitch();

            // Clone the target location to preserve original location details.
            Location teleportLocation = location.clone();
            teleportLocation.setYaw(currentYaw);
            teleportLocation.setPitch(currentPitch);
            Scheduler.runLater(() -> {
                player.teleport(teleportLocation.add(0, 1, 0)); // Teleport to one block above
                player.getWorld().createExplosion(teleportLocation, 4.0F, false, false, player);
                player.getWorld().spawnParticle(Particle.EXPLOSION, teleportLocation, 1);
            }, Duration.ofMillis(delay));
        }
    }

    private void delayedDamage(Entity entity, Player player, int delay) {
        // Get the item in the main hand, which is considered for the hit.
        ItemStack weapon = player.getInventory().getItemInMainHand();
        MeleeWeapon item = new MeleeWeapon(weapon);
        Scheduler.runLater(() -> {
            LivingEntity livingEntity = (LivingEntity) entity;
            item.applyHit(player, livingEntity);
        }, Duration.ofMillis(delay));
    }

    private long getRemainingCooldownTime(Player player) {
        long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        long elapsed = System.currentTimeMillis() - lastUsed;
        long cooldownDuration = cooldown * 1000;
        return cooldownDuration - elapsed;
    }
}
