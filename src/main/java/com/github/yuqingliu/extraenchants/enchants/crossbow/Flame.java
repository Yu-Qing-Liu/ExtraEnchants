package com.github.yuqingliu.extraenchants.enchants.crossbow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;

public class Flame implements Listener {
    private final JavaPlugin plugin;

    public Flame (JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCrossBowShoot(EntityShootBowEvent event) {
        // Check if the shooter is a player
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        ItemStack crossbow = event.getBow();
        
        // Ensure the item is a crossbow and has the custom Flame enchantment
        if (crossbow != null && crossbow.getType() == Material.CROSSBOW) {
            // Check if the projectile is an arrow
            if(crossbow.hasItemMeta() && crossbow.getItemMeta().hasEnchant(Enchantment.ARROW_FIRE)) {
                if (event.getProjectile() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getProjectile();
                    // Set the arrow on fire for 1000 ticks (50 seconds)
                    arrow.setFireTicks(1000);
                }
            }
        }
    }
}

