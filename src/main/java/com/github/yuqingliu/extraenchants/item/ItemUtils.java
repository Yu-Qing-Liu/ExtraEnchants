package com.github.yuqingliu.extraenchants.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.ExtraEnchants;
import com.github.yuqingliu.extraenchants.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentManager;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemUtils {
    private EnchantmentManager enchantmentManager;

    public ItemUtils(ExtraEnchants plugin) {
        this.enchantmentManager = plugin.getEnchantmentManager();
    }

    public Map<Enchantment, Integer> getEnchantments(ItemStack item) {
        Collection<Enchantment> enchantsRegistry = enchantmentManager.getEnchantments().values();
        Map<Enchantment, Integer> itemEnchants = new HashMap<>();

        for (Enchantment enchantment : enchantsRegistry) {
            int level = enchantment.getEnchantmentLevel(item);
            if(level > 0) {
                itemEnchants.put(enchantment, level);
            }
        }
        return itemEnchants;
    }

    public enum Rarity {
        COMMON, UNCOMMON, RARE, EPIC, LEGENDARY,
        MYTHIC, DIVINE, SPECIAL;
        
        public TextColor getColor() {
            return switch (this) {
                case COMMON -> NamedTextColor.WHITE;
                case UNCOMMON -> NamedTextColor.GREEN;
                case RARE -> NamedTextColor.BLUE;
                case EPIC -> NamedTextColor.DARK_PURPLE;
                case LEGENDARY -> NamedTextColor.GOLD;
                case MYTHIC -> NamedTextColor.LIGHT_PURPLE;
                case DIVINE -> NamedTextColor.AQUA;
                case SPECIAL -> NamedTextColor.RED;
            };
        }
    }
}
