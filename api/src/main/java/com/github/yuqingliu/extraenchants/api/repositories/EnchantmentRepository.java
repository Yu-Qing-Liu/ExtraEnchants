package com.github.yuqingliu.extraenchants.api.repositories;

import java.util.Set;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

public interface EnchantmentRepository {
    public enum EnchantID {
        AQUA_INFINITY, BANE_OF_ARTHROPODS, BINDING_CURSE, BLAST_PROTECTION, CHANNELING, DEPTH_STRIDER, EFFICIENCY, FEATHER_FALLING, FIRE_ASPECT,
        FIRE_PROTECTION, FLAME, FORTUNE, FROST_WALKER, IMPALING, INFINITY, KNOCKBACK, LOOTING, LOYALTY, LUCK_OF_THE_SEA, LURE, MENDING,
        MULTISHOT, PIERCING, POWER, PROJECTILE_PROTECTION, PROTECTION, PUNCH, QUICKCHARGE, RESPIRATION, RIPTIDE, SHARPNESS,
        SILK_TOUCH, SMITE, SOULSPEED, SWEEPING_EDGE, SWIFTSNEAK, THORNS, UNBREAKING, VANISHING_CURSE,

        AUTO_LOOTING, DELICATE, GROWTH, HOMING, IMMOLATE, LIFESTEAL, MITIGATION, POWER_STRIKE, REPLANT, SMELTING, SNIPE, VENOM, WARPED, WITHER,

        FOCUS, RAPID_FIRE, SONIC_BOOM;

        public static Set<EnchantID> getVanillaEnchantIds() {
            return Set.of(
                AQUA_INFINITY, BANE_OF_ARTHROPODS, BINDING_CURSE, BLAST_PROTECTION, CHANNELING, DEPTH_STRIDER, EFFICIENCY, FEATHER_FALLING, FIRE_ASPECT,
                FIRE_PROTECTION, FLAME, FORTUNE, FROST_WALKER, IMPALING, INFINITY, KNOCKBACK, LOOTING, LOYALTY, LUCK_OF_THE_SEA, LURE, MENDING,
                MULTISHOT, PIERCING, POWER, PROJECTILE_PROTECTION, PROTECTION, PUNCH, QUICKCHARGE, RESPIRATION, RIPTIDE, SHARPNESS,
                SILK_TOUCH, SMITE, SOULSPEED, SWEEPING_EDGE, SWIFTSNEAK, THORNS, UNBREAKING, VANISHING_CURSE
            );
        }

        public static Set<EnchantID> getCustomEnchantIds() {
            return Set.of(
                AUTO_LOOTING, DELICATE, GROWTH, HOMING, IMMOLATE, LIFESTEAL, MITIGATION, POWER_STRIKE, REPLANT, SMELTING, SNIPE, VENOM, WARPED, WITHER
            );
        }

        public static Set<EnchantID> getAbilityEnchantIds() {
            return Set.of(
                FOCUS, RAPID_FIRE, SONIC_BOOM
            );
        }
    }

    Set<Enchantment> getEnchantments();
    Set<Enchantment> getEnchantments(Set<EnchantID> ids);
    Enchantment[] getApplicableEnchantments(ItemStack item);
    Enchantment getEnchantment(EnchantID enchantment);
}
