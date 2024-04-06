package com.github.yuqingliu.extraenchants.enchants.armor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Sound;
import com.github.yuqingliu.extraenchants.utils.UtilityMethods;

public class Mitigation implements Listener {
    private final JavaPlugin plugin;

    public Mitigation(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack[] armors = player.getInventory().getArmorContents();
            
            int totalMitigationLevels = 0;
            for (ItemStack armor : armors) {
                if (armor != null) {
                    int mitigationLevel = UtilityMethods.getEnchantmentLevel(armor, "Mitigation");
                    totalMitigationLevels += mitigationLevel;
                }
            }
            
            // Calculate the total chance to negate damage
            double chanceToNegate = totalMitigationLevels * 0.005;
            if (Math.random() < chanceToNegate) {
                event.setCancelled(true); // Negate the damage
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            }
        }
    }
}

