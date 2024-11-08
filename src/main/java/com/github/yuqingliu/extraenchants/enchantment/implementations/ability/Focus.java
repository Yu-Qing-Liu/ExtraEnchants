package com.github.yuqingliu.extraenchants.enchantment.implementations.ability;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Focus extends AbilityEnchantment {
    public Focus(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text(" Left Click", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
            Component.text("Focus", nameColor),
            5,
            Component.text("Marks a target. Deal 20% increased damage to the target per level", descriptionColor),
            registry.getBowApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
