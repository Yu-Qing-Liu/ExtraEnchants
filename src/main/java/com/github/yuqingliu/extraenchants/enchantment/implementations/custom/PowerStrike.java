package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class PowerStrike extends CustomEnchantment {
    private Map<UUID, HashSet<UUID>> playerEntityHits = new HashMap<>();

    public PowerStrike(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.POWER_STRIKE,
            Component.text("Power Strike", nameColor),
            Component.text("Adds 20% more damage to the first hit dealt", descriptionColor),
            5,
            itemRepository.getItems().get(ItemCategory.MELEE),
            new HashSet<>(),
            "x^2",
            "x"
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity target = event.getEntity();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            
            int level = this.getEnchantmentLevel(weapon);
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
        for (HashSet<UUID> hitSet : playerEntityHits.values()) {
            hitSet.remove(entityID);
        }
    }
}
