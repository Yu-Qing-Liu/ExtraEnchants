package com.github.yuqingliu.extraenchants.enchantment.implementations.ability;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;
import com.github.yuqingliu.extraenchants.weapon.implementations.RangedWeapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class RapidFire extends AbilityEnchantment {
    private Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    private int shots;

    public RapidFire(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.RAPID_FIRE,
            Component.text("Rapid Fire", nameColor),
            Component.text("Shoots a barrage of arrows", descriptionColor),
            1,
            itemRepository.getItems().get(ItemCategory.CROSSBOW),
            new HashSet<>(),
            "x^2",
            "x",
            Component.text(" Left Click", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
            Duration.ofSeconds(10)
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && player.getInventory().getItemInMainHand().getType() == Material.CROSSBOW) {
            ItemStack crossbow = player.getInventory().getItemInMainHand();
            if (this.getEnchantmentLevel(crossbow) > 0) {
                long remainingTime = getRemainingCooldownTime(player);
                if (remainingTime <= 0) {
                    fireProjectiles(player, crossbow);
                    cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                } else {
                    player.sendMessage("Rapid Fire is on cooldown. Please wait " + remainingTime + " more seconds.");
                }
            }
        }
    }

    private long getRemainingCooldownTime(Player player) {
        long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        long elapsed = System.currentTimeMillis() - lastUsed;
        return cooldown.minus(elapsed, ChronoUnit.MILLIS).toSeconds();
    }

    private void fireProjectiles(Player player, ItemStack weapon) {
        Scheduler.runTimer(task -> {
            fireParticleBeam(player, weapon);
            soundManager.playArrowSound(player);
            shots++;
            if(shots >= 5) {
                task.cancel();
                shots = 0;
            }
        }, Duration.ofMillis(100), Duration.ZERO);
    }

    private Location getRightSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    private Location calculateFinalBeamLocation(Player player, Location startLocation, Vector direction, double beamRange) {
        for (double i = 0; i <= beamRange; i += 0.1) {
            Location point = startLocation.clone().add(direction.clone().multiply(i));
            if (point.getBlock().getType().isSolid()) {
                return point;
            }
        }
        return startLocation.clone().add(direction.multiply(beamRange));
    }

    private void fireParticleBeam(Player player, ItemStack weapon) {
        Location startLocation = getRightSide(player.getEyeLocation(), 0.45).subtract(0, 0.2, 0);
        Vector direction = getRandomizedDirection(player.getEyeLocation().getDirection(), 0.05);
        double beamRange = 30.0;
        Location finalLocation = calculateFinalBeamLocation(player, startLocation, direction, beamRange);
        RayTraceResult result = player.getWorld().rayTraceEntities(startLocation, direction, beamRange, 0.5, entity -> (entity instanceof LivingEntity && entity != player));
        Vector finalDirection = finalLocation.toVector().subtract(startLocation.toVector()).normalize();
        for (double i = 0; i <= finalLocation.distance(startLocation); i += 0.1) {
            Location point = startLocation.clone().add(finalDirection.clone().multiply(i));
            player.getWorld().spawnParticle(Particle.CRIT, point, 1, 0.01, 0.01, 0.01, 0.01);
        }
        if (result != null && result.getHitEntity() != null) {
            LivingEntity target = (LivingEntity) result.getHitEntity();
            RangedWeapon crossbow = new RangedWeapon(weapon);
            crossbow.applyHit(player, target);
            target.setNoDamageTicks(0);
        }
    }

    private Vector getRandomizedDirection(Vector originalDirection, double spread) {
        Random random = new Random();
        double offsetX = (random.nextDouble() - 0.5) * spread;
        double offsetY = (random.nextDouble() - 0.5) * spread;
        double offsetZ = (random.nextDouble() - 0.5) * spread;
        return originalDirection.add(new Vector(offsetX, offsetY, offsetZ)).normalize();
    }
}
