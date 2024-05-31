package com.github.yuqingliu.extraenchants.enchants.melee;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;

import java.time.Duration;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Immolate implements Listener {
    private final JavaPlugin plugin;
    private final Enchantment enchant;
    private int count = 0;
    private Particle.DustOptions beam = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 0.5f);

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        int level = enchant.getEnchantmentLevel(item);
        if (level > 0) {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                
                int duration = 3 * level;

                // Check if the target is already being damaged by this enchantment
                if (target.hasMetadata("Immolate")) {
                    return;
                }
                // Mark the target as being damaged by this enchantment
                target.setMetadata("Immolate", new FixedMetadataValue(plugin, true));
                
                applyEffect(player, target, duration);
            }
        }
    }

    private void applyEffect(Player player, LivingEntity target, int duration) {
        BukkitTask particleTask = Scheduler.runTimer(task -> {
            target.getWorld().spawnParticle(Particle.DUST, target.getLocation(), 1000, 0.25, 0.75, 0.25, 0, beam); // Ensure no extra velocity
        }, Duration.ofMillis(50), Duration.ZERO);

        Scheduler.runTimer(task -> {
            player.damage(0.25);
            target.damage(1.0, player);
            count++;
            if(count >= duration || target.isDead()) {
                target.removeMetadata("Immolate", plugin);
                task.cancel();
                particleTask.cancel();
                count = 0;
            }
        }, Duration.ofSeconds(1), Duration.ZERO);
    }
}

