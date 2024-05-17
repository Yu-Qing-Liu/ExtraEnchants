package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Snipe extends CustomEnchantment {
    public Snipe(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Snipe", nameColor),
            1,
            Component.text("Increase arrow velocity by 25% per level", descriptionColor),
            registry.getCrossbowApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
