package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class SonicBoom extends CustomEnchantment {
    public SonicBoom(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Sonic Boom", nameColor),
            1,
            Component.text("Warden's Ability", descriptionColor),
            registry.getMeleeApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
