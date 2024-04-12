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

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;

public class Venom implements Listener {
    private final JavaPlugin plugin;

    public Venom(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Retrieve the level of the Venom enchantment
        int poisonLevel = UtilityMethods.getEnchantmentLevel(item, "Venom");
        if (poisonLevel > 0) {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                
                // Calculate the effect duration: 3 seconds (60 ticks) per level of the enchantment
                int effectDurationTicks = 3 * 20 * poisonLevel;
                
                // Apply the Venom effect. PotionEffectType.WITHER uses 0-based levels (level 0 = I, level 1 = II, etc.)
                target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, effectDurationTicks, poisonLevel - 1));
            }
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        ItemStack bow = event.getBow();

        if (bow == null) return;

        int poisonLevel = UtilityMethods.getEnchantmentLevel(bow, "Venom");
        if (poisonLevel > 0) {
            // Tag the arrow with the "Venom" enchantment level
            if (event.getProjectile() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getProjectile();
                arrow.setMetadata("Venom", new FixedMetadataValue(plugin, poisonLevel));
            }
        }
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof LivingEntity) {
            Arrow arrow = (Arrow) event.getDamager();
            LivingEntity target = (LivingEntity) event.getEntity();

            if (arrow.hasMetadata("Venom")) {
                List<MetadataValue> metadata = arrow.getMetadata("Venom");
                if (!metadata.isEmpty()) {
                    int poisonLevel = metadata.get(0).asInt();

                    // Calculate the effect duration: 3 seconds (60 ticks) per level of the enchantment
                    int effectDurationTicks = 3 * 20 * poisonLevel;

                    // Apply the Venom effect to the target
                    target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, effectDurationTicks, poisonLevel - 1));
                }
            }
        }
    }
}

