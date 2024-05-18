package com.github.yuqingliu.extraenchants.enchants.bow;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.RayTraceResult;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Color;

import com.github.yuqingliu.extraenchants.enchantment.Enchantment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Focus implements Listener {
    private final JavaPlugin plugin;
    private final Enchantment enchant;
    private Particle.DustOptions beam = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 0.1F);

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) &&
            player.getInventory().getItemInMainHand().getType() == Material.BOW) {
            RayTraceResult rayTraceResult = player.rayTraceEntities(50);
            if (rayTraceResult != null && rayTraceResult.getHitEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) rayTraceResult.getHitEntity();
                markEntity(player, target);
                startBeamTask(player, target);
                event.setCancelled(true);
            }
        }
    }

    private void markEntity(Player player, Entity target) {
        // Construct the metadata key
        String markKey = "MarkedBy:" + player.getUniqueId().toString();

        // Clear previous mark by this player on any entity
        clearPreviousMark(player);

        // Set the new mark on the target entity
        target.setMetadata(markKey, new FixedMetadataValue(plugin, true));
    }

    private void clearPreviousMark(Player player) {
        String playerMarkKey = "MarkedBy:" + player.getUniqueId().toString();
        // Iterate over all entities to find any previously marked by this player and remove the mark
        for (LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (entity.hasMetadata(playerMarkKey)) {
                entity.removeMetadata(playerMarkKey, plugin);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                ItemStack bow = player.getInventory().getItemInMainHand();
                if (bow.getType() == Material.BOW && event.getEntity() instanceof LivingEntity) {
                    String markKey = "MarkedBy:" + player.getUniqueId().toString();
                    LivingEntity target = (LivingEntity) event.getEntity();
                    if (target.hasMetadata(markKey)) {
                        int level = enchant.getEnchantmentLevel(bow);
                        double extraDamage = event.getDamage() * 0.20 * level;
                        event.setDamage(event.getDamage() + extraDamage);
                    }
                }
            }
        } 
    }

    public static Location getRightSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    private void startBeamTask(Player player, Entity target) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!target.isValid() || target.isDead()) {
                    this.cancel();
                    return;
                }

                String markKey = "MarkedBy:" + player.getUniqueId().toString();
                if (!target.hasMetadata(markKey)) {
                    this.cancel();
                    return;
                }
                
                // Player is no longer holding bow
                if(!(enchant.getEnchantmentLevel(player.getEquipment().getItemInMainHand()) > 0)) {
                    target.removeMetadata(markKey, plugin);
                    this.cancel();
                    return;
                }

                Location rightHand = getRightSide(player.getEyeLocation(), 0.45).subtract(0, .2, 0);
                double height = target.getHeight();
                Location targetLocation = target.getLocation().add(0, height/2.0, 0); // Center the beam a bit better on the target
                Vector direction = targetLocation.toVector().subtract(rightHand.toVector()).normalize();
                
                if(!targetLocation.getWorld().equals(rightHand.getWorld())) {
                    target.removeMetadata(markKey, plugin);
                    this.cancel();
                    return;
                }

                double distance = rightHand.distance(targetLocation);
                if (distance > 50) {
                    target.removeMetadata(markKey, plugin);
                    this.cancel();
                    return;
                }
                
                // Check for blocks in the way
                RayTraceResult blockRayTraceResult = player.getWorld().rayTraceBlocks(rightHand, targetLocation.toVector().subtract(rightHand.toVector()).normalize(), rightHand.distance(targetLocation));
                if (blockRayTraceResult != null && blockRayTraceResult.getHitBlock() != null && blockRayTraceResult.getHitBlock().getType().isSolid()) {
                    target.removeMetadata(markKey, plugin);
                    this.cancel();
                    return; // Block is obstructing the line of sight
                }

                for (double d = 0; d <= distance; d += 0.025) {
                    Location point = rightHand.clone().add(direction.clone().multiply(d));
                    player.getWorld().spawnParticle(Particle.REDSTONE, point, 0, 0, 0, 0, 0, beam); // Ensure no extra velocity
                }
            }
        }.runTaskTimer(plugin, 0, 1); // 0 initial delay, 10 ticks interval
    }
}
