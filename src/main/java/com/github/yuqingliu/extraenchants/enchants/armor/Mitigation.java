package com.github.yuqingliu.extraenchants.enchants.armor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Sound;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Mitigation implements Listener {
    private final Enchantment enchant;

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack[] armors = player.getInventory().getArmorContents();
            
            int totalMitigationLevels = 0;
            for (ItemStack armor : armors) {
                if (armor != null) {
                    int mitigationLevel = enchant.getEnchantmentLevel(armor);
                    totalMitigationLevels += mitigationLevel;
                }
            }
            
            // Calculate the total chance to negate damage
            double chanceToNegate = totalMitigationLevels * 0.01;
            if (Math.random() < chanceToNegate) {
                event.setCancelled(true); // Negate the damage
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            }
        }
    }
}

