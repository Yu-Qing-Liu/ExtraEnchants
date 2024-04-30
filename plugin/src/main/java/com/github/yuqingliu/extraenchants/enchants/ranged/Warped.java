package com.github.yuqingliu.extraenchants.enchants.ranged;

import org.bukkit.entity.Player;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;
import com.github.yuqingliu.extraenchants.enchants.weapons.Weapon;

public class Warped implements Listener {
    private final JavaPlugin plugin;

    public Warped(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onArrowApproachEnderman(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow) event.getEntity();
        if (!(arrow.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player) arrow.getShooter();
        ItemStack weapon = player.getInventory().getItemInMainHand();
        int enchantmentLevel = UtilityMethods.getEnchantmentLevel(weapon, "Warped");
        if (enchantmentLevel > 0) {
            Entity enderman = event.getHitEntity();
            if(enderman != null && enderman.getType() == EntityType.ENDERMAN) {
                event.setCancelled(true); // Cancel the arrow impact event to prevent it from bouncing
                arrow.remove(); // Removes the arrow to prevent it from bouncing back
                Enderman entity = (Enderman) enderman;
                double speedPerTick = 63 / 20.0;
                Vector terminalVelocity = new Vector(0, -speedPerTick, 0);
                Weapon.applyHit(player, entity, weapon, terminalVelocity, true);
            }
        }
    }
}
