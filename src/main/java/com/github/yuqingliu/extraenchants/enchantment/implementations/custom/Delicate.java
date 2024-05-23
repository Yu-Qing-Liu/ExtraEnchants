package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Delicate extends CustomEnchantment {
    public Delicate(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Delicate", nameColor),
            1,
            Component.text("Avoids breaking immature crops", descriptionColor),
            registry.getHoeApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
