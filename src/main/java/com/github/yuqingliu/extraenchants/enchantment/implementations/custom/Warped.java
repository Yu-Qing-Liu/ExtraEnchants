package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Warped extends CustomEnchantment {
    public Warped(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Warped", nameColor),
            1,
            Component.text("Arrows can hit enderman", descriptionColor),
            registry.getRangedApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
