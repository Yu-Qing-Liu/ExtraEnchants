package com.github.yuqingliu.extraenchants.item;

import org.bukkit.Material;

import lombok.Getter;

import java.util.List;
import java.util.Arrays;

@Getter
public class ApplicableItemsRegistry {
    // Ranged weapons
    private List<Material> rangedApplicable = Arrays.asList(Material.BOW, Material.CROSSBOW);

    // Melee weapons
    private List<Material> meleeApplicable = Arrays.asList(
        Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
        Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE
    );

    // All weapons
    private List<Material> weaponsApplicable = Arrays.asList(
        Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
        Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
        Material.BOW, Material.CROSSBOW
    );

    // Armor 
    private List<Material> armorApplicable = Arrays.asList(
        Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
        Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
        Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
        Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
    );

    // Helmet
    private List<Material> helmetApplicable = Arrays.asList(
        Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET,
        Material.DIAMOND_HELMET, Material.NETHERITE_HELMET
    );

    // Chestplate
    private List<Material> chestplateApplicable = Arrays.asList(
        Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE,
        Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE
    );
    
    // Leggings
    private List<Material> leggingsApplicable = Arrays.asList(
        Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS,
        Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS
    );

    // Boots
    private List<Material> bootsApplicable = Arrays.asList(
        Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS,
        Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS
    );
    
    // All Tools
    private List<Material> toolsApplicable = Arrays.asList(
        Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
        Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL,
        Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL,
        Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE,
        Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE
    );

    // Pickaxe
    private List<Material> pickaxeApplicable = Arrays.asList(
        Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE
    );

    // Axe
    private List<Material> axeApplicable = Arrays.asList(
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE
    );

    // Shovel
    private List<Material> shovelApplicable = Arrays.asList(
        Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL,
        Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL
    );

    // Hoe
    private List<Material> hoeApplicable = Arrays.asList(
        Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE,
        Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE
    );

    // Elytra
    private List<Material> elytraApplicable = Arrays.asList(
        Material.ELYTRA
    );

    // Bow
    private List<Material> bowApplicable = Arrays.asList(
        Material.BOW
    );

    // CrossBow
    private List<Material> crossbowApplicable = Arrays.asList(
        Material.CROSSBOW
    );

    // All tools and weapons
    private List<Material> toolsWeaponsApplicable = Arrays.asList(
        Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
        Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
        Material.BOW, Material.CROSSBOW,
        Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
        Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL,
        Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL,
        Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE,
        Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE
    );
    
    // All tools, weapons and armor
    private List<Material> equipmentApplicable = Arrays.asList(
        Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
        Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
        Material.BOW, Material.CROSSBOW, Material.ELYTRA,
        Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE,
        Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
        Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
        Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL,
        Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL,
        Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE,
        Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE,
        Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
        Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
        Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
        Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
    );
}
