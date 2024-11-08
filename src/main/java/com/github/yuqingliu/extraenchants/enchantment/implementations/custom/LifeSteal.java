package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.HashSet;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class LifeSteal extends CustomEnchantment {
    public LifeSteal(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.LIFESTEAL,
            Component.text("Life Steal", nameColor),
            Component.text("Steals 1% of the victim's total health per level", descriptionColor),
            2,
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
            ItemStack weapon = player.getInventory().getItemInMainHand();
            int level = this.getEnchantmentLevel(weapon);
            if (level > 0 && event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                double targetCurrentHealth = target.getHealth();
                double healthToSteal = targetCurrentHealth * 0.01 * level;
                double newPlayerHealth = Math.min(player.getHealth() + healthToSteal, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                event.setDamage(event.getDamage() + Math.ceil(healthToSteal));
                player.setHealth(newPlayerHealth);
            }
        }
    }
}
