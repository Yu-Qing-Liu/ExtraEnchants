package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class LifeSteal extends CustomEnchantment {
    public LifeSteal(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Life Steal", nameColor),
            1,
            Component.text("Steals 1% of the victim's total health per level", descriptionColor),
            registry.getMeleeApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
