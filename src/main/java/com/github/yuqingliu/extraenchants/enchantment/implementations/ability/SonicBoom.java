package com.github.yuqingliu.extraenchants.enchantment.implementations.ability;

import java.time.Duration;
import java.util.HashSet;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SonicBoom extends AbilityEnchantment {
    public SonicBoom(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.SONIC_BOOM,
            Component.text("Sonic Boom", nameColor),
            Component.text("Warden's Ability", descriptionColor),
            1,
            itemRepository.getItems().get(ItemCategory.SWORD),
            new HashSet<>(),
            "x^2",
            "x",
            Component.text(" Right Click", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
            Duration.ofSeconds(10)
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }
}
