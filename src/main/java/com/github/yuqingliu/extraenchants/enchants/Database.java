package com.github.yuqingliu.extraenchants.enchants;

import com.github.yuqingliu.extraenchants.enchants.utils.CustomEnchantment;

import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Database extends ApplicableItemsRegistry {
    
    private static List<CustomEnchantment> customEnchantmentRegistry = new ArrayList<>();

    public static void registerEnchants(JavaPlugin plugin) {
        CustomEnchantment homing = new CustomEnchantment(plugin, "Homing", null, 1, NamedTextColor.GRAY, Arrays.asList(Material.BOW));
        CustomEnchantment snipe = new CustomEnchantment(plugin, "Snipe", null, 5, NamedTextColor.GRAY, Arrays.asList(Material.CROSSBOW));
        CustomEnchantment flame = new CustomEnchantment(plugin, "Flame", null, 1, NamedTextColor.GRAY, Arrays.asList(Material.CROSSBOW));
        CustomEnchantment mitigation = new CustomEnchantment(plugin, "Mitigation", null, 5, NamedTextColor.GRAY, armor_applicable);
        CustomEnchantment growth = new CustomEnchantment(plugin, "Growth", null, 5, NamedTextColor.GRAY, armor_applicable);
        CustomEnchantment wither = new CustomEnchantment(plugin, "Wither", null, 2, NamedTextColor.GRAY, weapons_applicable);
        CustomEnchantment venom = new CustomEnchantment(plugin, "Venom", null, 2, NamedTextColor.GRAY, weapons_applicable);
        CustomEnchantment replant = new CustomEnchantment(plugin, "Replant", null, 2, NamedTextColor.GRAY, hoe_applicable);
        CustomEnchantment sonic_boom = new CustomEnchantment(plugin, "Sonic Boom", null, 1, NamedTextColor.LIGHT_PURPLE, melee_applicable);
        
        customEnchantmentRegistry.add(homing);
        customEnchantmentRegistry.add(snipe);
        customEnchantmentRegistry.add(flame);
        customEnchantmentRegistry.add(mitigation);
        customEnchantmentRegistry.add(growth);
        customEnchantmentRegistry.add(wither);
        customEnchantmentRegistry.add(venom);
        customEnchantmentRegistry.add(replant);
        customEnchantmentRegistry.add(sonic_boom);
    }

    public static List<CustomEnchantment> getCustomEnchantmentRegistry() {
        return customEnchantmentRegistry;
    }
}
