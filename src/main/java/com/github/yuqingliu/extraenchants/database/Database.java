package com.github.yuqingliu.extraenchants.database;

import com.github.yuqingliu.extraenchants.utils.CustomEnchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Database {
    
    private static List<CustomEnchantment> customEnchantmentRegistry = new ArrayList<>();

    // Ranged weapons
    private static List<Material> ranged_weapons_applicable = Arrays.asList(Material.BOW, Material.CROSSBOW);
    // Melee weapons
    private static List<Material> melee_weapons_applicable = Arrays.asList(
        Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
        Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE
    );
    // All weapons
    private static List<Material> weapons_applicable = new ArrayList<>();
    // Armor 
    private static List<Material> armor_applicable = Arrays.asList(
        Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
        Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
        Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
        Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
    );
    // Hoes
    private static List<Material> hoe_applicable = Arrays.asList(
        Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE,
        Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE
    );

    public static void registerEnchants(JavaPlugin plugin) {
        weapons_applicable.addAll(ranged_weapons_applicable);
        weapons_applicable.addAll(melee_weapons_applicable);

        CustomEnchantment homing = new CustomEnchantment(plugin, "Homing", 1, Arrays.asList(Material.BOW));
        CustomEnchantment snipe = new CustomEnchantment(plugin, "Snipe", 5, Arrays.asList(Material.CROSSBOW));
        CustomEnchantment flame = new CustomEnchantment(plugin, "Flame", 1, Arrays.asList(Material.CROSSBOW));
        CustomEnchantment mitigation = new CustomEnchantment(plugin, "Mitigation", 5, armor_applicable);
        CustomEnchantment growth = new CustomEnchantment(plugin, "Growth", 5, armor_applicable);
        CustomEnchantment wither = new CustomEnchantment(plugin, "Wither", 2, weapons_applicable);
        CustomEnchantment venom = new CustomEnchantment(plugin, "Venom", 2, weapons_applicable);
        CustomEnchantment replant = new CustomEnchantment(plugin, "Replant", 1, hoe_applicable);
        
        customEnchantmentRegistry.add(homing);
        customEnchantmentRegistry.add(snipe);
        customEnchantmentRegistry.add(flame);
        customEnchantmentRegistry.add(mitigation);
        customEnchantmentRegistry.add(growth);
        customEnchantmentRegistry.add(wither);
        customEnchantmentRegistry.add(venom);
        customEnchantmentRegistry.add(replant);
    }

    public static List<CustomEnchantment> getCustomEnchantmentRegistry() {
        return customEnchantmentRegistry;
    }
}
