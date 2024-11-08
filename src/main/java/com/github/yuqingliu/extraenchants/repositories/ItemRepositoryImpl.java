package com.github.yuqingliu.extraenchants.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.item.ItemImpl;
import com.google.inject.Singleton;

import lombok.Getter;

@Getter
@Singleton
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<ItemCategory, Set<Item>> items = new HashMap<>();
    
    public ItemRepositoryImpl() {
        initializeItems();
    }

    private void initializeItems() {
        items.put(ItemCategory.BOW,
            Set.of(
                new ItemImpl(new ItemStack(Material.BOW))
            )
        );

        items.put(ItemCategory.CROSSBOW,
            Set.of(
                new ItemImpl(new ItemStack(Material.CROSSBOW))
            )
        );

        items.put(ItemCategory.ELYTRA,
            Set.of(
                new ItemImpl(new ItemStack(Material.ELYTRA))
            )
        );

        items.put(ItemCategory.MACE,
            Set.of(
                new ItemImpl(new ItemStack(Material.MACE))
            )
        );

        items.put(ItemCategory.RANGED,
            Set.of(
                new ItemImpl(new ItemStack(Material.BOW)),
                new ItemImpl(new ItemStack(Material.CROSSBOW))
            )
        );
        
        // Melee Weapons (Sword & Axe & Mace)
        items.put(ItemCategory.MELEE, 
            Set.of(
                new ItemImpl(new ItemStack(Material.WOODEN_SWORD)),
                new ItemImpl(new ItemStack(Material.STONE_SWORD)),
                new ItemImpl(new ItemStack(Material.IRON_SWORD)),
                new ItemImpl(new ItemStack(Material.GOLDEN_SWORD)),
                new ItemImpl(new ItemStack(Material.DIAMOND_SWORD)),
                new ItemImpl(new ItemStack(Material.NETHERITE_SWORD)),
                new ItemImpl(new ItemStack(Material.WOODEN_AXE)),
                new ItemImpl(new ItemStack(Material.STONE_AXE)),
                new ItemImpl(new ItemStack(Material.IRON_AXE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_AXE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_AXE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_AXE)),
                new ItemImpl(new ItemStack(Material.MACE))
            )
        );

        // Sword category
        items.put(ItemCategory.SWORD, 
            Set.of(
                new ItemImpl(new ItemStack(Material.WOODEN_SWORD)),
                new ItemImpl(new ItemStack(Material.STONE_SWORD)),
                new ItemImpl(new ItemStack(Material.IRON_SWORD)),
                new ItemImpl(new ItemStack(Material.GOLDEN_SWORD)),
                new ItemImpl(new ItemStack(Material.DIAMOND_SWORD)),
                new ItemImpl(new ItemStack(Material.NETHERITE_SWORD))
            )
        );

        // Axe category
        items.put(ItemCategory.AXE,
            Set.of(
                new ItemImpl(new ItemStack(Material.WOODEN_AXE)),
                new ItemImpl(new ItemStack(Material.STONE_AXE)),
                new ItemImpl(new ItemStack(Material.IRON_AXE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_AXE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_AXE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_AXE))
            )
        );

        // Armor category
        items.put(ItemCategory.ARMOR, 
            Set.of(
                new ItemImpl(new ItemStack(Material.LEATHER_HELMET)),
                new ItemImpl(new ItemStack(Material.LEATHER_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.LEATHER_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.LEATHER_BOOTS)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_HELMET)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_BOOTS)),
                new ItemImpl(new ItemStack(Material.IRON_HELMET)),
                new ItemImpl(new ItemStack(Material.IRON_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.IRON_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.IRON_BOOTS)),
                new ItemImpl(new ItemStack(Material.GOLDEN_HELMET)),
                new ItemImpl(new ItemStack(Material.GOLDEN_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.GOLDEN_BOOTS)),
                new ItemImpl(new ItemStack(Material.DIAMOND_HELMET)),
                new ItemImpl(new ItemStack(Material.DIAMOND_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.DIAMOND_BOOTS)),
                new ItemImpl(new ItemStack(Material.NETHERITE_HELMET)),
                new ItemImpl(new ItemStack(Material.NETHERITE_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.NETHERITE_BOOTS))
            )
        );

        // Helmet category
        items.put(ItemCategory.HELMET,
            Set.of(
                new ItemImpl(new ItemStack(Material.LEATHER_HELMET)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_HELMET)),
                new ItemImpl(new ItemStack(Material.IRON_HELMET)),
                new ItemImpl(new ItemStack(Material.GOLDEN_HELMET)),
                new ItemImpl(new ItemStack(Material.DIAMOND_HELMET)),
                new ItemImpl(new ItemStack(Material.NETHERITE_HELMET))
            )
        );

        // Chestplate category
        items.put(ItemCategory.CHESTPLATE,
            Set.of(
                new ItemImpl(new ItemStack(Material.LEATHER_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.IRON_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_CHESTPLATE))
            )
        );
        
        // Leggings category
        items.put(ItemCategory.LEGGING,
            Set.of(
                new ItemImpl(new ItemStack(Material.LEATHER_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.IRON_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.GOLDEN_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.DIAMOND_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.NETHERITE_LEGGINGS))
            )
        );

        // Boots category
        items.put(ItemCategory.BOOT,
            Set.of(
                new ItemImpl(new ItemStack(Material.LEATHER_BOOTS)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_BOOTS)),
                new ItemImpl(new ItemStack(Material.IRON_BOOTS)),
                new ItemImpl(new ItemStack(Material.GOLDEN_BOOTS)),
                new ItemImpl(new ItemStack(Material.DIAMOND_BOOTS)),
                new ItemImpl(new ItemStack(Material.NETHERITE_BOOTS))
            )
        );

        // Tools category
        items.put(ItemCategory.TOOL,
            Set.of(
                new ItemImpl(new ItemStack(Material.WOODEN_PICKAXE)),
                new ItemImpl(new ItemStack(Material.STONE_PICKAXE)),
                new ItemImpl(new ItemStack(Material.IRON_PICKAXE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_PICKAXE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_PICKAXE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_PICKAXE)),
                new ItemImpl(new ItemStack(Material.WOODEN_AXE)),
                new ItemImpl(new ItemStack(Material.STONE_AXE)),
                new ItemImpl(new ItemStack(Material.IRON_AXE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_AXE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_AXE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_AXE)),
                new ItemImpl(new ItemStack(Material.WOODEN_SHOVEL)),
                new ItemImpl(new ItemStack(Material.STONE_SHOVEL)),
                new ItemImpl(new ItemStack(Material.IRON_SHOVEL)),
                new ItemImpl(new ItemStack(Material.GOLDEN_SHOVEL)),
                new ItemImpl(new ItemStack(Material.DIAMOND_SHOVEL)),
                new ItemImpl(new ItemStack(Material.NETHERITE_SHOVEL)),
                new ItemImpl(new ItemStack(Material.WOODEN_HOE)),
                new ItemImpl(new ItemStack(Material.STONE_HOE)),
                new ItemImpl(new ItemStack(Material.IRON_HOE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_HOE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_HOE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_HOE))
            )
        );

        // Pickaxe category
        items.put(ItemCategory.PICKAXE,
            Set.of(
                new ItemImpl(new ItemStack(Material.WOODEN_PICKAXE)),
                new ItemImpl(new ItemStack(Material.STONE_PICKAXE)),
                new ItemImpl(new ItemStack(Material.IRON_PICKAXE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_PICKAXE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_PICKAXE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_PICKAXE))
            )
        );

        // Shovel category
        items.put(ItemCategory.SHOVEL,
            Set.of(
                new ItemImpl(new ItemStack(Material.WOODEN_SHOVEL)),
                new ItemImpl(new ItemStack(Material.STONE_SHOVEL)),
                new ItemImpl(new ItemStack(Material.IRON_SHOVEL)),
                new ItemImpl(new ItemStack(Material.GOLDEN_SHOVEL)),
                new ItemImpl(new ItemStack(Material.DIAMOND_SHOVEL)),
                new ItemImpl(new ItemStack(Material.NETHERITE_SHOVEL))
            )
        );

        // Hoe category
        items.put(ItemCategory.HOE,
            Set.of(
                new ItemImpl(new ItemStack(Material.WOODEN_HOE)),
                new ItemImpl(new ItemStack(Material.STONE_HOE)),
                new ItemImpl(new ItemStack(Material.IRON_HOE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_HOE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_HOE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_HOE))
            )
        );

        // All items category
        items.put(ItemCategory.ALL,
            Set.of(
                new ItemImpl(new ItemStack(Material.WOODEN_SWORD)),
                new ItemImpl(new ItemStack(Material.STONE_SWORD)),
                new ItemImpl(new ItemStack(Material.IRON_SWORD)),
                new ItemImpl(new ItemStack(Material.GOLDEN_SWORD)),
                new ItemImpl(new ItemStack(Material.DIAMOND_SWORD)),
                new ItemImpl(new ItemStack(Material.NETHERITE_SWORD)),
                new ItemImpl(new ItemStack(Material.WOODEN_AXE)),
                new ItemImpl(new ItemStack(Material.STONE_AXE)),
                new ItemImpl(new ItemStack(Material.IRON_AXE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_AXE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_AXE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_AXE)),
                new ItemImpl(new ItemStack(Material.BOW)),
                new ItemImpl(new ItemStack(Material.CROSSBOW)),
                new ItemImpl(new ItemStack(Material.MACE)),
                new ItemImpl(new ItemStack(Material.WOODEN_PICKAXE)),
                new ItemImpl(new ItemStack(Material.STONE_PICKAXE)),
                new ItemImpl(new ItemStack(Material.IRON_PICKAXE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_PICKAXE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_PICKAXE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_PICKAXE)),
                new ItemImpl(new ItemStack(Material.WOODEN_SHOVEL)),
                new ItemImpl(new ItemStack(Material.STONE_SHOVEL)),
                new ItemImpl(new ItemStack(Material.IRON_SHOVEL)),
                new ItemImpl(new ItemStack(Material.GOLDEN_SHOVEL)),
                new ItemImpl(new ItemStack(Material.DIAMOND_SHOVEL)),
                new ItemImpl(new ItemStack(Material.NETHERITE_SHOVEL)),
                new ItemImpl(new ItemStack(Material.WOODEN_HOE)),
                new ItemImpl(new ItemStack(Material.STONE_HOE)),
                new ItemImpl(new ItemStack(Material.IRON_HOE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_HOE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_HOE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_HOE)),
                new ItemImpl(new ItemStack(Material.LEATHER_HELMET)),
                new ItemImpl(new ItemStack(Material.LEATHER_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.LEATHER_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.LEATHER_BOOTS)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_HELMET)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.CHAINMAIL_BOOTS)),
                new ItemImpl(new ItemStack(Material.IRON_HELMET)),
                new ItemImpl(new ItemStack(Material.IRON_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.IRON_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.IRON_BOOTS)),
                new ItemImpl(new ItemStack(Material.GOLDEN_HELMET)),
                new ItemImpl(new ItemStack(Material.GOLDEN_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.GOLDEN_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.GOLDEN_BOOTS)),
                new ItemImpl(new ItemStack(Material.DIAMOND_HELMET)),
                new ItemImpl(new ItemStack(Material.DIAMOND_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.DIAMOND_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.DIAMOND_BOOTS)),
                new ItemImpl(new ItemStack(Material.NETHERITE_HELMET)),
                new ItemImpl(new ItemStack(Material.NETHERITE_CHESTPLATE)),
                new ItemImpl(new ItemStack(Material.NETHERITE_LEGGINGS)),
                new ItemImpl(new ItemStack(Material.NETHERITE_BOOTS)),
                new ItemImpl(new ItemStack(Material.ELYTRA))
            )
        );
    }
}
