package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class ProjectileProtection extends VanillaEnchantment {
    public ProjectileProtection(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.PROJECTILE_PROTECTION,
            Component.text("Projectile Protection", nameColor),
            Enchantment.PROJECTILE_PROTECTION.getMaxLevel(),
            Component.text("Reduces projectile damage.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
