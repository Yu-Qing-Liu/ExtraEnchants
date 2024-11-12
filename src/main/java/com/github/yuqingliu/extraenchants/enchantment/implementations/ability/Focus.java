package com.github.yuqingliu.extraenchants.enchantment.implementations.ability;

import java.time.Duration;
import java.util.HashSet;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;
import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Focus extends AbilityEnchantment {
    private Particle.DustOptions beam = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 0.1F);

    public Focus(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.FOCUS,
            Component.text("Focus", nameColor),
            Component.text("Marks a target. Deal 20% increased damage to the target per level", descriptionColor),
            5,
            itemRepository.getItems().get(ItemCategory.BOW),
            new HashSet<>(),
            "x^2",
            "x",
            Component.text(" Left Click", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
            Duration.ZERO
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }

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
                        int level = this.getEnchantmentLevel(bow);
                        double extraDamage = event.getDamage() * 0.20 * level;
                        event.setDamage(event.getDamage() + extraDamage);
                    }
                }
            }
        } 
    }

    private void markEntity(Player player, Entity target) {
        String markKey = "MarkedBy:" + player.getUniqueId().toString();
        clearPreviousMark(player);
        target.setMetadata(markKey, new FixedMetadataValue(plugin, true));
    }

    private void clearPreviousMark(Player player) {
        String playerMarkKey = "MarkedBy:" + player.getUniqueId().toString();
        for (LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (entity.hasMetadata(playerMarkKey)) {
                entity.removeMetadata(playerMarkKey, plugin);
            }
        }
    }

    private void startBeamTask(Player player, Entity target) {
        Scheduler.runTimer(task -> {
            if (!target.isValid() || target.isDead()) {
                task.cancel();
                return;
            }
            String markKey = "MarkedBy:" + player.getUniqueId().toString();
            if (!target.hasMetadata(markKey)) {
                task.cancel();
                return;
            }
            if(!(this.getEnchantmentLevel(player.getEquipment().getItemInMainHand()) > 0)) {
                target.removeMetadata(markKey, plugin);
                task.cancel();
                return;
            }
            Location rightHand = mathManager.getRightSide(player.getEyeLocation(), 0.45).subtract(0, .2, 0);
            double height = target.getHeight();
            Location targetLocation = target.getLocation().add(0, height/2.0, 0); // Center the beam a bit better on the target
            Vector direction = targetLocation.toVector().subtract(rightHand.toVector()).normalize();
            if(!targetLocation.getWorld().equals(rightHand.getWorld())) {
                target.removeMetadata(markKey, plugin);
                task.cancel();
                return;
            }
            double distance = rightHand.distance(targetLocation);
            if (distance > 50) {
                target.removeMetadata(markKey, plugin);
                task.cancel();
                return;
            }
            RayTraceResult blockRayTraceResult = player.getWorld().rayTraceBlocks(rightHand, targetLocation.toVector().subtract(rightHand.toVector()).normalize(), rightHand.distance(targetLocation));
            if (blockRayTraceResult != null && blockRayTraceResult.getHitBlock() != null && blockRayTraceResult.getHitBlock().getType().isSolid()) {
                target.removeMetadata(markKey, plugin);
                task.cancel();
                return;
            }
            for (double d = 0; d <= distance; d += 0.025) {
                Location point = rightHand.clone().add(direction.clone().multiply(d));
                Scheduler.runSync(t -> {
                    player.getWorld().spawnParticle(Particle.DUST, point, 0, 0, 0, 0, 0, beam);
                });
            }
        }, Duration.ofMillis(50), Duration.ZERO);
    }
}
