package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Smelting extends CustomEnchantment {
    public Smelting(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Smelting", nameColor),
            1,
            Component.text("Smelts mined ores", descriptionColor),
            registry.getPickaxeApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
