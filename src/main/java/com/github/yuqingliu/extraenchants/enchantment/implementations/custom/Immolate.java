package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Immolate extends CustomEnchantment {
    public Immolate(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Immolate", nameColor),
            2,
            Component.text("Burns your target with eternal flames. Caster takes a quarter of the damage dealt", descriptionColor),
            registry.getMeleeApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
