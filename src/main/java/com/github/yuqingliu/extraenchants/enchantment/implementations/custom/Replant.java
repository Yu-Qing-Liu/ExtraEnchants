package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Replant extends CustomEnchantment {
    public Replant(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Replant", nameColor),
            1,
            Component.text("Replants mature crops", descriptionColor),
            registry.getHoeApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
