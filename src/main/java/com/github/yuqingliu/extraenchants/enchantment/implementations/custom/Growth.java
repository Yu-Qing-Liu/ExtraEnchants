package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Growth extends CustomEnchantment {
    public Growth(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Growth", nameColor),
            1,
            Component.text("Adds 1 HP per level", descriptionColor),
            registry.getArmorApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
