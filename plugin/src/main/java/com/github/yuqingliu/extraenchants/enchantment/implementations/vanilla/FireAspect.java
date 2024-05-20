package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class FireAspect extends VanillaEnchantment {
    public FireAspect(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.FIRE_ASPECT,
            Component.text("Fire Aspect", nameColor),
            Enchantment.FIRE_ASPECT.getMaxLevel(),
            Component.text("Sets target on fire.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
