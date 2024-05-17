package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Focus extends CustomEnchantment {
    public Focus(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Focus", nameColor),
            1,
            Component.text("Marks a target. Deal 20% increased damage to the target per level", descriptionColor),
            registry.getBowApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
