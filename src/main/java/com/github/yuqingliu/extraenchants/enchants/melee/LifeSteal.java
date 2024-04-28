package com.github.yuqingliu.extraenchants.enchants.melee;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;

public class LifeSteal implements Listener {
    private final JavaPlugin plugin;

    public LifeSteal(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            int level = UtilityMethods.getEnchantmentLevel(weapon, "LifeSteal");
            if (level > 0 && event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                double targetCurrentHealth = target.getHealth();
                double healthToSteal = targetCurrentHealth * 0.01 * level; // 1% of target's current health pool per level
                double newPlayerHealth = Math.min(player.getHealth() + healthToSteal, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                event.setDamage(event.getDamage() + Math.ceil(healthToSteal));
                player.setHealth(newPlayerHealth);
            }
        }
    }
}
