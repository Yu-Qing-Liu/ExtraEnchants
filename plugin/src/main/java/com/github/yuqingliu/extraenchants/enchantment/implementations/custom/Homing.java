package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Homing extends CustomEnchantment {
    public Homing(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Homing", nameColor),
            1,
            Component.text("Guided arrows", descriptionColor),
            registry.getBowApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
