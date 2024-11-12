package com.github.yuqingliu.extraenchants.enchantment.implementations.ability;

import java.time.Duration;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.cooldown.Cooldown;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.cooldown.CooldownImpl;
import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;
import com.github.yuqingliu.extraenchants.weapon.implementations.MeleeWeapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SonicBoom extends AbilityEnchantment {
    public SonicBoom(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.SONIC_BOOM,
            Component.text("Sonic Boom", nameColor),
            Component.text("Warden's Ability", descriptionColor),
            1,
            itemRepository.getItems().get(ItemCategory.SWORD),
            new HashSet<>(),
            "x^2",
            "x",
            Component.text(" Right Click", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
            Duration.ofSeconds(10)
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(item != null) {
                int SonicBoomLevel = this.getEnchantmentLevel(item);
                if(SonicBoomLevel > 0) {
                    Cooldown cooldown = cooldownManager.computeIfAbsent(player.getUniqueId(), new CooldownImpl(this.id.name(), this.cooldown));
                    long remainingTime = cooldown.getRemainingSeconds();
                    if (remainingTime <= 0) {
                        fireSonicBoom(player);
                        cooldown.start();
                    } else {
                        player.sendMessage("Sonic Boom is on cooldown. Please wait " + remainingTime + " more seconds.");
                    }
                }
            }
        }
    }

    private void fireSonicBoom(Player player) {
        Location eye = player.getEyeLocation();
        Vector direction = eye.getDirection();
        int distance = 30;
        int teleportDelay = 500;
        int damageDelay = 250;
        for (int i = 0; i < distance; i++) {
            Location step = eye.add(direction);
            player.getWorld().spawnParticle(Particle.SONIC_BOOM, step, 1);
            soundManager.playSonicBoomSound(player);
            RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(eye, direction, i);
            if (rayTraceResult != null && rayTraceResult.getHitEntity() != null && !rayTraceResult.getHitEntity().equals(player)) {
                Entity hitEntity = rayTraceResult.getHitEntity();
                Location hitEntityLocation = hitEntity.getLocation();
                delayedTeleportPlayer(player, hitEntityLocation, teleportDelay);
                delayedDamage(hitEntity, player, damageDelay);
                break;
            } else if (step.getBlock().getType().isSolid()) {
                Location hitLocation = step.getBlock().getLocation();
                delayedTeleportPlayer(player, hitLocation, teleportDelay);
                break;
            } else if (i == distance - 1) {
                Location hitLocation = step.getBlock().getLocation();
                delayedTeleportPlayer(player, hitLocation, teleportDelay);
            }
        }
    }

    private void delayedTeleportPlayer(Player player, Location location, int delay) {
        if (location != null) {
            float currentYaw = player.getLocation().getYaw();
            float currentPitch = player.getLocation().getPitch();
            Location teleportLocation = location.clone();
            teleportLocation.setYaw(currentYaw);
            teleportLocation.setPitch(currentPitch);
            Scheduler.runLater(task -> {
                player.teleport(teleportLocation.add(0, 1, 0));
                player.getWorld().createExplosion(teleportLocation, 4.0F, false, false, player);
                player.getWorld().spawnParticle(Particle.EXPLOSION, teleportLocation, 1);
            }, Duration.ofMillis(delay));
        }
    }

    private void delayedDamage(Entity entity, Player player, int delay) {
        ItemStack weapon = player.getInventory().getItemInMainHand();
        MeleeWeapon item = new MeleeWeapon(weapon);
        Scheduler.runLater(task -> {
            LivingEntity livingEntity = (LivingEntity) entity;
            item.applyHit(player, livingEntity);
        }, Duration.ofMillis(delay));
    }
}
