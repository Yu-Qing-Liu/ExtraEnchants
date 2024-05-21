package com.github.yuqingliu.extraenchants.enchants.melee;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.enchantment.Enchantment;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

@RequiredArgsConstructor
public class PowerStrike implements Listener {
    private final Enchantment enchant;
    private HashMap<UUID, HashSet<UUID>> playerEntityHits = new HashMap<>();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity target = event.getEntity();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            
            int level = enchant.getEnchantmentLevel(weapon);
            if (level > 0) {
                UUID playerId = player.getUniqueId();
                UUID targetId = target.getUniqueId();
                
                HashSet<UUID> entitiesHit = playerEntityHits.getOrDefault(playerId, new HashSet<>());
                
                if (!entitiesHit.contains(targetId)) {
                    double extraDamage = event.getDamage() * 0.20 * level;
                    event.setDamage(event.getDamage() + extraDamage);
                    entitiesHit.add(targetId);
                    playerEntityHits.put(playerId, entitiesHit);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        UUID entityID = entity.getUniqueId();

        // Remove this entity from all player tracking sets
        for (HashSet<UUID> hitSet : playerEntityHits.values()) {
            hitSet.remove(entityID);
        }
    }
}
