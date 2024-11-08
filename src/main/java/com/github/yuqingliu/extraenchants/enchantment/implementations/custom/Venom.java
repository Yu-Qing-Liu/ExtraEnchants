package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.HashSet;
import java.util.List;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Venom extends CustomEnchantment {
    public Venom(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.VENOM,
            Component.text("Venom", nameColor),
            Component.text("Applies poison effect for 3 seconds per level", descriptionColor),
            2,
            itemRepository.getItems().get(ItemCategory.WEAPON),
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
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();
        int poisonLevel = this.getEnchantmentLevel(item);
        if (poisonLevel > 0) {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                int effectDurationTicks = 3 * 20 * poisonLevel;
                target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, effectDurationTicks, 0));
            }
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        ItemStack bow = event.getBow();
        if (bow == null) return;
        int poisonLevel = this.getEnchantmentLevel(bow);
        if (poisonLevel > 0) {
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
                    int effectDurationTicks = 3 * 20 * poisonLevel;
                    target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, effectDurationTicks, 0));
                }
            }
        }
    }
}
