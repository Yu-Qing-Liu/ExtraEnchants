package com.github.yuqingliu.extraenchants.enchants;

import org.bukkit.Material;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ApplicableItemsRegistry {
    // Ranged weapons
    public static List<Material> ranged_applicable = Arrays.asList(Material.BOW, Material.CROSSBOW);
    // Melee weapons
    public static List<Material> melee_applicable = Arrays.asList(
        Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
        Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE
    );
    // All weapons
    public static List<Material> weapons_applicable = Arrays.asList(
        Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
        Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
        Material.BOW, Material.CROSSBOW
    );
    // Armor 
    public static List<Material> armor_applicable = Arrays.asList(
        Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
        Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
        Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
        Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
    );

    // Helmet
    public static List<Material> helmet_applicable = Arrays.asList(

    );

    // Chestplate
    public static List<Material> chestplate_applicable = Arrays.asList(
        Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE,
        Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE
    );
    
    // Leggings

    // Boots
    
    // Tools
    
    // Pickaxe

    // Axe
    
    // Shovel

    // Hoe
    public static List<Material> hoe_applicable = Arrays.asList(
        Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE,
        Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE
    );

    // Elytra
    public static List<Material> elytra_applicable = Arrays.asList(
        Material.ELYTRA
    );
}
