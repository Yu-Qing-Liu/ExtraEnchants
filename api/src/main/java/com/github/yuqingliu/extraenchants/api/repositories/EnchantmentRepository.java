package com.github.yuqingliu.extraenchants.api.repositories;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.persistence.Database;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public interface EnchantmentRepository {
    public enum Rarity {
        COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC;

        public NamedTextColor color() {
            return switch(this) {
                case COMMON -> NamedTextColor.GREEN;
                case UNCOMMON -> NamedTextColor.BLUE;
                case RARE -> NamedTextColor.DARK_PURPLE;
                case EPIC -> NamedTextColor.LIGHT_PURPLE;
                case LEGENDARY -> NamedTextColor.GOLD;
                case MYTHIC -> NamedTextColor.AQUA;
                default -> NamedTextColor.WHITE;
            };
        }

        public int rank() {
            return switch(this) {
                case COMMON -> 1;
                case UNCOMMON -> 2;
                case RARE -> 3;
                case EPIC -> 4;
                case LEGENDARY -> 5;
                case MYTHIC -> 6;
                default -> 0;
            };
        }
    }

    public enum EnchantID {
        // Vanilla enchants ids
        AQUA_INFINITY, BANE_OF_ARTHROPODS, BINDING_CURSE, BLAST_PROTECTION, CHANNELING, DEPTH_STRIDER, EFFICIENCY, FEATHER_FALLING, FIRE_ASPECT,
        FIRE_PROTECTION, FLAME, FORTUNE, FROST_WALKER, IMPALING, INFINITY, KNOCKBACK, LOOTING, LOYALTY, LUCK_OF_THE_SEA, LURE, MENDING,
        MULTISHOT, PIERCING, POWER, PROJECTILE_PROTECTION, PROTECTION, PUNCH, QUICK_CHARGE, RESPIRATION, RIPTIDE, SHARPNESS,
        SILK_TOUCH, SMITE, SOUL_SPEED, SWEEPING_EDGE, SWIFT_SNEAK, THORNS, UNBREAKING, VANISHING_CURSE,
        // Custom enchants ids
        AUTO_LOOTING, DELICATE, GROWTH, HOMING, IMMOLATE, LIFESTEAL, MITIGATION, POWER_STRIKE, REPLANT, SMELTING, SNIPE, VENOM, WARPED, WITHER,
        // Ability enchants ids
        FOCUS, RAPID_FIRE, SONIC_BOOM;
        
        public Rarity rarity() {
            return switch (this) {
                // COMMON RARITY
                case AUTO_LOOTING, DELICATE, REPLANT, SMELTING -> Rarity.COMMON;
                // UNCOMMON RARITY
                case AQUA_INFINITY, BANE_OF_ARTHROPODS, BLAST_PROTECTION, CHANNELING, DEPTH_STRIDER, EFFICIENCY, FEATHER_FALLING,
                     FIRE_ASPECT, FIRE_PROTECTION, FLAME, FORTUNE, FROST_WALKER, IMPALING, INFINITY, KNOCKBACK, LOOTING, LOYALTY, LUCK_OF_THE_SEA, 
                     LURE, MULTISHOT, PIERCING, POWER, PROJECTILE_PROTECTION, PROTECTION, PUNCH, QUICK_CHARGE, RESPIRATION, RIPTIDE, SHARPNESS,
                     SILK_TOUCH, SMITE, SOUL_SPEED, SWEEPING_EDGE, SWIFT_SNEAK, THORNS, UNBREAKING -> Rarity.UNCOMMON;
                // RARE RARITY
                case MENDING, VANISHING_CURSE, BINDING_CURSE, GROWTH, VENOM, WITHER -> Rarity.RARE;
                // EPIC RARITY
                case LIFESTEAL, POWER_STRIKE, SNIPE, WARPED -> Rarity.EPIC;
                case HOMING, IMMOLATE, MITIGATION -> Rarity.LEGENDARY;
                // MYTHIC RARITY
                case FOCUS, RAPID_FIRE, SONIC_BOOM -> Rarity.MYTHIC;
                // Default to COMMON if no match
                default -> Rarity.COMMON;
            };
        }

        private static final Map<Rarity, Set<EnchantID>> ENCHANTMENTS_BY_RARITY = new EnumMap<>(Rarity.class);

        static {
            for (EnchantID enchantID : EnchantID.values()) {
                Rarity rarity = enchantID.rarity();
                ENCHANTMENTS_BY_RARITY.computeIfAbsent(rarity, k -> new HashSet<>()).add(enchantID);
            }
        }

        public static Set<EnchantID> getEnchantmentIdsByRarity(Rarity rarity) {
            return ENCHANTMENTS_BY_RARITY.getOrDefault(rarity, Collections.emptySet());
        }
    }

    Enchantment getEnchantment(EnchantID enchantment);
    Enchantment getEnchantment(Component enchantName);
    Set<Enchantment> getEnchantments();
    Map<Enchantment, Integer> getEnchantments(ItemStack item);
    Map<Enchantment, Integer> getSortedEnchantments();
    Map<Enchantment, Integer> getSortedEnchantments(ItemStack item);
    Map<Enchantment, Integer> getSortedEnchantments(ItemStack item, Enchantment newEnchant, int newEnchantLevel);
    Enchantment[] getApplicableEnchantments(ItemStack item);
    Enchantment[] getApplicableEnchantmentsByRarity(ItemStack item, Rarity rarity);
    void setDatabase(Database database);
    void setEnchantmentMaxLevel(EnchantID id, int maxLevel);
    void addApplicable(EnchantID id, Item item);
    void postConstruct();
}
