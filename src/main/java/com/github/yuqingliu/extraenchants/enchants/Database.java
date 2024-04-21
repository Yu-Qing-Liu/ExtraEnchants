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
        CustomEnchantment homing = new CustomEnchantment("Homing", null, null, 1, NamedTextColor.GOLD, Arrays.asList(Material.BOW));
        homing.setDescription("Guided arrows");
        CustomEnchantment snipe = new CustomEnchantment("Snipe", null, null, 5, NamedTextColor.BLUE, Arrays.asList(Material.CROSSBOW));
        snipe.setDescription("Increase arrow velocity by 25% per level");
        CustomEnchantment flame = new CustomEnchantment("Flame", null, null, 1, NamedTextColor.GREEN, Arrays.asList(Material.CROSSBOW));
        flame.setDescription("Flaming arrows");
        CustomEnchantment mitigation = new CustomEnchantment("Mitigation", null, null, 5, NamedTextColor.BLUE, armor_applicable);
        mitigation.setDescription("1% chance to negate damage per level");
        CustomEnchantment growth = new CustomEnchantment("Growth", null, null, 5, NamedTextColor.BLUE, armor_applicable);
        growth.setDescription("Adds 1 HP per level");
        CustomEnchantment wither = new CustomEnchantment("Wither", null, null, 2, NamedTextColor.GREEN, weapons_applicable);
        wither.setDescription("Applies wither effect for 3 seconds per level");
        CustomEnchantment venom = new CustomEnchantment("Venom", null, null, 2, NamedTextColor.GREEN, weapons_applicable);
        venom.setDescription("Applies poison effect for 3 seconds per level");
        CustomEnchantment replant = new CustomEnchantment("Replant", null, null, 1, NamedTextColor.WHITE, hoe_applicable);
        replant.setDescription("Replants mature crops");
        CustomEnchantment sonic_boom = new CustomEnchantment("SonicBoom", null, null, 1, NamedTextColor.LIGHT_PURPLE, melee_applicable);
        sonic_boom.setDescription("Warden's ability");
        CustomEnchantment autolooting = new CustomEnchantment("AutoLooting", null, null, 1, NamedTextColor.WHITE, tools_weapons_applicable);
        autolooting.setDescription("Items go directly into your inventory");
        CustomEnchantment smelting = new CustomEnchantment("Smelting", null, null, 1, NamedTextColor.WHITE, pickaxe_applicable);
        smelting.setDescription("Smelts mined ores");
        CustomEnchantment delicate = new CustomEnchantment("Delicate", null, null, 1, NamedTextColor.WHITE, hoe_applicable);
        delicate.setDescription("Avoids breaking immature crops");
        CustomEnchantment powerstrike = new CustomEnchantment("PowerStrike", null, null, 5, NamedTextColor.DARK_PURPLE, melee_applicable);
        powerstrike.setDescription("Adds 20% more damage to the first hit dealt");
        CustomEnchantment focus = new CustomEnchantment("Focus", null, null, 5, NamedTextColor.LIGHT_PURPLE, Arrays.asList(Material.BOW));
        focus.setDescription("Marks a target. Deal 20% increased damage to the target per level");
        
        customEnchantmentRegistry.add(homing);
        customEnchantmentRegistry.add(snipe);
        customEnchantmentRegistry.add(flame);
        customEnchantmentRegistry.add(mitigation);
        customEnchantmentRegistry.add(growth);
        customEnchantmentRegistry.add(wither);
        customEnchantmentRegistry.add(venom);
        customEnchantmentRegistry.add(replant);
        customEnchantmentRegistry.add(sonic_boom);
        customEnchantmentRegistry.add(autolooting);
        customEnchantmentRegistry.add(smelting);
        customEnchantmentRegistry.add(delicate);
        customEnchantmentRegistry.add(powerstrike);
        customEnchantmentRegistry.add(focus);
    }

    public static List<CustomEnchantment> getCustomEnchantmentRegistry() {
        return customEnchantmentRegistry;
    }
}
