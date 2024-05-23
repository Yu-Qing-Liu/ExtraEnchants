package com.github.yuqingliu.extraenchants.api.item;

import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

public interface ItemUtils {
    @NotNull Map<Enchantment, Integer> getEnchantments(ItemStack item);

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
