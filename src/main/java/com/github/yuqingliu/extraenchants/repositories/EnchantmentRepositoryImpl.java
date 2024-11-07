package com.github.yuqingliu.extraenchants.repositories;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.item.ItemImpl;

import lombok.Getter;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

@Getter
public class EnchantmentRepositoryImpl implements EnchantmentRepository {
    private final Set<Enchantment> enchantments = new LinkedHashSet<>();

    public Enchantment getEnchantment(String enchantName) {
        return enchantments.stream().filter(enchant -> PlainTextComponentSerializer.plainText().serialize(enchant.getName()).equals(enchantName)).findFirst().orElse(null);
    }

    public Enchantment[] getApplicableEnchantments(ItemStack item) {
        Item i = new ItemImpl(item);        
        Map<Enchantment, Integer> enchants = i.getEnchantments(this);
        return i.getEnchantments(this).keySet().stream().filter(enchant -> 
            enchants.get(enchant) < enchant.getMaxLevel()
        ).toArray(Enchantment[]::new);
    }
}
