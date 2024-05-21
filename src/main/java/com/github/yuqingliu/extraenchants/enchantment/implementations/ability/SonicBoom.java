package com.github.yuqingliu.extraenchants.enchantment.implementations.ability;

import java.time.Duration;
import java.util.ArrayList;

import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SonicBoom extends AbilityEnchantment {
    public SonicBoom(TextColor nameColor, TextColor descriptionColor, ApplicableItemsRegistry registry) {
        super(
            Component.text(" Right Click", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
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
