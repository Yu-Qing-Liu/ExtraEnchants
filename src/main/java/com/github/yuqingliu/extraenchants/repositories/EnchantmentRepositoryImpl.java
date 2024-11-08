package com.github.yuqingliu.extraenchants.repositories;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.*;
import com.google.inject.Singleton;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
@Singleton
public class EnchantmentRepositoryImpl implements EnchantmentRepository {
    private final Set<Enchantment> enchantments = new LinkedHashSet<>();
    private final ItemRepository itemRepository;
    private final ManagerRepository managerRepository;
    private final NamedTextColor vanilla = NamedTextColor.BLUE;
    private final NamedTextColor custom = NamedTextColor.BLUE;
    private final NamedTextColor ability = NamedTextColor.GOLD;
    private final NamedTextColor descriptionColor = NamedTextColor.DARK_GRAY;
    
    public EnchantmentRepositoryImpl(ManagerRepository managerRepository, ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        this.managerRepository = managerRepository;
        initialize();
        postConstruct();
    }

    private void initialize() {
        // Vanilla enchants
        // Custom enchants
        enchantments.add(new AutoLooting(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Delicate(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Growth(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Homing(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Immolate(managerRepository, this, itemRepository, custom, descriptionColor));
        // Ability enchants
    }

    private void postConstruct() {
        enchantments.forEach(enchant -> enchant.postConstruct());
    }

    @Override
    public Set<Enchantment> getEnchantments(Set<EnchantID> ids) {
        return enchantments.stream().filter(enchant -> ids.contains(enchant.getId())).collect(Collectors.toSet());
    }
    
    @Override
    public Enchantment getEnchantment(EnchantID id) {
        return enchantments.stream().filter(enchant -> enchant.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments(ItemStack item) {
        Map<Enchantment, Integer> enchants = new HashMap<>();
        getEnchantments().stream().forEach(enchant -> {
            if(enchant.getEnchantmentLevel(item) > 0) {
                enchants.put(enchant, enchant.getEnchantmentLevel(item));
            }
        });
        return enchants;
    }
    
    @Override
    public Enchantment[] getApplicableEnchantments(ItemStack item) {
        Map<Enchantment, Integer> itemEnchants = getEnchantments(item);
        return enchantments.stream().filter(enchant -> 
            enchant.canEnchant(item) && (itemEnchants.containsKey(enchant) ? (itemEnchants.get(enchant) < enchant.getMaxLevel()) : true)
        ).toArray(Enchantment[]::new);
    }
}
