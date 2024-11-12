package com.github.yuqingliu.extraenchants.enchantment.implementations.ability;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;
import com.github.yuqingliu.extraenchants.weapon.implementations.MeleeWeapon;
import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.cooldown.Cooldown;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.api.weapon.Weapon;
import com.github.yuqingliu.extraenchants.cooldown.CooldownImpl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Wave extends AbilityEnchantment {
    private Map<UUID, int[]> shots = new ConcurrentHashMap<>();
    private final int damage = 2;

    public Wave(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.WAVE,
            Component.text("Wave", nameColor),
            Component.text("Fire seeking water projectiles towards clicked target.", descriptionColor),
            5,
            itemRepository.getItems().get(ItemCategory.TRIDENT),
            new HashSet<>(),
            "x^2",
            "x",
            Component.text(" Left Click", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
            Duration.ofSeconds(1)
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
        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
            ItemStack trident = player.getInventory().getItemInMainHand();
            RayTraceResult rayTraceResult = player.rayTraceEntities(50);
            if (rayTraceResult != null && rayTraceResult.getHitEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) rayTraceResult.getHitEntity();
                Cooldown cooldown = cooldownManager.computeIfAbsent(player.getUniqueId(), new CooldownImpl(this.id.name(), this.cooldown));
                long remainingTime = cooldown.getRemainingSeconds();
                if(remainingTime <= 0) {
                    fireProjectiles(player, target, trident);
                    cooldown.start();
                } else {
                    player.sendMessage("Wave is on cooldown. Please wait " + remainingTime + " more seconds.");
                }
            } else {
                player.sendMessage("Not a valid target");
            }
        }
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof LivingEntity) {
            Arrow arrow = (Arrow) event.getDamager();
            LivingEntity target = (LivingEntity) event.getEntity();
            if (arrow.hasMetadata("Wave")) {
                List<MetadataValue> metadata = arrow.getMetadata("Wave");
                if (!metadata.isEmpty()) {
                    event.setCancelled(true);
                    Player player = (Player) arrow.getShooter();
                    ItemStack trident = player.getInventory().getItemInMainHand();
                    Weapon weapon = new MeleeWeapon(trident);
                    weapon.applyHit(player, target);
                    arrow.remove();
                    target.setNoDamageTicks(0);
                }
            }
        }
    }

    private void fireProjectiles(Player player, Entity target, ItemStack weapon) {
        shots.put(player.getUniqueId(), new int[]{0});
        int level = this.getEnchantmentLevel(weapon);
        Scheduler.runTimer(task -> {
            fireProjectile(player, target, level);
            shots.get(player.getUniqueId())[0]++;
            if(shots.get(player.getUniqueId())[0] >= level) {
                task.cancel();
                shots.get(player.getUniqueId())[0] = 0;
            }
        }, Duration.ofMillis(100), Duration.ZERO);
    }

    private void fireProjectile(Player player, Entity target, int level) {
        soundManager.playWaveSound(player);
        Location startLocation = mathManager.getRightSide(player.getEyeLocation(), 0.45).subtract(0, 0.2, 0);
        Vector direction = mathManager.getRandomizedDirection(player.getEyeLocation().getDirection(), 1.0);
        Arrow arrow = player.getWorld().spawn(startLocation, Arrow.class);
        arrow.setMetadata("Wave", new FixedMetadataValue(plugin, level));
        arrow.setShooter(player);
        arrow.setVelocity(direction);
        arrow.setInvisible(true);
        setHomingProjectile(player, arrow, target);
    }

    private void setHomingProjectile(Player player, Arrow arrow, Entity target) {
        arrow.setGravity(false);
        Scheduler.runTimer(task -> {
            if (!arrow.isValid() || arrow.isOnGround()) {
                arrow.remove();
                task.cancel();
                return;
            }
            Location targetLocation = mathManager.getRandomPointOnEntityHitbox(target).toLocation(player.getWorld());
            Vector direction = targetLocation.toVector().subtract(arrow.getLocation().toVector()).normalize();
            arrow.setVelocity(direction);
            Location arrowLocation = arrow.getLocation();
            arrow.getWorld().spawnParticle(Particle.DOLPHIN, arrowLocation, 10, 0.1, 0.1, 0.1, 0);
        }, Duration.ofMillis(100), Duration.ofMillis(100));
    }
}
