package com.github.yuqingliu.extraenchants.enchants.weapons;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Arrow;

import java.util.List;

import com.github.yuqingliu.extraenchants.enchantment.Enchantment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Wither implements Listener {
    private final JavaPlugin plugin;
    private final Enchantment enchant;

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Retrieve the level of the Wither enchantment
        int witherLevel = enchant.getEnchantmentLevel(item);
        if (witherLevel > 0) {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                
                // Calculate the effect duration: 3 seconds (60 ticks) per level of the enchantment
                int effectDurationTicks = 3 * 20 * witherLevel;
                
                // Apply the Wither effect. PotionEffectType.WITHER uses 0-based levels (level 0 = I, level 1 = II, etc.)
                target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, effectDurationTicks, 0));
            }
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        ItemStack bow = event.getBow();

        if (bow == null) return;

        int witherLevel = enchant.getEnchantmentLevel(bow);
        if (witherLevel > 0) {
            // Tag the arrow with the "Wither" enchantment level
            if (event.getProjectile() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getProjectile();
                arrow.setMetadata("Wither", new FixedMetadataValue(plugin, witherLevel));
            }
        }
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof LivingEntity) {
            Arrow arrow = (Arrow) event.getDamager();
            LivingEntity target = (LivingEntity) event.getEntity();

            if (arrow.hasMetadata("Wither")) {
                List<MetadataValue> metadata = arrow.getMetadata("Wither");
                if (!metadata.isEmpty()) {
                    int witherLevel = metadata.get(0).asInt();

                    // Calculate the effect duration: 3 seconds (60 ticks) per level of the enchantment
                    int effectDurationTicks = 3 * 20 * witherLevel;

                    // Apply the Wither effect to the target
                    target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, effectDurationTicks, 0));
                }
            }
        }
    }
}
