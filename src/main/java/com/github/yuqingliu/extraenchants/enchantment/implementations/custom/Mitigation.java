package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Mitigation extends CustomEnchantment {
    public Mitigation(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Mitigation", nameColor),
            1,
            Component.text("1% chance to negate damage per level", descriptionColor),
            registry.getArmorApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
