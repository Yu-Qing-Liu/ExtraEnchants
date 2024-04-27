package com.github.yuqingliu.extraenchants.enchants.melee;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.bukkit.Sound;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.event.block.Action;
import org.bukkit.util.RayTraceResult;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;
import com.github.yuqingliu.extraenchants.enchants.weapons.Weapon;

public class SonicBoom implements Listener {
    private final JavaPlugin plugin;
    private final int cooldown = 5000;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public SonicBoom(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(item != null) {
                int SonicBoomLevel = UtilityMethods.getEnchantmentLevel(item, "SonicBoom");
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
        int teleportDelay = 5;

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
                delayedDamage(hitEntity, player, teleportDelay);
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
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(teleportLocation.add(0, 1, 0)); // Teleport to one block above
                    player.getWorld().createExplosion(teleportLocation, 4.0F, false, false, player);
                    player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, teleportLocation, 1);
                }
            }.runTaskLater(plugin, delay); // "plugin" should be your instance of JavaPlugin
        }
    }

    private void delayedDamage(Entity entity, Player player, int delay) {
        // Get the item in the main hand, which is considered for the hit.
        ItemStack weapon = player.getInventory().getItemInMainHand();

        // Damage the entity after some time
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity instanceof LivingEntity && !entity.isDead()) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    Weapon.applyHit(player, livingEntity, weapon, null, true);
                }
            }
        }.runTaskLater(plugin, delay);  // "plugin" should be your instance of JavaPlugin
    }

    private long getRemainingCooldownTime(Player player) {
        long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        long elapsed = System.currentTimeMillis() - lastUsed;
        long cooldownDuration = cooldown;
        return cooldownDuration - elapsed;
    }
}
