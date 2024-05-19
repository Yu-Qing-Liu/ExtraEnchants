package com.github.yuqingliu.extraenchants.enchantment.implementations.ability;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class RapidFire extends CustomEnchantment {
    public RapidFire(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Rapid Fire", nameColor),
            1,
            Component.text("Shoots a barrage of projectile", descriptionColor),
            registry.getCrossbowApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
