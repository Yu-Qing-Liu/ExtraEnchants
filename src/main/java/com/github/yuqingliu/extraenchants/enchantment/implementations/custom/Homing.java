package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.time.Duration;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.weapon.implementations.RangedWeapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Homing extends CustomEnchantment {
    public Homing(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.HOMING,
            Component.text("Homing", nameColor),
            Component.text("Guided Arrows", descriptionColor),
            1,
            itemRepository.getItems().get(ItemCategory.BOW),
            new HashSet<>(),
            "x^2",
            "x"
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);   
    }
    
    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        } 
        Player player = (Player) event.getEntity();
        ItemStack bow = event.getBow();
        if (bow == null || bow.getType() != Material.BOW) {
            return;
        } 
        if (this.getEnchantmentLevel(bow) > 0) {
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
                if (handItem.getType() == Material.BOW && this.getEnchantmentLevel(handItem) > 0) {
                    RangedWeapon bow = new RangedWeapon(handItem);
                    double originalDamage = bow.getDamage();
                    event.setDamage(originalDamage);
                }
            }
        }
    }

    private void setHomingArrow(Player player, Arrow arrow) {
        arrow.setGravity(false);
        Scheduler.runTimer(task -> {
            if (!arrow.isValid() || arrow.isOnGround()) {
                task.cancel();
                return;
            }

            // Define the maximum distance to check for entities
            double maxDistance = 100;

            // Ray trace for entities
            RayTraceResult entityRayTraceResult = player.getWorld().rayTraceEntities(
                    player.getEyeLocation(),
                    player.getEyeLocation().getDirection(),
                    maxDistance
            );

            // Ray trace for blocks
            RayTraceResult blockRayTraceResult = player.getWorld().rayTraceBlocks(
                    player.getEyeLocation(),
                    player.getEyeLocation().getDirection(),
                    maxDistance
            );
            Location targetLocation = null;
            Location blockLocation = null;
            if(entityRayTraceResult != null && entityRayTraceResult.getHitEntity() != null) {
                if(!entityRayTraceResult.getHitEntity().equals(player) && !(entityRayTraceResult.getHitEntity().getType() == EntityType.ARROW)) {
                    Vector hitPosition = entityRayTraceResult.getHitPosition();
                    targetLocation = new Location(player.getWorld(), hitPosition.getX(), hitPosition.getY(), hitPosition.getZ());
                }
            }
            if(blockRayTraceResult != null && blockRayTraceResult.getHitBlock().isSolid()) {
                Vector hitPosition = blockRayTraceResult.getHitPosition();
                blockLocation = new Location(player.getWorld(), hitPosition.getX(), hitPosition.getY(), hitPosition.getZ());
            } 
            if(blockLocation != null && targetLocation != null) {
                if(blockLocation.distanceSquared(player.getEyeLocation()) < targetLocation.distanceSquared(player.getEyeLocation())) {
                    Vector hitPosition = blockRayTraceResult.getHitPosition();
                    targetLocation = new Location(player.getWorld(), hitPosition.getX(), hitPosition.getY(), hitPosition.getZ());
                }
            } else if(blockLocation != null && targetLocation == null) {
                Vector hitPosition = blockRayTraceResult.getHitPosition();
                targetLocation = new Location(player.getWorld(), hitPosition.getX(), hitPosition.getY(), hitPosition.getZ());
            } else if(blockLocation == null && targetLocation == null) {
                targetLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(maxDistance));
            }
            Vector direction = targetLocation.toVector().subtract(arrow.getLocation().toVector()).normalize();
            arrow.setVelocity(direction);
        }, Duration.ofMillis(50), Duration.ofSeconds(0));
    }
}
