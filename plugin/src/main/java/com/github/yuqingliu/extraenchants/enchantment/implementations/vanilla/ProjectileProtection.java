package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class ProjectileProtection extends VanillaEnchantment {
    public ProjectileProtection(TextColor nameColor) {
        super(
            Enchantment.PROTECTION_PROJECTILE,
            Component.text("Projectile Protection", nameColor),
            Enchantment.PROTECTION_PROJECTILE.getMaxLevel(),
            Component.text(Enchantment.PROTECTION_PROJECTILE.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
