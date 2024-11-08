package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.HashSet;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.weapon.implementations.RangedWeapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Warped extends CustomEnchantment {
    public Warped(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.WARPED,
            Component.text("Warped", nameColor),
            Component.text("Arrows can hit enderman", descriptionColor),
            1,
            itemRepository.getItems().get(ItemCategory.RANGED),
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
        int enchantmentLevel = this.getEnchantmentLevel(weapon);
        if (enchantmentLevel > 0) {
            Entity enderman = event.getHitEntity();
            if(enderman != null && enderman.getType() == EntityType.ENDERMAN) {
                event.setCancelled(true);
                arrow.remove();
                Enderman entity = (Enderman) enderman;
                RangedWeapon item = new RangedWeapon(weapon);
                item.applyHit(player, entity);
            }
        }
    }
}
