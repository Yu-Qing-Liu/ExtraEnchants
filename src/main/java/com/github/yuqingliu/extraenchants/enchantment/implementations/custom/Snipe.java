package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Snipe extends CustomEnchantment {
    public Snipe(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Snipe", nameColor),
            4,
            Component.text("Increase arrow velocity by 25% per level", descriptionColor),
            registry.getCrossbowApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
