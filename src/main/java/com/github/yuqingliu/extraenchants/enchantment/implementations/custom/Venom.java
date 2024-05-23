package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Venom extends CustomEnchantment {
    public Venom(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Venom", nameColor),
            1,
            Component.text("Applies poison effect for 3 seconds per level", descriptionColor),
            registry.getWeaponsApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
