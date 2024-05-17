package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class PowerStrike extends CustomEnchantment {
    public PowerStrike(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("Power Strike", nameColor),
            1,
            Component.text("Adds 20% more damage to the first hit dealt", descriptionColor),
            registry.getMeleeApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
