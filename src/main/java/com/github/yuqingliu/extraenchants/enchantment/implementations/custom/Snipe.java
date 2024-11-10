package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.weapon.implementations.RangedWeapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Snipe extends CustomEnchantment {
    public Snipe(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.SNIPE,
            Component.text("Snipe", nameColor),
            Component.text("Increase arrow velocity by 25% per level", descriptionColor),
            4,
            itemRepository.getItems().get(ItemCategory.CROSSBOW),
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
    public void onCrossBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        ItemStack bow = event.getBow();
        if (bow == null || bow.getType() != Material.CROSSBOW) return;
        int level = this.getEnchantmentLevel(bow);
        if (level > 0 && event.getProjectile() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getProjectile();
            setArrowSpeed(arrow, level);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                ItemStack bow = shooter.getInventory().getItemInMainHand();
                if (bow.getType() == Material.CROSSBOW && this.getEnchantmentLevel(bow) > 0) {
                    RangedWeapon weapon = new RangedWeapon(bow);
                    double originalDamage = weapon.getDamage();
                    event.setDamage(originalDamage);
                }
            }
        }
    }

    private void setArrowSpeed(Arrow arrow, int level) {
        double speedMultiplier = 1.0 + (0.25 * level);
        Vector velocity = arrow.getVelocity();
        velocity.multiply(speedMultiplier);
        arrow.setVelocity(velocity);
    }
}
