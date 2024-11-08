package com.github.yuqingliu.extraenchants.enchantment.implementations.ability;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;

import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Focus extends AbilityEnchantment {
    public Focus(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.FOCUS,
            Component.text("Focus", nameColor),
            Component.text("Marks a target. Deal 20% increased damage to the target per level", descriptionColor),
            5,
            itemRepository.getItems().get(ItemCategory.BOW),
            new HashSet<>(),
            "x^2",
            "x",
            Component.text(" Left Click", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
            Duration.ZERO
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }
}
