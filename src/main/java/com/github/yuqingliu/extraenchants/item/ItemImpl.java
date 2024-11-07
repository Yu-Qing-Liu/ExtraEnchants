package com.github.yuqingliu.extraenchants.item;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ItemImpl implements Item {
    private final ItemStack itemStack;
    
    @Override
    public Map<Enchantment, Integer> getEnchantments(EnchantmentRepository enchantmentRepository) {
        Map<Enchantment, Integer> enchants = new HashMap<>();
        enchantmentRepository.getEnchantments().stream().forEach(enchant -> {
            if(enchant.getEnchantmentLevel(itemStack) > 0) {
                enchants.put(enchant, enchant.getEnchantmentLevel(itemStack));
            }
        });
        return enchants;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ItemImpl other = (ItemImpl) obj;
        return this.itemStack.getType().equals(other.getItemStack().getType()) && this.itemStack.displayName().equals(other.getItemStack().displayName());
    }

    @Override
    public int hashCode() {
        return itemStack != null ? itemStack.hashCode() : 0;
    }
}
