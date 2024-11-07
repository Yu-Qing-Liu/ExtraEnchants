package com.github.yuqingliu.extraenchants.repositories;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.managers.ColorManager;
import com.github.yuqingliu.extraenchants.api.managers.LoreManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.managers.TextManager;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.Homing;
import com.github.yuqingliu.extraenchants.item.ItemImpl;
import com.google.inject.Singleton;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
@Singleton
public class EnchantmentRepositoryImpl implements EnchantmentRepository {
    private final Set<Enchantment> enchantments = new LinkedHashSet<>();
    private final ItemRepository itemRepository;
    private final TextManager textManager;
    private final LoreManager loreManager;
    private final ColorManager colorManager;
    private final NameSpacedKeyManager keyManager;
    private final NamedTextColor vanilla = NamedTextColor.BLUE;
    private final NamedTextColor custom = NamedTextColor.BLUE;
    private final NamedTextColor ability = NamedTextColor.GOLD;
    private final NamedTextColor descriptionColor = NamedTextColor.DARK_GRAY;
    
    public EnchantmentRepositoryImpl(ItemRepository itemRepository, TextManager textManager, LoreManager loreManager, ColorManager colorManager, NameSpacedKeyManager keyManager) {
        this.itemRepository = itemRepository;
        this.textManager = textManager;
        this.loreManager = loreManager;
        this.colorManager = colorManager;
        this.keyManager = keyManager;
        initialize();
    }

    private void initialize() {
        enchantments.add(new Homing(textManager, loreManager, colorManager, keyManager, this, itemRepository, custom, descriptionColor));
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
    public Enchantment[] getApplicableEnchantments(ItemStack item) {
        Map<Enchantment, Integer> itemEnchants = new ItemImpl(item).getEnchantments(this);
        return enchantments.stream().filter(enchant -> 
            enchant.canEnchant(item) && (itemEnchants.containsKey(enchant) ? (itemEnchants.get(enchant) < enchant.getMaxLevel()) : true)
        ).toArray(Enchantment[]::new);
    }
}
