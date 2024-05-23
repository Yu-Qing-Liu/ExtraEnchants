package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class AutoLooting extends CustomEnchantment {
    public AutoLooting(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text("AutoLooting", nameColor),
            1,
            Component.text("Items go directly into your inventory", descriptionColor),
            registry.getToolsWeaponsApplicable(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
